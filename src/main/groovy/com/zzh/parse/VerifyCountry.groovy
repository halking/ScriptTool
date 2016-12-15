package com.zzh.parse

import java.nio.file.Paths
import org.apache.poi.xssf.usermodel.*;
import com.zzh.parse.tool.Csv
import com.zzh.parse.tool.Excel;
import static org.apache.poi.ss.usermodel.Cell.*;

def excel = Excel.parse("C:\\Users\\Zenghua.Zhang\\Desktop\\back_in_stock.xlsx",1)

def firstMap = [:]
def references = [] as Set
excel.values.each{Map line ->
   references <<  "'"+line['reference']+"'"
}

println references.join(",")

def m = new HashMap<String, String>();

Queue a;