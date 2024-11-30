package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public class MyConn {
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;

    public static Connection getConnection() throws SQLException {
        // Load database credentials from connection.properties file
        String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        String decodePath = URLDecoder.decode(rootPath, StandardCharsets.UTF_8);
        String appConfigPath = decodePath + "resources/database.properties";
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(appConfigPath));
            URL = props.getProperty("DB_URL");
            USERNAME = props.getProperty("DB_USERNAME");
            PASSWORD = props.getProperty("DB_PASSWORD");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
