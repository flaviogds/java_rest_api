package api.rest.personapi.mapper;

import api.rest.personapi.dto.request.PersonDTO;
import api.rest.personapi.entity.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static api.rest.personapi.utils.PersonUtils.createFakeDTO;
import static api.rest.personapi.utils.PersonUtils.createFakeEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonMapperTest {

    @Mock
    private PersonMapper personMapper;

    @Test
    void testGivenPersonDTOThenReturnPerson(){
        PersonDTO personDTO = createFakeDTO();
        Person expectedPerson = createFakeEntity();

        when(personMapper.toModel(personDTO)).thenReturn(expectedPerson);

        Person mappingPerson = personMapper.toModel(personDTO);

        assertEquals(expectedPerson, mappingPerson);
    }

    @Test
    void testGivenPersonThenReturnPersonDTO(){
        PersonDTO expectedPersonDTO = createFakeDTO();
        Person person = createFakeEntity();

        when(personMapper.toDTO(person)).thenReturn(expectedPersonDTO);

        PersonDTO mappingPersonDTO = personMapper.toDTO(person);

        assertEquals(expectedPersonDTO, mappingPersonDTO);
    }
}
