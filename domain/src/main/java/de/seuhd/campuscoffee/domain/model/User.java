package de.seuhd.campuscoffee.domain.model;

import lombok.Builder;
import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder(toBuilder = true)
public record User(
        @Nullable Long id,
        @Nullable LocalDateTime createdAt,
        @Nullable LocalDateTime updatedAt,
        String loginName,
        String emailAddress,
        String firstName,
        String lastName
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // Statische Null-Checks wie in der Aufgabe gefordert
    public User {
        Objects.requireNonNull(loginName, "loginName darf nicht leer sein");
        Objects.requireNonNull(emailAddress, "emailAddress darf nicht leer sein");
        Objects.requireNonNull(firstName, "firstName darf nicht leer sein");
        Objects.requireNonNull(lastName, "lastName darf nicht leer sein");
    }
}