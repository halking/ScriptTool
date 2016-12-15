package com.zzh.parse

import com.zzh.parse.tool.Excel;

def excel = Excel.parse("C:\\Users\\Zenghua.Zhang\\Desktop\\Cartier.xlsx",0)

def excel1 = Excel.parse("C:\\Users\\Zenghua.Zhang\\Desktop\\Cartier.xlsx",1)

def list = []
excel.values.each{
    Map line ->
    def l = [];
    line.each{
        def v = it.value == 'NULL' ? " is null" : " = '${it.value}'"
        l << "`"+it.key+"`" + v
    }
    list << " ( " + l.join(" and ")+" ) "
}
excel1.values.each{
    Map line ->
    def l = [];
    line.each{
        def v = it.value == 'NULL' ? " is null" : " = '${it.value}'"
        l << "`"+it.key+"`" + v
    }
    list << " ( " + l.join(" and ")+" ) "
}
println "select * from `boutique_address` where "+list.join(""" or
""" )