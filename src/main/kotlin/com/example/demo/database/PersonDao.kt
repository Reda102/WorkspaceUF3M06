package com.example.demo.database

import com.example.demo.model.Person
import java.sql.Timestamp


object PersonDao {

    private val connection = Database.connection

    private fun getLast(): PersonEntity {
        var last: PersonEntity? = null
        val resultSet = connection.createStatement().executeQuery("SELECT * FROM person p ORDER BY id DESC")
        if (resultSet.next()) last = PersonEntity(
            resultSet.getInt("id"),
            resultSet.getString("name"),
            resultSet.getString("title"))
        return last!!
    }

    fun save (person: PersonEntity): PersonEntity {
        val preparedStatement = connection.prepareStatement("INSERT INTO person(name, title) VALUES (?, ?)")
        preparedStatement.setString(1, person.name)
        preparedStatement.setString(2, person.title)
        preparedStatement.executeUpdate()
        preparedStatement.close()
        return getLast()
    }

    fun readAll(): List<PersonEntity> {
        val resultSet = connection.createStatement().executeQuery("SELECT * FROM person p WHERE p.date_deleted = 0  ORDER BY name")
        val personList = ArrayList<PersonEntity>()
        while (resultSet.next()) {
            val id = resultSet.getInt("id")
            val name = resultSet.getString("name")
            val title = resultSet.getString("title")
            personList += PersonEntity(id, name, title)
        }
        resultSet.close()
        return personList
    }

    fun update(person: PersonEntity) {
        val preparedStatement = connection.prepareStatement("UPDATE person p set p.name = ?, p.title = ? WHERE p.id = ?")
        preparedStatement.setString(1, person.name)
        preparedStatement.setString(2, person.title)
        preparedStatement.setInt(3, person.id)
        preparedStatement.executeUpdate()
        preparedStatement.close()
    }

    fun delete(person: PersonEntity) {
        val dateDeleted = System.currentTimeMillis()
        val preparedStatement = connection.prepareStatement("DELETE FROM person  WHERE id = ?")
        preparedStatement.setInt(1, person.id)
        preparedStatement.executeUpdate()
        preparedStatement.close()
    }

    fun closeConnection() = Database.closeConnection()
}