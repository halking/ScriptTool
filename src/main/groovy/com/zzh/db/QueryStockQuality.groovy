package com.zzh.db

"""SELECT distinct REGISTRATION_ID as id FROM swse_plus.registration_information
 where CHARACTERISTIC = 'CREATION_REQUEST' 
 and STR_TO_DATE(CHARACTERISTIC_VALUE,'%Y-%m-%d %T') > '2016-03-01 00:00:00'"""

import groovy.sql.Sql

def sql = Sql.newInstance("jdbc:mysql://localhost:3306/swse_plus?useUnicode=true&characterEncoding=utf-8&useSSL=false", "root","root", "com.mysql.jdbc.Driver")


def registrations = sql.rows("select * from REGISTRATION where removed = 0 and REGISTRATION_TYPE = 'ZZSTOCKREMINDEREMAIL' order by id")

Date mimDate = Date.parse( 'yyyy-MM-dd', '2016-03-01' )

def references = [] as Set

registrations.each{registration ->
    def id = registration.id
    def infor = sql.firstRow("select * from registration_information where registration_id = ${id} and parent_id is null ")
    if(infor){
        Date date = Date.parse( 'yyyy-MM-dd hh:mm:ss', infor.CHARACTERISTIC_VALUE)

        if(date > mimDate){
            def reference = sql.firstRow("select CHARACTERISTIC_VALUE from registration_information where registration_id = ${id} and parent_id  = ${infor.id} and CHARACTERISTIC = 'REFERENCE'")

            references << reference
        }
    }
}


