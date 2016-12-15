package com.zzh.parse

import java.util.regex.Matcher
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//INC1321452
def file = new File("C:/Users/Zenghua.Zhang/Desktop/INC1321452.txt")
def text = new StringBuilder()
def separator = System.getProperty("line.separator")
int count = 0

new XSSFWorkbook().with { workbook ->
    int rowCount = 1;

    XSSFSheet sheet = workbook.createSheet("Failed Emails");

    def pritLast = {String content,int index ->
        def lines = content.readLines()
        def firstRow = lines.first().split("\\s")

        XSSFRow row = sheet.createRow(rowCount++);

        for(int i = 0;i<7;i++){
            firstRow[i] = firstRow[i] == "\\N" ? "NULL" : firstRow[i]
        }

        XSSFCell cell = row.createCell(0);
        cell.setCellValue(firstRow[0].toString())

        cell = row.createCell(1);
        cell.setCellValue(firstRow[1].toString())

        cell = row.createCell(2);
        cell.setCellValue(firstRow[2].toString())

        cell = row.createCell(3);
        cell.setCellValue(firstRow[3].toString())

        cell = row.createCell(4);
        cell.setCellValue(firstRow[4] +" "+firstRow[5])

        cell = row.createCell(5);
        cell.setCellValue(firstRow[6].toString())


        def last = lines.last().split("\\s").last()

        def pattern = ~/\<\?xml.*/
        def m = pattern.matcher(lines.first())
        StringBuilder xml = new StringBuilder()
        xml.append(m[0]).append(separator)
        for(int i=1;i<lines.size()-1;i++){
            xml.append(lines[i]).append(separator)
        }
        String lastLine = lines.last()+""
        int ind = lastLine.lastIndexOf(last);

        xml.append(lastLine.substring(0, ind))
        cell = row.createCell(6);
        cell.setCellValue(xml.toString())

        cell = row.createCell(7);
        last = last == '\\N'? 'NULL' : last
        cell.setCellValue(last.toString())

        println rowCount

    }


    file.eachLine{line , index ->
        if(index == 1){
            text.append(line).append(separator)
        }else    if(!line.contains("<?xml")){
            text.append(line.toString()).append(separator)
            return
        }else{
            pritLast(text.toString() , index)
            text = new StringBuilder()
            text.append(line.toString()).append(separator)
        }
    }
    pritLast(text.toString() , -1)


    new File( 'report.xlsx' ).withOutputStream { os -> write( os ) }
}



