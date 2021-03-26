package api.rest.personapi.utils;

import api.rest.personapi.dto.request.PersonDTO;
import api.rest.personapi.entity.Person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

public class PersonUtils {

    private static final String FIRST_NAME = "João";
    private static final String LAST_NAME = "da Silva";
    private static final String CPF_NUMBER = "111.222.333-45";
    private static final long PERSON_ID = 1L;
    public static final LocalDate BIRTH_DATE = LocalDate.of(1990, 1, 1);

    public static PersonDTO createFakeDTO() {
        return PersonDTO.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .cpf(CPF_NUMBER)
                .birthDate(BIRTH_DATE.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .phones(Collections.singletonList(PhoneUtils.createFakeDTO()))
                .build();
    }

    public static Person createFakeEntity() {
        return Person.builder()
                .id(PERSON_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .cpf(CPF_NUMBER)
                .birthDate(BIRTH_DATE)
                .phones(Collections.singletonList(PhoneUtils.createFakeEntity()))
                .build();
    }
}