package br.com.miagui.services

import br.com.miagui.data.vo.v1.PersonVO
import br.com.miagui.data.vo.v2.PersonVO as PersonVOV2
import br.com.miagui.exceptions.ResourceNotFoundException
import br.com.miagui.mapper.DozerMapper
import br.com.miagui.mapper.custom.PersonMapper
import br.com.miagui.model.Person
import br.com.miagui.repository.PersonRepository
import com.github.dozermapper.core.Mapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong
import java.util.logging.Logger

@Service
class PersonService {

    @Autowired
    private lateinit var repository: PersonRepository

    @Autowired
    private lateinit var mapper: PersonMapper

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
        return DozerMapper.parseListObjects(persons, PersonVO::class.java)
    }

    fun findById(id: Long): PersonVO {
        logger.info("Finding one Person!")
        var person =  repository.findById(id)
            .orElseThrow{ResourceNotFoundException("No records found for this ID!")}
        return DozerMapper.parseObject(person, PersonVO::class.java)
    }

    fun create(person: PersonVO) : PersonVO {
        logger.info("Creating person with name ${person.firstName}.")
        var entity: Person = DozerMapper.parseObject(person, Person::class.java)
        return DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
    }

    fun createV2(person: PersonVOV2) : PersonVOV2 {
        logger.info("Creating person with name ${person.firstName}.")
        var entity: Person = mapper.mapVOToEntity(person)
        return mapper.mapEntityToVO(repository.save(entity))
    }

    fun update(person: PersonVO) : PersonVO {
        logger.info("Updating person with ID ${person.id}!")
        val entity = repository.findById(person.id)
            .orElseThrow{ResourceNotFoundException("No records found for this ID!")}
        entity.firstName = person.firstName
        entity.lastName  = person.lastName
        entity.address   = person.address
        entity.gender    = person.gender
        return DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
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
        person.id        = counter.incrementAndGet()
        person.firstName = "First Name $i"
        person.lastName  = "Last Name $i"
        person.address   = "Address $i"
        person.gender    = "Gender Type $i"
        return person
    }

}