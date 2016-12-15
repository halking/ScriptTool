package com.zzh.parse.tool

import java.nio.file.Paths
import org.apache.poi.xssf.usermodel.*
import static org.apache.poi.ss.usermodel.Cell.*

class Excel {
    static Content parse(String fileName, Integer sheetNum){
        
        def header = []
        def values = []
        
        Paths.get(fileName).toFile().withInputStream { input ->
            def workbook = new XSSFWorkbook(input)
            def sheet = workbook.getSheetAt(sheetNum)
            
            for (cell in sheet.getRow(0).cellIterator()) {
                header << cell.stringCellValue
            }
        
            def headerFlag = true
            for (row in sheet.rowIterator()) {
                if (headerFlag) {
                    headerFlag = false
                    continue
                }
                Map rowData = [:]
                for (cell in row.cellIterator()) {
                    def value = ''
        
                    switch(cell.cellType) {
                        case CELL_TYPE_STRING:
                            value = cell.stringCellValue
                            break
                        case CELL_TYPE_NUMERIC:
                            value = cell.numericCellValue
                            break
                        default:
                            value = cell.stringCellValue
                    }
                    String key = header[cell.columnIndex]
                    
                    rowData.put(key, value)
                }
                values << rowData
            }
        }
        
        def content = new Content();
        
        content.header = header
        
        content.setValues(values)
        
        return content
    }
}
