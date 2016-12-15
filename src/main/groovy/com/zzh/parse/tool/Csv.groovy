package com.zzh.parse.tool
import org.apache.commons.csv.CSVParser
import static org.apache.commons.csv.CSVFormat.*
import static com.xlson.groovycsv.CsvParser.parseCsv


class Csv {
    static Content parse(String fileName){
        def data = parseCsv(new File(fileName).getText())
        def values = []
        boolean firstLine = true
        def header
        for(line in data) {
            def l = line.toMap();
            if(firstLine){
                firstLine = false
                header = l.keySet() as List
            }
            values << l
        }


        def content = new Content()
        content.setHeader(header)

        content.setValues(values)

        return content
    }
}
