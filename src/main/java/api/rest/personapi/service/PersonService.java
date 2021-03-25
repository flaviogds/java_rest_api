package api.rest.personapi.service;

import api.rest.personapi.dto.MessageResponseDTO;
import api.rest.personapi.entity.Person;
import api.rest.personapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
public class PersonService {

    private PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    @PostMapping
    public MessageResponseDTO createPerson(Person person){
        Person saved = personRepository.save(person);
        return MessageResponseDTO
                .builder()
                .message("Created Person With Id " + saved.getId())
                .build();
    }
}
