package com.zzh.parse
import java.nio.file.Paths
import org.apache.poi.xssf.usermodel.*;
import com.zzh.parse.tool.Csv
import com.zzh.parse.tool.Excel;
import static org.apache.poi.ss.usermodel.Cell.*;

def excel = Excel.parse("C:\\Users\\Zenghua.Zhang\\Desktop\\Cartier.xlsx",0)
def csv = Csv.parse('C:\\Users\\Zenghua.Zhang\\Desktop\\QUAL.csv')

def csvMap = Csv.parse('C:\\Users\\Zenghua.Zhang\\Desktop\\ref_boutique.csv')
def codeMap = [:]

csvMap.values.each{
    codeMap.put(it["ID"] , it["BOUTIQUE_CODE"])
}


def existsIds = []
csv.values.each{ existsIds << it["ID"] }

def insertList = []

excel.values.each{Map line ->
    if(!existsIds.contains(line["ID"])){
        def code = codeMap[line["BOUTIQUE_ID"]]
        println """SELECT @boutique${code} := id from swse_qualif_plus_temp.`ref_boutique_list` where COUNTRY='CH' and BRAND = 'CA' and BOUTIQUE_CODE = '${code}';"""
    }
}

println ""

excel.values.each {Map line ->
    if(existsIds.contains(line["ID"])){
        def s = """UPDATE swse_qualif_plus_temp.`boutique_address` SET
"""
        def l = []
        line.each {
            if(it.key != "ID" && it.key != "COUNTRY" && it.key != "BRAND" && it.key !="BOUTIQUE_ID"){
                def v = it.value == "NULL" ? "NULL" : "'${it.value}'"
                if(it.key == "ACTIVATE"){
                    v = it.value
                }
                l << "`"+it.key+"`" + " = " + v
            }
        }
        s += l.join(""" ,
""")
        def id = line["ID"]
        s += """
WHERE `ID` = ${line["ID"]} and `COUNTRY` = '${line["COUNTRY"]}' and `BRAND` = '${line["BRAND"]}' and `BOUTIQUE_ID` = '${line["BOUTIQUE_ID"]}' limit 1;
"""
        println s
    }else{
        def s = """UPDATE swse_qualif_plus_temp.`boutique_address` SET
"""
        def l = []
        line.each {
            if(it.key != "ID" && it.key != "COUNTRY" && it.key != "BRAND" && it.key !="BOUTIQUE_ID"){
                def v = it.value == "NULL" ? "NULL" : "'${it.value}'"
                if(it.key == "ACTIVATE"){
                    v = it.value
                }
                l << "`"+it.key+"`" + " = " + v
            }
        }
        s += l.join(""" ,
""")
        def id = line["ID"]
        def code = codeMap[line["BOUTIQUE_ID"]]
        s += """
WHERE `COUNTRY` = '${line["COUNTRY"]}' and `BRAND` = '${line["BRAND"]}' and `BOUTIQUE_ID` = @boutique${code} limit 1;
"""
        println s
    
    }
}

