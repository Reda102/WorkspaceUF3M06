package com.example.demo.view

import com.example.demo.controller.PersonController
import com.example.demo.database.toPerson
import com.example.demo.model.Person
import javafx.application.Platform
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.input.KeyCombination
import javafx.scene.layout.BorderPane
import tornadofx.*
import tornadofx.controlsfx.infoNotification

class TablePersonEditor : View("Person Editor") {
    override val root: BorderPane = BorderPane()
    private val persons: ObservableList<Person>
    private var model: TableViewEditModel<Person> by singleAssign()
    private val controller: PersonController by inject()

    init {
        persons = controller.persons()
        with(root) {
            top {
                menubar {
                    menu("File") {
//                        menu("New") {
//                            item("Facebook")
//                            item("Twitter")
//                        }
//                        separator()
                        item("Add", KeyCombination.valueOf("Shortcut+A")).action {
//                            model.commit()
                            save(Person(name = "New", title = "New"))
                        }
                        item("Quit", KeyCombination.valueOf("Shortcut+Q")).action {
                            Platform.exit()
                        }
                    }
                    menu("Edit") {
                        item("Copy")
                        item("Paste")
                    }
                }
            }
            center {
                tableview(persons) {
                    column("Name", Person::nameProperty).makeEditable()
                    column("Title", Person::titleProperty).makeEditable()
                    contextmenu {
                        item("Remove").action {
                            selectedItem?.apply { delete(this) }
                        }
                        item("Update").action {
                            selectedItem?.apply {
                                model.commit()
                                update(this)
                            }
                        }
                        item("Change Status").action {
                            selectedItem?.apply { println("Changing Status for $name") }
                        }
                    }
                    isEditable = true
                    enableCellEditing()
                    enableDirtyTracking()
                    model = editModel
                }
            }
            bottom {
                buttonbar {
                    button("COMMIT").setOnAction {
                        model.items.forEach {
                            if (it.value.isDirty) {
                                update(it.key)
                                //println(it.key)
                            }
                        }
                        model.commit()
                    }
                    button("ROLLBACK").setOnAction {
                        model.rollback()
                    }
                }
            }
        }
    }

    override fun onDock() {
        super.onDock()
        currentWindow?.setOnCloseRequest { controller.closeConnection() }
    }

    private fun update(person: Person) {
        if (person.id > 0) {
            controller.update(person)
            infoNotification(
                messages["person_editor"],
                """${person}: ${messages["el_registro_se_ha_actualizado_correctamente"]}""",
                Pos.CENTER)
        } else {
            // save new record???
            infoNotification(messages["person_editor"], messages["no_hay_nada_que_actualizar"], Pos.CENTER)
        }
    }

    private fun save(person: Person) {
        val record = controller.save(person)
        persons.add(record.toPerson())
//        persons.sortBy { it.name }
        infoNotification(
            messages["person_editor"],
            """${person}: ${messages["el_registro_se_ha_guardado_satisfactoriamente"]}""",
            Pos.CENTER)
    }

    private fun delete(person: Person): Unit {
        if (person.id > 0) {
            messages
            confirm(title = messages["person_editor"], header = messages["estas_seguro"]) {
                controller.delete(person)
                persons.remove(person)
                information(
                    title = messages["person_editor"],
                    header = messages["el_registro_se_ha_eliminado_correctamente"],
                    content = """${person.name} ${messages["se_ha_marcado_como_borrado"]}""")
            }
        } else {
            infoNotification(
                messages["person_editor"],
                messages["el_registro_no_esta_presente_en_la_tabla"],
                Pos.CENTER)
        }
    }

}