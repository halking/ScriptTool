package com.zzh.db


import groovy.sql.Sql

def sql = Sql.newInstance("jdbc:mysql://localhost:3306/swse_plus", "root","root", "com.mysql.jdbc.Driver")

//Drop table if it already exists
sql.execute('drop TABLE users')

//Create table users
sql.execute('create table users(id INT NOT NULL AUTO_INCREMENT,name VARCHAR(15) NOT NULL,email VARCHAR(15), PRIMARY KEY(id))')

//Insert some values
sql.execute('insert into users values(null,"John Doe","jd@test.com")')
sql.execute('insert into users values(null,"Joe Parker","jp@test.com")')
sql.execute('insert into users values(null,"Tom Pecker","tp@test.com")')

//We can also insert by prepared statements by
sql.execute('insert into users values(null,?,?)',['Jill Peter', 'jill@test.com'])

//Or for better reuse
def InsertQuery = "insert into users values(null,?,?)"
sql.execute(InsertQuery,['Harry Costa', 'hc@test.com'])

//Print single row
println "\n------------------------------Print Single Row--------------------------------------------------------\n"
def row = sql.firstRow('select * from users where name = "John Doe"')
println "Row: id = ${row.id} and email = ${row.email}"

//Printing Multiple rows with rows (user handles each row with each)
println "\n------------------------------Print Multiple Rows (using rows)----------------------------------------\n"
println "ID    NAME         EMAIL"
def fetch = sql.rows("select * from users")
fetch.each { it ->
    println it.id + " " + it.name + " " +it.email
}

//Printing Multiple rows with eachRow (closure passed in as the second parameter should handle each row)
println "\n------------------------------Print Multiple Rows (using eachRow)-------------------------------------\n"
sql.eachRow("select * from users") { printrow -> println "$printrow.id $printrow.name $printrow.email" }

//Delete a row
println "\n------------------------------Delete an entry from table----------------------------------------------\n"
sql.execute('delete from users where id = ?' , [5])
printDbValues(sql)

//Update a value
println "\n------------------------------Update an entry in table------------------------------------------------\n"
sql.executeUpdate('update users set email = ? where id=4', ["pj@test.com"])
printDbValues(sql)

//Define a function for print reuse
println "\n------------------------------------------------------------------------------------------------------\n"
def printDbValues(sql) {
    sql.eachRow("select * from users") { printrow -> println "$printrow.id $printrow.name $printrow.email" }
}