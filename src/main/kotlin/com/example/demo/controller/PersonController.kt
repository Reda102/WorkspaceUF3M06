package com.example.demo.controller

import com.example.demo.database.PersonDao
import com.example.demo.database.PersonEntity
import com.example.demo.model.Person
import com.example.demo.utils.Utils.toEntity
import tornadofx.*

class PersonController : Controller() {

    private val personDao = PersonDao

    fun update(person: Person) {
        personDao.update(person.toEntity())
    }

    fun save(person: Person) : PersonEntity {
        return personDao.save(person.toEntity())
    }

    fun delete(person: Person) {
        personDao.delete(person.toEntity())
    }

    fun persons() = personDao.readAll().map { entity ->
        Person(entity.id, entity.name, entity.title)
    }.observable()

    fun closeConnection() = PersonDao.closeConnection()

}