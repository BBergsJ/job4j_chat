package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Room;
import ru.job4j.service.RoomService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public List<Room> findAll() {
        return roomService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable int id) {
        Optional<Room> room = roomService.findById(id);
        if (room.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found");
        }
        return new ResponseEntity<Room>(room.get(), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Room> create(@RequestBody Room room) {
        if (room == null) {
            throw new NullPointerException("Room is empty");
        }
        return new ResponseEntity<Room>(
                roomService.save(room),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Room room) {
        if (room == null) {
            throw new NullPointerException("Room is empty");
        }
        roomService.save(room);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Room room = new Room();
        room.setId(id);
        return new ResponseEntity<>(
                roomService.delete(id) ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PatchMapping("/")
    public void edit(@RequestBody Room dto) {
        Optional<Room> room = roomService.findById(dto.getId());
        if (room.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Roo not found");
        }
        roomService.save(Room.of(dto.getId()));
    }
}