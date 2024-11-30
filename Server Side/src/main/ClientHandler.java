package main;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            // Read login credentials from client
            String login = (String) in.readObject();
            String password = (String) in.readObject();

            // Authenticate user
            String role = authenticateUser(login, password);

            // Send role back to client
            out.writeObject(role);
            out.flush(); // Ensure all data is sent

        } catch (EOFException e) {
            System.out.println("Client disconnected abruptly.");
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public String authenticateUser(String login, String password) throws SQLException {
        String role = null;
        try (Connection con = MyConn.getConnection();
             PreparedStatement statement = con.prepareStatement("SELECT role FROM clients WHERE login = ? AND password = ?")) {
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                role = rs.getString("role");
            }
        }
        return role;
    }
}