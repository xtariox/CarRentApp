package main;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;

public record Command(int idCommande, Date dateCommande, int idVoiture, int idClient, Date dateDebut, Date dateFin) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}