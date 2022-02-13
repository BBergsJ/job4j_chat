package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.handlers.Operation;
import ru.job4j.service.MessageService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/")
    public List<Message> findAll() {
        return messageService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable int id) {
        Optional<Message> message = messageService.findById(id);
        if (message.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found");
        }
        return new ResponseEntity<Message>(message.get(), HttpStatus.OK);
    }

    @PostMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Message> create(@Valid @RequestBody Message message) {
        if (message.getText() == null || message.getAuthor() == null) {
            throw new NullPointerException("Autor of the message or text is empty");
        }
        return new ResponseEntity<Message>(
                messageService.create(message),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@Valid @RequestBody Message message) {
        if (message.getText() == null || message.getAuthor() == null) {
            throw new NullPointerException("Autor of the message or text is empty");
        }
        messageService.create(message);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Message message = new Message();
        message.setId(id);
        return new ResponseEntity<>(
                messageService.delete(id) ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PatchMapping("/")
    public void edit(@Valid @RequestBody Message dto) {
        Optional<Message> message = messageService.findById(dto.getId());
        if (message.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not foud");
        }
        messageService.save(Message.of(
                dto.getId(),
                dto.getText(),
                dto.getRoom(),
                dto.getAuthor()
        ));
    }
}
