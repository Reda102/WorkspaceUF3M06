package com.example.demo.database

import com.example.demo.model.Person

data class PersonEntity(val id: Int = 0, val name: String, val title: String)

fun PersonEntity.toPerson() = Person(id, name, title)