package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.domain.Message;
import ru.job4j.repository.MessageRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> findAll() {
        return StreamSupport
                .stream(messageRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<Message> findById(int id) {
        return messageRepository.findById(id);
    }

    public Message create(Message message) {
        return messageRepository.save(message);
    }

    public boolean delete(int id) {
        boolean rsl = false;
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            rsl = true;
        }
        return rsl;
    }
}
