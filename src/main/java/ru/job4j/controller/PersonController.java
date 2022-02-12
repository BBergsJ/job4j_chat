package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Person;
import ru.job4j.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class.getSimpleName());
    private final PersonService personService;
    private BCryptPasswordEncoder encoder;
    private final ObjectMapper objectMapper;

    public PersonController(PersonService personService,
                            BCryptPasswordEncoder encoder,
                            ObjectMapper objectMapper) {
        this.personService = personService;
        this.encoder = encoder;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public List<Person> findAll() {
        return personService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        if (id == 0) {
            throw new IllegalArgumentException("Id mustn't be 0");
        }
        return ResponseEntity.of(Optional.of(
                personService.findById(id).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found"))
        ));
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody Map<String, String> body) {
        var name = body.get("name");
        var password = body.get("password");
        if (name == null || password == null) {
            throw new NullPointerException("Username and password mustn't be empty");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password length must be more than 5 characters.");
        }
        var person = new Person();
        person.setName(name);
        person.setPassword(encoder.encode(person.getPassword()));
        var entity = new ResponseEntity(
                person,
                HttpStatus.CREATED
        );
        return entity;
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Map<String, String> body) {
        var name = body.get("name");
        var password = body.get("password");
        if (name == null || password == null) {
            throw new NullPointerException("Username and password mustn't be empty");
        }
        var person = new Person();
        person.setName(name);
        person.setPassword(password);
        personService.save(person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (id == 0) {
            throw  new NullPointerException("Id mustn't be 0");
        }
        Person person = new Person();
        person.setId(id);
        return new ResponseEntity<>(
                personService.delete(id) ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PatchMapping("/")
    public void edit(@RequestBody Person dto) {
        Optional<Person> person = personService.findById(dto.getId());
        if (person.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");
        }
        personService.save(Person.of(
                dto.getId(),
                dto.getName(),
                dto.getPassword(),
                dto.getRole()
                ));
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public void exceptionHandler(Exception e,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {{
            put("message", e.getMessage());
            put("type", e.getClass());
        }}));
        LOGGER.error(e.getLocalizedMessage());
    }
}
