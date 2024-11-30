CREATE DATABASE car_rental;

USE car_rental;

-- Table for clients
CREATE TABLE clients (
    Id_client INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    role ENUM('admin', 'user') NOT NULL,
    photo VARCHAR(255),
    celibataire BOOLEAN,
    sexe ENUM('M', 'F')
);

-- Table for voitures
CREATE TABLE voitures (
    Id_voiture INT AUTO_INCREMENT PRIMARY KEY,
    voiture VARCHAR(50) NOT NULL,
    Nbre_voiture INT NOT NULL,
    Nbre_voiture_louer INT NOT NULL DEFAULT 0,
    type VARCHAR(50),
    prix_jour DECIMAL(10, 2)
);

-- Table for commandes
CREATE TABLE commandes (
    Id_commande INT AUTO_INCREMENT PRIMARY KEY,
    Date_commande DATE NOT NULL,
    Id_voiture INT,
    Id_client INT,
    Date_debut DATE NOT NULL,
    Date_fin DATE NOT NULL,
    FOREIGN KEY (Id_voiture) REFERENCES voitures(Id_voiture),
    FOREIGN KEY (Id_client) REFERENCES clients(Id_client)
);

/*Insert Values*/
-- Sample clients
INSERT INTO clients (nom, prenom, login, password, role, photo, celibataire, sexe)
VALUES 
('Admin', 'User', 'admin', 'admin123', 'admin', NULL, TRUE, 'M'),
('John', 'Doe', 'johndoe', 'password', 'user', NULL, FALSE, 'M');

-- Sample voitures
INSERT INTO voitures (voiture, Nbre_voiture, Nbre_voiture_louer, type, prix_jour)
VALUES 
('Dacia Logan', 6, 5, 'Diesel', 300),
('Hyundai i10', 4, 2, 'Essence', 280),
('Volkswagen Polo', 3, 1, 'Diesel', 450),
('Peugeot 307', 5, 4, 'Diesel', 550),
('Mercedes 220', 2, 0, 'Diesel', 700),
('Range Rover', 1, 0, 'Diesel', 1000);
