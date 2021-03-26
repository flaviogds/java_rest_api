package api.rest.personapi.service;

import api.rest.personapi.dto.request.PersonDTO;
import api.rest.personapi.dto.response.MessageResponseDTO;
import api.rest.personapi.entity.Person;
import api.rest.personapi.exception.PersonNotFoundException;
import api.rest.personapi.mapper.PersonMapper;
import api.rest.personapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private PersonRepository personRepository;

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    @Autowired
    public PersonService(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    public MessageResponseDTO createPerson(PersonDTO personDTO){
        Person personToSave = personMapper.toModel(personDTO);

        Person saved = personRepository.save(personToSave);
        return MessageResponseDTO
                .builder()
                .message("Created Person With Id " + saved.getId())
                .build();
    }

    public List<PersonDTO> listAll() {
        List<Person> allPeople = personRepository.findAll();
        return allPeople.stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findById(Long id) throws PersonNotFoundException {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));

        return personMapper.toDTO(person);
    }
}
