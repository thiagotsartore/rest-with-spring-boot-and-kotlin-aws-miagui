package br.com.miagui.services

import br.com.miagui.controllers.PersonController
import br.com.miagui.data.vo.v1.PersonVO
import br.com.miagui.exceptions.RequiredObjectsIsNullException
import br.com.miagui.exceptions.ResourceNotFoundException
import br.com.miagui.mapper.DozerMapper
import br.com.miagui.model.Person
import br.com.miagui.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong
import java.util.logging.Logger

@Service
class PersonService {

    @Autowired
    private lateinit var repository: PersonRepository

    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAll(): List<PersonVO>  {
        logger.info("Finding all Persons!")
//*       dados mockados
//        val persons: MutableList<Person> = ArrayList()
//
//        for (i in 1..7) {
//            val person = mockPerson(i)
//            persons.add(person)
//        }
        val persons = repository.findAll()
        val vos =  DozerMapper.parseListObjects(persons, PersonVO::class.java)
        for (person in vos){
            val withSelfRel = linkTo(PersonController::class.java).slash(person.key).withSelfRel()
            person.add(withSelfRel)
        }
        return vos
    }

    fun findById(id: Long): PersonVO {
        logger.info("Finding one Person with ID $id!")
        var person =  repository.findById(id)
            .orElseThrow{ResourceNotFoundException("No records found for this ID!")}
        val personVO: PersonVO = DozerMapper.parseObject(person, PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    fun create(person: PersonVO?) : PersonVO {
        if (person == null) throw RequiredObjectsIsNullException()
        logger.info("Creating person with name ${person.firstName}.")
        var entity: Person = DozerMapper.parseObject(person, Person::class.java)
        val personVO: PersonVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    fun update(person: PersonVO?) : PersonVO {
        if (person == null) throw RequiredObjectsIsNullException()
        logger.info("Updating person with ID ${person.key}!")
        val entity = repository.findById(person.key)
            .orElseThrow{ResourceNotFoundException("No records found for this ID!")}
        entity.firstName = person.firstName
        entity.lastName  = person.lastName
        entity.address   = person.address
        entity.gender    = person.gender
        val personVO: PersonVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    fun delete(id: Long) {
        logger.info("Deleting person with ID $id!")
        val entity = repository.findById(id)
                .orElseThrow{ResourceNotFoundException("No records found for this ID!")}
        repository.delete(entity)
    }

    private fun mockPerson(i: Int): PersonVO {
        val person       = PersonVO()
        val counter      = AtomicLong()
        person.key        = counter.incrementAndGet()
        person.firstName = "First Name $i"
        person.lastName  = "Last Name $i"
        person.address   = "Address $i"
        person.gender    = "Gender Type $i"
        return person
    }

}