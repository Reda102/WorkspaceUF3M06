package com.example.demo.database

import java.sql.Connection
import java.sql.DriverManager


const val MYSQL_URL = "jdbc:mysql://localhost:3306/oficio"
const val MYSQL_USER  = "root"
const val MYSQL_PASSWORD  = "root"

object Database {
    var connection: Connection

    init {
//        Class.forName("com.mysql.jdbc.Driver")
        connection = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
    }

    fun closeConnection() {
        if (!connection.isClosed)
            connection.close()
    }
}