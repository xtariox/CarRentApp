package main;

import java.io.Serial;
import java.io.Serializable;

public record Car(int Id_voiture, String voiture, int Nbre_voiture, int Nbre_voiture_louer, String type, int prix_jour) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}