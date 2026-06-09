package com.tandf.casestudy.banking.db;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final Properties props = new Properties();
    static {
         
    try (InputStream input =
         DBConnection.class.getClassLoader()
                           .getResourceAsStream("db.properties")) 
                           {
    props.load(input);
} catch(IOException e){
    e.printStackTrace();
}     
    }
   
    
    private static final String url = props.getProperty("db.url");
    private static final String username = props.getProperty("db.username");
    private static final String password = props.getProperty("db.password");
    public static Connection getConnection() throws SQLException{
       return DriverManager.getConnection(
                    url,
                    username,
                    password);
    }
    
}
