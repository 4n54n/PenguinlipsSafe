/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package penguinlipsguard;

/**
 *
 * @author penguinlips
 */


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class db {
    public static Connection getConnection() throws SQLException, IOException{

        List<String> list = Files.readAllLines(new File("src/penguinlipsguard/custom.ini").toPath(), Charset.defaultCharset());
        String uname = list.get(3).replace("Username = ", "");

        String pass = list.get(4).replace("Password = ", "");

        String port = list.get(5).replace("Port = ", "");

        String database = list.get(6).replace("Database = ", "");//issue while converting """ to string
        



        Connection c = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost:"+port+"/penguinlipssafe",uname,pass);
            
        }catch (ClassNotFoundException | SQLException e){
        }
            return c;
        }
    public static Statement createStatement(){
        throw new UnsupportedOperationException("Not supported");
    }
}
