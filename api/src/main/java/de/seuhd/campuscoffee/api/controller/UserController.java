package de.seuhd.campuscoffee.api.controller;

import de.seuhd.campuscoffee.api.dtos.UserDto;
import de.seuhd.campuscoffee.api.mapper.UserDtoMapper;
import de.seuhd.campuscoffee.domain.model.User;
import de.seuhd.campuscoffee.domain.ports.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController // Wichtig: @RestController statt @Controller für REST-APIs
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserDtoMapper userMapper;

    // Abrufen aller Benutzer (GET /api/users)
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDto> userDtos = users.stream().map(userMapper::fromDomain).toList();
        return ResponseEntity.ok(userDtos);
    }

    // Abrufen eines Benutzers nach ID (GET /api/users/{id})
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.fromDomain(user));
    }

    // Abrufen eines Benutzers nach Anmeldename (GET /api/users/filter?loginName=...)
    @GetMapping("/filter")
    public ResponseEntity<UserDto> getUserByLoginName(@RequestParam String loginName) {
        User user = userService.getUserByLoginName(loginName);
        return ResponseEntity.ok(userMapper.fromDomain(user));
    }

    // Erstellen eines neuen Benutzers (POST /api/users)
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        User userToCreate = userMapper.toDomain(userDto);
        User createdUser = userService.createUser(userToCreate);
        URI location = URI.create("/api/users/" + createdUser.id());
        return ResponseEntity.created(location).body(userMapper.fromDomain(createdUser));
    }

    // Aktualisieren eines bestehenden Benutzers (PUT /api/users/{id})
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        // Sicherstellen, dass die ID im Pfad mit der ID im Body übereinstimmt
        if (!id.equals(userDto.id())) {
            throw new IllegalArgumentException("ID in path and body do not match");
        }
        User userToUpdate = userMapper.toDomain(userDto);
        User updatedUser = userService.updateUser(userToUpdate);
        return ResponseEntity.ok(userMapper.fromDomain(updatedUser));
    }

    // Löschen eines Benutzers nach ID (DELETE /api/users/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}