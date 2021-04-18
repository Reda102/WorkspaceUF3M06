package com.example.demo.model

import javafx.beans.property.Property
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class Person(id: Int = 0, name: String? = null, title: String? = null) {
    val idProperty: SimpleIntegerProperty = SimpleIntegerProperty(this, "id", id)
    var id: Int by idProperty

    val nameProperty: SimpleStringProperty = SimpleStringProperty(this, "name", name)
    var name: String by nameProperty

    val titleProperty: SimpleStringProperty = SimpleStringProperty(this, "title", title)
    var title: String by titleProperty



class PersonModel(person: Person) : ItemViewModel<Person>(person) {
    val id: Property<Number> = bind(Person::idProperty)
    val name: Property<String> = bind(Person::nameProperty)
    val title: Property<String> = bind(Person::titleProperty)

    override fun onCommit() {
        super.onCommit()
        println("onCommit invoked")
    }

    override fun onCommit(commits: List<Commit>) {
        // The println will only be called if findChanged is not null
        commits.findChanged(name)?.let { println("First-Name changed from ${it.second} to ${it.first}") }
        commits.findChanged(title)?.let { println("Title-Name changed from ${it.second} to ${it.first}") }
    }

    private fun <T> List<Commit>.findChanged(ref: Property<T>): Pair<T, T>? {
        val commit: Commit? = find { it.property == ref && it.changed }
        return commit?.let { (it.newValue as T) to (it.oldValue as T) }
    }

}
}



