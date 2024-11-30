package main;

import java.io.Serial;
import java.io.Serializable;

public record User(int Id_client, String nom, String prenom, String login, String password, String role, String photo, boolean celibataire, String sexe)
        implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}

