package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.ParseException;

public class LoginWindow extends JFrame {
    private JPanel panel;
    private static JTextField loginField;
    private JPasswordField passwordField;
    private JLabel loginLabel, passwordLabel;


    public static JTextField getLoginField() {
        return loginField;
    }

    public LoginWindow() {
        setTitle("Authentification");
        setSize(525, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        loginLabel = new JLabel("Login:");
        loginField = new JTextField();

        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Ok");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
        panel = new JPanel();

        loginLabel.setBounds(125, 60, 100, 25);
        panel.add(loginLabel);

        loginField.setBounds(225, 60, 150, 25);
        panel.add(loginField);

        passwordLabel.setBounds(125, 100, 100, 25);
        panel.add(passwordLabel);

        passwordField.setBounds(225, 100, 150, 25);
        panel.add(passwordField);

        loginButton.setBounds(130, 175, 80, 25);
        panel.add(loginButton);

        cancelButton.setBounds(300, 175, 80, 25);
        panel.add(cancelButton);

        panel.setLayout(null);
        panel.setBounds(0, 0, 525, 300);
        add(panel);

        setVisible(true);

    }

    public void login() {
        String login = loginField.getText();
        String password = new String(passwordField.getPassword());

        try (Socket socket = new Socket("localhost", 23456); // Connect to server
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // Send login credentials to server
            out.writeObject(login);
            out.writeObject(password);
            out.flush(); // Ensure all data is sent

            // Receive role from server
            String role;
            try {
                role = (String) in.readObject(); // Attempt to read role
            } catch (EOFException e) {
                System.out.println("Reached end of stream while waiting for role.");
                return; // Handle end of stream gracefully
            }

            if ("admin".equals(role)) {
                JOptionPane.showMessageDialog(this, "Welcome, admin!");
                new AdminWindow(); // Create an instance of AdminPanel
                dispose(); // Close login window
            } else if ("user".equals(role)) {
                JOptionPane.showMessageDialog(this, "Welcome " + login + "!");
                new ClientWindow(); // Create an instance of UserWindow
                dispose(); // Close login window
            } else {
                JOptionPane.showMessageDialog(this, "Unknown user");
                cancel();
            }

        } catch (IOException | ClassNotFoundException | ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to server.");
        }
    }

    public void cancel(){
        System.out.println("Cancel button clicked");
        loginField.setText("");
        passwordField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginWindow::new);
    }
}
