package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class AdminWindow extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel carsPanel, clientsPanel;
    private JTable carsTable, clientsTable;
    private DefaultTableModel carsModel, clientsModel;
    private JButton addCarButton, updateCarButton, deleteCarButton;
    private JButton addClientButton, updateClientButton, deleteClientButton, disconnectButton;

    public AdminWindow() {
        setTitle("Admin Panel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        carsPanel = createCarsPanel();
        clientsPanel = createClientsPanel();

        tabbedPane.addTab("Voitures", carsPanel);
        tabbedPane.addTab("Clients", clientsPanel);

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel createCarsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        carsModel = new DefaultTableModel(new String[]{"ID", "Voiture", "Type", "Prix/Jour"}, 0);
        carsTable = new JTable(carsModel);
        loadCarsData();

        JPanel buttonPanel = new JPanel();
        addCarButton = new JButton("Ajouter Voiture");
        updateCarButton = new JButton("Modifier Voiture");
        deleteCarButton = new JButton("Supprimer Voiture");
        disconnectButton = new JButton("Déconnecter");

        buttonPanel.add(addCarButton);
        buttonPanel.add(updateCarButton);
        buttonPanel.add(deleteCarButton);
        buttonPanel.add(disconnectButton);

        addCarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddCar dialog = new AddCar(AdminWindow.this, "", "", 0);
                dialog.setVisible(true);
                if (dialog.isSucceeded()) {
                    String car = dialog.getCar();
                    String type = dialog.getCarType();
                    int price = dialog.getPrice();
                    addCarToDatabase(car, type, price);
                }
            }
        });

        updateCarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = carsTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(AdminWindow.this, "Please select a car to modify", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    // Proceed with the update logic
                    int carId = (int) carsModel.getValueAt(selectedRow, 0);
                    String car = (String) carsModel.getValueAt(selectedRow, 1);
                    String type = (String) carsModel.getValueAt(selectedRow, 2);
                    int price = (int) carsModel.getValueAt(selectedRow, 3);

                    // Show the AddCar dialog with the selected car details
                    AddCar dialog = new AddCar(AdminWindow.this, car, type, price);
                    dialog.setVisible(true);
                    if (dialog.isSucceeded()) {
                        String newCar = dialog.getCar();
                        String newType = dialog.getCarType();
                        int newPrice = dialog.getPrice();
                        updateCarInDatabase(carId, newCar, newType, newPrice);
                    }
                }
            }
        });

        deleteCarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = carsTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(AdminWindow.this, "Please select a car to delete", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    int response = JOptionPane.showConfirmDialog(AdminWindow.this, "Are you sure you want to delete the selected car?", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        int carId = (int) carsModel.getValueAt(selectedRow, 0);
                        deleteCarFromDatabase(carId);
                        carsModel.removeRow(selectedRow);
                    }
                }
            }
        });

        disconnectButton.addActionListener(e -> {
            dispose(); // Close the AdminWindow
            new LoginWindow(); // Open the LoginWindow
        });

        panel.add(new JScrollPane(carsTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }



    private JPanel createClientsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        clientsModel = new DefaultTableModel(new String[]{"ID", "Nom", "Login", "Role"}, 0);
        clientsTable = new JTable(clientsModel);
        loadClientsData();

        JPanel buttonPanel = new JPanel();
        addClientButton = new JButton("Ajouter Client");
        updateClientButton = new JButton("Modifier Client");
        deleteClientButton = new JButton("Supprimer Client");
        disconnectButton = new JButton("Déconnecter");

        buttonPanel.add(addClientButton);
        buttonPanel.add(updateClientButton);
        buttonPanel.add(deleteClientButton);
        buttonPanel.add(disconnectButton);


        addClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddUser dialog = new AddUser(AdminWindow.this, "", "", "", "", "", "", false, "");
                dialog.setVisible(true);
                if (dialog.isSucceeded()) {
                    String name = dialog.getName();
                    String surname = dialog.getSurname();
                    String login = dialog.getLogin();
                    String password = dialog.getPassword();
                    String role = dialog.getRole();
                    String photo = dialog.getPhoto();
                    boolean celibataire = dialog.isCelibataire();
                    String sexe = dialog.getSexe();
                    AddUserToDataBase(name, surname, login, password, role, photo, celibataire, sexe);
                }
            }
        });


        updateClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int clientId = getSelectedClientId();
                if (clientId == -1) return;

                User user = getUserDetails(clientId);
                if (user == null) {
                    JOptionPane.showMessageDialog(AdminWindow.this, "Failed to fetch user details", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                AddUser dialog = new AddUser(
                        AdminWindow.this,
                        user.nom(),
                        user.prenom(),
                        user.login(),
                        user.password(),
                        user.role(),
                        user.photo(),
                        user.celibataire(),
                        user.sexe()
                );
                dialog.setVisible(true);
                if (dialog.isSucceeded()) {
                    updateClientInDatabase(
                            clientId,
                            dialog.getName(),
                            dialog.getSurname(),
                            dialog.getLogin(),
                            dialog.getPassword(),
                            dialog.getRole(),
                            dialog.getPhoto(),
                            dialog.isCelibataire(),
                            dialog.getSexe()
                    );
                }
            }
        });

        deleteClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int clientId = getSelectedClientId();
                if (clientId == -1) return;

                if (confirmDeletion()) {
                    deleteClientFromDatabase(clientId);
                    int selectedRow = clientsTable.getSelectedRow();
                    clientsModel.removeRow(selectedRow);
                }
            }
        });

        disconnectButton.addActionListener(e -> {
            dispose(); // Close the AdminWindow
            new LoginWindow(); // Open the LoginWindow
        });

        panel.add(new JScrollPane(clientsTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }



    private void loadCarsData() {
        try (Connection conn = MyConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM voitures")) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("Id_voiture"));
                row.add(rs.getString("voiture"));
                row.add(rs.getString("type"));
                row.add(rs.getInt("prix_jour"));
                carsModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addCarToDatabase(String car, String type, int price) {
        String checkSql = "SELECT Id_voiture, Nbre_voiture FROM voitures WHERE voiture = ? AND type = ? AND prix_jour = ?";
        String updateSql = "UPDATE voitures SET Nbre_voiture = Nbre_voiture + 1 WHERE Id_voiture = ?";
        String insertSql = "INSERT INTO voitures (voiture, type, prix_jour, Nbre_voiture, Nbre_voiture_louer) VALUES (?, ?, ?, 1, 0)";

        try (Connection conn = MyConn.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

            checkStmt.setString(1, car);
            checkStmt.setString(2, type);
            checkStmt.setInt(3, price);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int carId = rs.getInt("Id_voiture");
                updateStmt.setInt(1, carId);
                updateStmt.executeUpdate();
            } else {
                insertStmt.setString(1, car);
                insertStmt.setString(2, type);
                insertStmt.setInt(3, price);
                insertStmt.executeUpdate();

                ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int newCarId = generatedKeys.getInt(1);
                    Vector<Object> row = new Vector<>();
                    row.add(newCarId);
                    row.add(car);
                    row.add(type);
                    row.add(price);
                    row.add(1); // Nbre_voiture
                    row.add(0); // Nbre_voiture_louer
                    carsModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void updateCarInDatabase(int carId, String car, String type, int price) {
        String updateSql = "UPDATE voitures SET voiture = ?, type = ?, prix_jour = ? WHERE Id_voiture = ?";

        try (Connection conn = MyConn.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            updateStmt.setString(1, car);
            updateStmt.setString(2, type);
            updateStmt.setInt(3, price);
            updateStmt.setInt(4, carId);
            updateStmt.executeUpdate();

            // Update the table model
            for (int i = 0; i < carsModel.getRowCount(); i++) {
                if ((int) carsModel.getValueAt(i, 0) == carId) {
                    carsModel.setValueAt(car, i, 1);
                    carsModel.setValueAt(type, i, 2);
                    carsModel.setValueAt(price, i, 3);
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void deleteCarFromDatabase(int carId) {
        String deleteSql = "DELETE FROM voitures WHERE Id_voiture = ?";

        try (Connection conn = MyConn.getConnection();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {

            deleteStmt.setInt(1, carId);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadClientsData() {
        try (Connection conn = MyConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM clients")) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("Id_client"));
                row.add(rs.getString("nom") + " " + rs.getString("prenom"));
                row.add(rs.getString("login"));
                row.add(rs.getString("role"));
                clientsModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User getUserDetails(int clientId) {
        String query = "SELECT Id_client, nom, prenom, login, password, role, photo, celibataire, sexe FROM clients WHERE Id_client = ?";
        try (Connection conn = MyConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("Id_client"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("photo"),
                        rs.getBoolean("celibataire"),
                        rs.getString("sexe")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void AddUserToDataBase(String name, String surname, String login, String password, String role, String photo, boolean celibataire, String sexe) {
        String checkSql = "SELECT COUNT(*) FROM clients WHERE login = ?";
        String insertSql = "INSERT INTO clients (nom, prenom, login, password, role, photo, celibataire, sexe) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = MyConn.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

            checkStmt.setString(1, login);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Login already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            insertStmt.setString(1, name);
            insertStmt.setString(2, surname);
            insertStmt.setString(3, login);
            insertStmt.setString(4, password);
            insertStmt.setString(5, role);
            if (photo.isEmpty()) {
                insertStmt.setNull(6, Types.VARCHAR);
            } else {
                insertStmt.setString(6, photo);
            }
            insertStmt.setBoolean(7, celibataire);
            insertStmt.setString(8, sexe);
            insertStmt.executeUpdate();

            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int newClientId = generatedKeys.getInt(1);
                Vector<Object> row = new Vector<>();
                row.add(newClientId);
                row.add(name + " " + surname);
                row.add(login);
                row.add(role);
                clientsModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getSelectedClientId() {
        int selectedRow = clientsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a client to modify", "Warning", JOptionPane.WARNING_MESSAGE);
            return -1;
        }
        return (int) clientsModel.getValueAt(selectedRow, 0);
    }

    private boolean confirmDeletion() {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected client?", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return response == JOptionPane.YES_OPTION;
    }

    private void updateClientInDatabase(int clientId, String name, String surname, String login, String password, String role, String photo, boolean celibataire, String sexe) {
        String updateSql = "UPDATE clients SET nom = ?, prenom = ?, login = ?, password = ?, role = ?, photo = ?, celibataire = ?, sexe = ? WHERE Id_client = ?";

        try (Connection conn = MyConn.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            updateStmt.setString(1, name);
            updateStmt.setString(2, surname);
            updateStmt.setString(3, login);
            updateStmt.setString(4, password);
            updateStmt.setString(5, role);
            if (photo.isEmpty()) {
                updateStmt.setNull(6, Types.VARCHAR);
            } else {
                updateStmt.setString(6, photo);
            }
            updateStmt.setBoolean(7, celibataire);
            updateStmt.setString(8, sexe);
            updateStmt.setInt(9, clientId);
            updateStmt.executeUpdate();

            // Update the table model
            for (int i = 0; i < clientsModel.getRowCount(); i++) {
                if ((int) clientsModel.getValueAt(i, 0) == clientId) {
                    clientsModel.setValueAt(name + " " + surname, i, 1);
                    clientsModel.setValueAt(login, i, 2);
                    clientsModel.setValueAt(role, i, 3);
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteClientFromDatabase(int clientId) {
        String deleteSql = "DELETE FROM clients WHERE Id_client = ?";

        try (Connection conn = MyConn.getConnection();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {

            deleteStmt.setInt(1, clientId);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}