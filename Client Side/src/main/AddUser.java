package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddUser extends JDialog {
    private JTextField nameField, surnameField, loginField, passwordField, roleField, photoField, sexeField;
    private JCheckBox celibataireCheckBox;
    private JButton addButton, cancelButton;
    private boolean succeeded;

    public AddUser(Frame parent, String name, String surname, String login, String password, String role, String photo, boolean celibataire, String sexe) {
        super(parent, "Ajouter/Modifier Utilisateur", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField(name, 15);
        surnameField = new JTextField(surname, 15);
        loginField = new JTextField(login, 15);
        passwordField = new JTextField(password, 15);
        roleField = new JTextField(role, 15);
        photoField = new JTextField(photo, 15);
        sexeField = new JTextField(sexe, 15);
        celibataireCheckBox = new JCheckBox();
        celibataireCheckBox.setSelected(celibataire);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Nom:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Prénom:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(surnameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Login:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        add(loginField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Mot de passe:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Role:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        add(roleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Photo:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        add(photoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Célibataire:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        add(celibataireCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        add(new JLabel("Sexe:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        add(sexeField, gbc);

        addButton = new JButton("Ajouter");
        cancelButton = new JButton("Annuler");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nameField.getText().isEmpty() || surnameField.getText().isEmpty() || loginField.getText().isEmpty() || passwordField.getText().isEmpty() || roleField.getText().isEmpty() || sexeField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(AddUser.this, "All fields except photo must be filled", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    succeeded = true;
                    dispose();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                succeeded = false;
                dispose();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, gbc);

        setPreferredSize(new Dimension(400, 400));
        pack();
        setLocationRelativeTo(parent);
    }

    public String getName() {
        return nameField.getText();
    }

    public String getSurname() {
        return surnameField.getText();
    }

    public String getLogin() {
        return loginField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public String getRole() {
        return roleField.getText();
    }

    public String getPhoto() {
        return photoField.getText();
    }

    public boolean isCelibataire() {
        return celibataireCheckBox.isSelected();
    }

    public String getSexe() {
        return sexeField.getText();
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}