package com.example.springtutorial.mappers;

import com.example.springtutorial.entities.Person;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    Person toDTO(Person person);
    Person toEntity(Person person);
}
