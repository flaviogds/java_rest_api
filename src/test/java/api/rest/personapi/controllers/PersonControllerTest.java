package api.rest.personapi.controllers;

import java.util.Collections;
import java.util.List;

import api.rest.personapi.controller.PersonController;
import api.rest.personapi.dto.request.PersonDTO;
import api.rest.personapi.dto.response.MessageResponseDTO;
import api.rest.personapi.exception.PersonNotFoundException;
import api.rest.personapi.service.PersonService;
import static api.rest.personapi.utils.PersonUtils.createFakeDTO;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {

    private static final String PEOPLE_API_URL_PATH = "/api/v1/people";

    private MockMvc mockMvc;

    private PersonController personController;

    @Mock
    private PersonService personService;

    @BeforeEach
    void setUp() {
        personController = new PersonController(personService);
        mockMvc = MockMvcBuilders.standaloneSetup(personController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void testWhenPOSTIsCalledThenAPersonShouldBeCreated() throws Exception {
        PersonDTO expectedPersonDTO = createFakeDTO();
        MessageResponseDTO expectedResponseMessage = createMessageResponse(1L, "Created person with Id ");

        when(personService.createPerson(expectedPersonDTO)).thenReturn(expectedResponseMessage);

        mockMvc.perform(post(PEOPLE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedPersonDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is(expectedResponseMessage.getMessage())));
    }

    @Test
    void testWhenGETWithValidIsCalledThenAPersonShouldBeReturned() throws Exception {
        var expectedValidId = 1L;
        PersonDTO expectedPersonDTO = createFakeDTO();
        expectedPersonDTO.setId(expectedValidId);

        when(personService.findById(expectedValidId)).thenReturn(expectedPersonDTO);

        mockMvc.perform(get(PEOPLE_API_URL_PATH + "/" + expectedValidId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("João")))
                .andExpect(jsonPath("$.lastName", is("da Silva")));
    }

    @Test
    void testWhenGETWithInvalidIsCalledThenAnErrorMessagenShouldBeReturned() throws Exception {
        var expectedValidId = 1L;
        PersonDTO expectedPersonDTO = createFakeDTO();
        expectedPersonDTO.setId(expectedValidId);

        when(personService.findById(expectedValidId)).thenThrow(PersonNotFoundException.class);

        mockMvc.perform(get(PEOPLE_API_URL_PATH + "/" + expectedValidId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testWhenGETIsCalledThenPeopleListShouldBeReturned() throws Exception {
        var expectedValidId = 1L;
        PersonDTO expectedPersonDTO = createFakeDTO();
        expectedPersonDTO.setId(expectedValidId);
        List<PersonDTO> expectedPeopleDTOList = Collections.singletonList(expectedPersonDTO);

        when(personService.listAll()).thenReturn(expectedPeopleDTOList);

        mockMvc.perform(get(PEOPLE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("João")))
                .andExpect(jsonPath("$[0].lastName", is("da Silva")));
    }

    @Test
    void testWhenPUTIsCalledThenAPersonShouldBeUpdated() throws Exception {
        var expectedValidId = 1L;
        PersonDTO expectedPersonDTO = createFakeDTO();
        MessageResponseDTO expectedResponseMessage = createMessageResponse(1L, "Updated person with Id ");

        when(personService.updateById(expectedValidId, expectedPersonDTO)).thenReturn(expectedResponseMessage);

        mockMvc.perform(put(PEOPLE_API_URL_PATH + "/" + expectedValidId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedPersonDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(expectedResponseMessage.getMessage())));
    }

    @Test
    void testWhenDELETEIsCalledThenAPersonShouldBeDeleted() throws Exception {
        var expectedValidId = 1L;

        mockMvc.perform(delete(PEOPLE_API_URL_PATH + "/" + expectedValidId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private MessageResponseDTO createMessageResponse(Long id, String message) {
        return MessageResponseDTO.builder()
                .message(message + id)
                .build();
    }

    public static String asJsonString(PersonDTO personDTO) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.registerModules(new JavaTimeModule());

            return objectMapper.writeValueAsString(personDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}