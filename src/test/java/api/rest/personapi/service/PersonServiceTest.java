package api.rest.personapi.service;

import api.rest.personapi.dto.request.PersonDTO;
import api.rest.personapi.dto.response.MessageResponseDTO;
import api.rest.personapi.entity.Person;
import api.rest.personapi.exception.PersonNotFoundException;
import api.rest.personapi.repository.PersonRepository;

import static api.rest.personapi.utils.PersonUtils.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @Test //Post method
    void testGivenPersonDTOThenReturnSavedMessage(){
        PersonDTO personDTO = createFakeDTO();
        Person expectedSavedPerson = createFakeEntity();

        when(personRepository.save(any(Person.class))).thenReturn(expectedSavedPerson);

        MessageResponseDTO expectedSuccessMessage = createdExpectedMessageResponse(expectedSavedPerson.getId(), "Created person with Id ");
        MessageResponseDTO successMessage = personService.createPerson((personDTO));

        assertEquals(expectedSuccessMessage, successMessage);
    }

    @Test //Get method with list return
    void testGivenNoDataThenReturnAllPerson(){
        List<Person> expectedDataPerson = Collections.singletonList(createFakeEntity());
        PersonDTO personDTO = createFakeDTO();

        when(personRepository.findAll()).thenReturn(expectedDataPerson);

        List<PersonDTO> expectedDataPersonDTO = personService.listAll();

        assertFalse(expectedDataPersonDTO.isEmpty());
        assertEquals(expectedDataPerson.get(0).getId(), expectedDataPersonDTO.get(0).getId());
    }
    
    @Test //Get with ID method
    void testGivenValidPersonIdThenReturnThisPerson() throws PersonNotFoundException {
        PersonDTO expectedPersonDTO = createFakeDTO();
        Person expectedPerson = createFakeEntity();
        expectedPersonDTO.setId(expectedPerson.getId());

        when(personRepository.findById(expectedPerson.getId())).thenReturn(Optional.of(expectedPerson));

        PersonDTO personDTO = personService.findById(expectedPerson.getId());

        assertEquals(expectedPersonDTO, personDTO);
    }

    @Test //Get with ID method but whit wrong ID
    void testGivenInvalidPersonIdThenReturnThrowException(){
        var invalidID = 1L;

        when(personRepository.findById(invalidID)).thenReturn(Optional.ofNullable(any(Person.class)));

        assertThrows(PersonNotFoundException.class, () -> personService.findById(invalidID));
    }

    @Test //Put method with ID
    void testGivenValidPersonIdThenReturnSuccessOnUpdateInfo() throws PersonNotFoundException {
        var updateID = 2L;

        PersonDTO personDTOToUpdate = createFakeDTO();
        Person expectedPersonToUpdate = createFakeEntity();
        Person expectedPersonUpdated =createFakeEntity();

        personDTOToUpdate.setId(updateID);
        personDTOToUpdate.setFirstName("Update João");

        expectedPersonToUpdate.setId(updateID);

        expectedPersonUpdated.setId(updateID);
        expectedPersonUpdated.setFirstName(personDTOToUpdate.getFirstName());

        when(personRepository.findById(updateID)).thenReturn(Optional.of(expectedPersonUpdated));
        when(personRepository.save(any(Person.class))).thenReturn(expectedPersonUpdated);

        MessageResponseDTO expectedSuccessMessage = createdExpectedMessageResponse(updateID, "Updated person with Id ");
        MessageResponseDTO successMessage = personService.updateById(updateID, personDTOToUpdate);

        assertEquals(expectedSuccessMessage, successMessage);
    }

    @Test //Put method with ID but wrong ID
    void testGivenInvalidPersonIdThenReturnThrowExceptionUpdateInfo(){
        var invalidID = 1L;

        PersonDTO updatePersonDTO = createFakeDTO();

        updatePersonDTO.setId(invalidID);
        updatePersonDTO.setFirstName("Update João");

        when(personRepository.findById(invalidID)).thenReturn(Optional.ofNullable(any(Person.class)));

        assertThrows(PersonNotFoundException.class, () -> personService.updateById(invalidID, updatePersonDTO));
    }

    @Test //Delete method with ID
    void testGivenValidPersonIdThenReturnSuccessOnDelete() throws PersonNotFoundException {
        var deleteId = 1L;

        Person expectedPersonToDelete = createFakeEntity();

        when(personRepository.findById(deleteId)).thenReturn(Optional.of(expectedPersonToDelete));

        personService.deleteById(deleteId);

        verify(personRepository, times(1)).deleteById(deleteId);
    }

    @Test //Delete method with ID but wrong ID
    void testGivenInvalidPersonIdThenReturnThrowExceptionOnDelete(){
        var invalidID = 2L;

        Person expectedPersonToDelete = createFakeEntity();

        when(personRepository.findById(invalidID)).thenReturn(Optional.ofNullable(any(Person.class)));

        assertThrows(PersonNotFoundException.class, () -> personService.deleteById(invalidID));
    }

    private MessageResponseDTO createdExpectedMessageResponse(Long id, String message) {
        return MessageResponseDTO
                .builder()
                .message( message + id)
                .build();
    }
}
