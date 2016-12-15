package com.zzh.parse

import com.zzh.parse.tool.Excel;

def excel = Excel.parse("C:\\Users\\Zenghua.Zhang\\Desktop\\20161807boutiques.xlsx",0)


def ch_codes = []
def eu_codes = []

excel.values.each{Map line ->
    def code = line["BOUTIQUE_CODE"] as String
    code = code.split("\\.")[0]
    def country
    if(line["COUNTRY"] == 'CH'){
        country = 'CH'
        ch_codes << code
    }else{
        country = 'EU'
        eu_codes << code
    }

    println """UPDATE swse_plus.`ref_boutique_list` SET
`LIBELLE_BOUTIQUE` = '${line["LIBELLE_BOUTIQUE"]}'
WHERE `BOUTIQUE_CODE` = '${code}' and `COUNTRY` = '${country}' and `BRAND` = '${line["BRAND"]}' limit 1;
"""
}

def codes_str = ch_codes.collect{"'"+it+"'"}.join(", ")

println """DELETE FROM swse_plus.boutique_address WHERE boutique_id in (SELECT id
FROM swse_plus.ref_boutique_list 
WHERE `COUNTRY` = 'CH' and `BRAND` = 'CA' 
AND boutique_code IN (${codes_str})) 
AND address_type='01';
"""

codes_str = eu_codes.collect{"'"+it+"'"}.join(", ")
println """DELETE FROM swse_plus.boutique_address WHERE boutique_id in (SELECT id
FROM swse_plus.ref_boutique_list 
WHERE `COUNTRY` = 'EU' and `BRAND` = 'CA' 
AND boutique_code IN (${codes_str})) 
AND address_type='01';
"""

def columns = []
for(int i = 12;i < excel.header.size();i++){
    columns << excel.header[i]
}

def values = []

excel.values.each{Map line ->
    def value = []
    def code = line["BOUTIQUE_CODE"] as String
    code = code.split("\\.")[0]
    def country = line["COUNTRY"] == 'CH' ? "'CH'" : "'EU'"
    for(int i = 12;i < excel.header.size();i++){
        def head = excel.header[i]
        def v = "'${line[head]}'"
        if(head == 'ACTIVATE'){
            v = 1
        }else if(head == 'BOUTIQUE_ID'){
            v  = """(SELECT ID FROM swse_plus.ref_boutique_list WHERE `BOUTIQUE_CODE` = '${code}' and `COUNTRY` = ${country} and `BRAND` = '${line["BRAND"]}')"""
        }else if(head == 'COUNTRY'){
            v  = country
        }else if(v.isEmpty() || v == "'NULL'"){
            v = "NULL"
        }
        value << v
    }
    
    values << "(${value.join(', ')})"
}
def v = values.join(""",
""")
println """INSERT INTO swse_plus.boutique_address (${columns.join(", ")})
VALUES
${v};
"""

