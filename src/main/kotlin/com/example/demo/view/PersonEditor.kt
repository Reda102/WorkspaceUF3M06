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

class PersonEditor : View("Person Editor") {
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
                        menu("Connect") {
                            item("Facebook")
                            item("Twitter")
                        }
                        item("Add", KeyCombination.valueOf("Shortcut+A")).action {
//                            model.commit()
                            save(Person(name = "Escribe nombre", title = "Esbribe titulo"))
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
                    column("Nombre", Person::nameProperty).makeEditable()
                    column("Titulo", Person::titleProperty).makeEditable()

                    contextmenu {
                        item("Borrar").action {
                            selectedItem?.apply {  delete(this) }
                        }
                        item("Actualizar").action {
                            selectedItem?.apply {
                                model.commit()
                                update(this)
                            }
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
                        model.commit()
                        model.items.forEach {
                            if (it.value.isDirty) {}
                        }
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
                messages["el_registro_se_ha_actualizado_correctamente"],
                Pos.CENTER
            )
        } else {
            infoNotification(messages["person_editor"], messages["no_hay_nada_que_actualizar"], Pos.CENTER)
        }
    }

    private fun save(person: Person) {
        val record = controller.save(person)
        persons.add(record.toPerson())

//        clients.sortBy { it.name }
        infoNotification(
            messages["person_editor"],
            messages["el_registro_se_ha_guardado_satisfactoriamente"],
            Pos.CENTER
        )
    }

    private fun delete(person: Person) {
        if (person.id > 0) {
            messages
            confirm(title = messages["client_editor"], header = messages["estas_seguro"]) {
                controller.delete(person)
                persons.remove(person)
                information(
                    title = messages["person_editor"],
                    header = messages["el_registro_se_ha_eliminado_correctamente"],
                    content = """${person.name} ${messages["se_ha_marcado_como_borrado"]}"""
                )
            }
        } else {
            infoNotification(messages["person_editor"], messages["el_registro_no_esta_presente_en_la_tabla"], Pos.CENTER)
        }
    }

}