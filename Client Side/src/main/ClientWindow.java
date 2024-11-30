package main;

import com.mysql.cj.protocol.Message;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Vector;

public class ClientWindow extends JFrame {
    private JPanel panelTop, panelLeft, panelRight, panelBottom;
    private JList<Car> voitureList;
    private DefaultListModel<Car> listModel;
    private JTextField name, car;
    private JFormattedTextField date_debut, date_fin;
    private JButton addButton, saveButton;

    public ClientWindow() throws ParseException {
        String username = LoginWindow.getLoginField().getText();
        setTitle("Agence de location EnsaCo");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(new BorderLayout());
        setResizable(false);

        panelTop = new JPanel();
        panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));
        panelTop.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));

        panelBottom = new JPanel();
        panelBottom.setLayout(new BoxLayout(panelBottom, BoxLayout.X_AXIS));
        panelBottom.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));

        panelLeft = new JPanel();
        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
        panelLeft.setBorder(BorderFactory.createEmptyBorder(20, 60, 50, 50));

        panelRight = new JPanel();
        panelRight.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding between components
        panelRight.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        JLabel userInfoLabel = new JLabel("Bonjour Mr/Mlle/Madame : " + username);
        JLabel countLabel = new JLabel("Choisissez votre commande. Vous serez déconnecté automatiquement après 5 minutes d'inactivité");

        // Center the labels
        userInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTop.add(userInfoLabel);
        panelTop.add(countLabel);

        // Create header for JList
        JPanel headerPanel = new JPanel(new GridLayout(1, 2)); // Adjust columns as needed
        JLabel carHeader = new JLabel("Voiture");
        JLabel priceHeader = new JLabel("Prix/Jour");

        // Center headers
        carHeader.setHorizontalAlignment(SwingConstants.CENTER);
        priceHeader.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(carHeader);
        headerPanel.add(priceHeader);

        // Create JList for cars
        listModel = new DefaultListModel<>();
        voitureList = new JList<>(listModel);

        voitureList.setCellRenderer(new CarCellRenderer());

        // Create JScrollPane and add JList to it
        JScrollPane listScroller = new JScrollPane(voitureList);

        // Set preferred size for JScrollPane
        listScroller.setPreferredSize(new Dimension(250, 150)); // Adjust as needed

        voitureList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add components to panelLeft
        panelLeft.add(headerPanel); // Add header first
        panelLeft.add(listScroller); // Then add the JScrollPane

        JLabel nameLabel = new JLabel("Nom et Prénom");
        name = new JTextField(12);
        name.setText(username);
        name.setEditable(false);
        JLabel carLabel = new JLabel("Voiture");
        car = new JTextField(12);
        car.setEditable(false);

        MaskFormatter dateFormatter = new MaskFormatter("##/##/####") {
            @Override
            protected void invalidEdit() {
                // Do nothing to suppress the beep sound
            }
        };
        dateFormatter.setPlaceholderCharacter(' ');

        JLabel date_debutLabel = new JLabel("Date de début");
        date_debut = new JFormattedTextField(dateFormatter);
        date_debut.setColumns(12);
        date_debut.setText("dd-mm-yyyy");
        date_debut.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                date_debut.setCaretPosition(0);
            }
        });

        JLabel date_finLabel = new JLabel("Date de fin");
        date_fin = new JFormattedTextField(dateFormatter);
        date_fin.setColumns(12);
        date_fin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                date_fin.setCaretPosition(0);
            }
        });

        // Add components to panelRight using GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        panelRight.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panelRight.add(name, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panelRight.add(carLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panelRight.add(car, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        panelRight.add(date_debutLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panelRight.add(date_debut, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        panelRight.add(date_finLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panelRight.add(date_fin, gbc);

        addButton = new JButton("Ajouter");
        saveButton = new JButton("Enregistrer");
        JButton disconnectButton = new JButton("Déconnecter");

        addButton.addActionListener(e -> {
            int selectedRow = voitureList.getSelectedIndex();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une voiture");
                return;
            }

            Car selectedCar = listModel.getElementAt(selectedRow);
            car.setText(selectedCar.voiture());

        });

        saveButton.addActionListener(e -> {
            int selectedRow = voitureList.getSelectedIndex();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une voiture");
                return;
            }

            String dateDebut = date_debut.getText().trim();
            String dateFin = date_fin.getText().trim();
            String carText = car.getText().trim();

            if (dateDebut.isEmpty() || dateFin.isEmpty() || carText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs");
                return;
            }

            Car selectedCar = listModel.getElementAt(selectedRow);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Command command = new Command(
                        1, // Replace with actual command ID
                        new java.sql.Date(System.currentTimeMillis()),
                        selectedCar.Id_voiture(),
                        1, // Replace with actual client ID
                        new java.sql.Date(dateFormat.parse(dateDebut).getTime()),
                        new java.sql.Date(dateFormat.parse(dateFin).getTime())
                );
                saveCommand(command);
                JOptionPane.showMessageDialog(this, "Commande enregistrée avec succès!", "Information", JOptionPane.INFORMATION_MESSAGE);
            } catch (ParseException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de la conversion des dates.");
            }
        });

        disconnectButton.addActionListener(e -> {
            dispose(); // Close the ClientWindow
            new LoginWindow(); // Open the LoginWindow
        });

        // Center the buttons and add space between them
        panelBottom.add(Box.createHorizontalGlue());
        panelBottom.add(disconnectButton);
        panelBottom.add(Box.createRigidArea(new Dimension(20, 0))); // Add space between buttons
        panelBottom.add(addButton);
        panelBottom.add(Box.createRigidArea(new Dimension(20, 0))); // Add space between buttons
        panelBottom.add(saveButton);
        panelBottom.add(Box.createHorizontalGlue());

        loadDataFromDatabase();

        this.add(panelTop, BorderLayout.NORTH);
        this.add(panelLeft, BorderLayout.CENTER);
        this.add(panelRight, BorderLayout.EAST);
        this.add(panelBottom, BorderLayout.SOUTH);
    }

    public User getUserByLogin(String login) {
        String sql = "SELECT * FROM clients WHERE login = ?";
        try (Connection conn = MyConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("Id_client"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("photo"),
                        rs.getBoolean("celibataire"),
                        rs.getString("sexe")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if user is not found
    }

    private void loadDataFromDatabase() {
        try (Connection conn = MyConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM voitures WHERE Nbre_voiture > Nbre_voiture_louer")) {

            while (rs.next()) {
                int Id_voiture = rs.getInt("Id_voiture");
                String voiture = rs.getString("voiture");
                int Nbre_voiture = rs.getInt("Nbre_voiture");
                int Nbre_voiture_loue = rs.getInt("Nbre_voiture_louer");
                String type = rs.getString("type");
                int prix_jour = rs.getInt("prix_jour");
                listModel.addElement(new Car(Id_voiture, voiture, Nbre_voiture, Nbre_voiture_loue, type, prix_jour));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCarRentalCount(int carId) {
        String sql = "UPDATE voitures SET Nbre_voiture_louer = Nbre_voiture_louer + 1 WHERE Id_voiture = ?";
        try (Connection conn = MyConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, carId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveCommand(Command command) {
        String sqlCheck = "SELECT COUNT(*) FROM voitures WHERE Id_voiture = ?";
        String sqlInsert = "INSERT INTO commandes (Date_commande, Id_voiture, Id_client, Date_debut, Date_fin) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = MyConn.getConnection();
             PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck);
             PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {

            pstmtCheck.setInt(1, command.idVoiture());
            ResultSet rs = pstmtCheck.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                pstmtInsert.setDate(1, command.dateCommande());
                pstmtInsert.setInt(2, command.idVoiture());
                pstmtInsert.setInt(3, command.idClient());
                pstmtInsert.setDate(4, command.dateDebut());
                pstmtInsert.setDate(5, command.dateFin());

                pstmtInsert.executeUpdate();
                System.out.println("Command saved successfully!");

                // Update the rental count
                updateCarRentalCount(command.idVoiture());

                // Create the file with the required information
                Car selectedCar = listModel.getElementAt(voitureList.getSelectedIndex());
                createInvoiceFile(command, getUserByLogin(LoginWindow.getLoginField().getText()), selectedCar);

            } else {
                System.out.println("Error: Id_voiture does not exist.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error saving command.");
        }
    }

    private void createInvoiceFile(Command command, User user, Car voitures) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate currentDate = LocalDate.now();
        String fileName = "facture_" + user.nom() + "_" + currentDate.format(formatter) + ".txt";
        long dureeLocation = ChronoUnit.DAYS.between(command.dateDebut().toLocalDate(), command.dateFin().toLocalDate());
        int prixTotal = (int) dureeLocation * voitures.prix_jour();
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Facture                                 " + currentDate.format(formatter) + "\n");
            writer.write("Mr /Mlle/Madame : " + user.nom() +"\n");
            writer.write("Voiture :"+ voitures.voiture() +"\n");
            writer.write("Type : "+ voitures.type() + "\n");
            writer.write("Date début : " + command.dateDebut().toString() + "\n");
            writer.write("Date fin : " + command.dateFin().toString() + "\n");
            writer.write("Durée de location en (Jours) : " + dureeLocation + "\n");
            writer.write("Prix total à payer : " + prixTotal + " DH\n");
            System.out.println("Invoice file created successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error creating invoice file.");
        }
    }
}

class CarCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        Car car = (Car) value;

        String formattedText = String.format("%-50s $%d", car.voiture(), car.prix_jour());
        label.setText(formattedText);

        return label;
    }
}