package ru.job4j.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Person;
import ru.job4j.domain.Role;
import ru.job4j.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return StreamSupport
                .stream(this.personRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }

    public boolean delete(int id) {
        boolean rsl = false;
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            rsl = true;
        }
        return rsl;
    }

    public Person findByUsername(String username) {
        return personRepository.findByName(username);
    }
}
