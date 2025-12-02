package de.seuhd.campuscoffee.domain.impl;

import de.seuhd.campuscoffee.domain.model.User;
import de.seuhd.campuscoffee.domain.ports.UserDataService;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

import de.seuhd.campuscoffee.domain.ports.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    // TODO: Implement user service
    private final UserDataService userDataService;

    @Override
    public List<User> getAllUsers() {
        return userDataService.getAll();
    }

    @Override
    public User getUserById(Long id) {
        return userDataService.getById(id);
    }

    @Override
    public User getUserByLoginName(String loginName) {
        return userDataService.getByLoginName(loginName);
    }

    @Override
    public User createUser(User user) {
        // Für's Erstellen stellen wir sicher, dass die ID null ist
        User userToCreate = user.toBuilder().id(null).build();
        return userDataService.upsert(userToCreate);
    }

    @Override
    public User updateUser(User user) {
        // Stellen sicher, dass der User existiert, bevor wir ihn updaten
        userDataService.getById(user.id());
        return userDataService.upsert(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userDataService.delete(id);
    }
  }
}
