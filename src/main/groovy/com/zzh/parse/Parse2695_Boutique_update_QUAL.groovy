package com.zzh.parse
import com.zzh.parse.tool.Csv
import com.zzh.parse.tool.Excel;

println """START TRANSACTION;
"""
def excel = Excel.parse("C:\\Users\\Zenghua.Zhang\\Desktop\\btq_list.xlsx",0)

def codes = []

excel.values.each{Map line ->
    def code = line["BOUTIQUE_CODE"] as Integer

    code = String.format("%04d",code)
    codes << code

    println """INSERT INTO swsecn_qual_plus.ref_boutique_list (`COUNTRY`, `BRAND`, `BOUTIQUE_CODE`, `LIBELLE_BOUTIQUE`, `PICKUP_AUTHORIZED`, `ACTIVE_BOOLEAN`, `BSAOD_AUTHORIZED`)
VALUES ('${line["COUNTRY"]}', '${line["BRAND"]}', '${code}', '${line["LIBELLE_BOUTIQUE"]}', 1, 1, 1) 
ON DUPLICATE KEY UPDATE `LIBELLE_BOUTIQUE` = '${line["LIBELLE_BOUTIQUE"]}';
"""
}

def codes_str = codes.collect{"'"+it+"'"}.join(", ")

println """DELETE FROM swsecn_qual_plus.boutique_address WHERE boutique_id in (SELECT id
FROM swsecn_qual_plus.ref_boutique_list 
WHERE `COUNTRY` = 'CN' and `BRAND` = 'CA' 
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
    def code = line["BOUTIQUE_CODE"] as Integer

    code = String.format("%04d",code)
    codes << code
    for(int i = 12;i < excel.header.size();i++){
        def head = excel.header[i]
        def v = "'${line[head]}'"
        if(head == 'ACTIVATE'){
            v = 1
        }else if(head == 'BOUTIQUE_ID'){
            v  = """(SELECT ID FROM swsecn_qual_plus.ref_boutique_list WHERE `BOUTIQUE_CODE` = '${code}' and `COUNTRY` = '${line["COUNTRY"]}' and `BRAND` = '${line["BRAND"]}')"""
        }else if(v.isEmpty() || v == "'NULL'"){
            v = "NULL"
        }
        value << v
    }
    values << "(${value.join(', ')})"
}
def v = values.join(""",
""")
println """INSERT INTO swsecn_qual_plus.boutique_address (${columns.join(", ")})
VALUES 
${v};
"""

println """COMMIT;
"""