package br.com.miagui.services

import br.com.miagui.model.Person
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong
import java.util.logging.Logger

@Service
class PersonService {

    private val counter: AtomicLong = AtomicLong()

    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAll(): List<Person>  {

        logger.info("Finding all Persons!")
        val persons: MutableList<Person> = ArrayList()

        for (i in 1..7) {
            val person = mockPerson(i)
            persons.add(person)
        }

        return persons
    }

    fun findById(id: Long): Person {

        logger.info("Finding one Person!")
        val person = Person()
        person.id = counter.incrementAndGet()
        person.firstName = "Miagui"
        person.lastName = "Sartore"
        person.address = "Americana - São Paulo"
        person.gender = "Masculino"

        return person
    }

    fun create(person: Person) = person

    fun update(person: Person) = person

    fun delete(id: Long) {}

    private fun mockPerson(i: Int): Person {

        val person = Person()
        person.id = counter.incrementAndGet()
        person.firstName = "First Name $i"
        person.lastName = "Last Name $i"
        person.address = "Address $i"
        person.gender = "Gender Type $i"

        return person
    }

}