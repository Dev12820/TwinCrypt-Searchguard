/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DualServer;

/**
 *
 * @author Murthi
 */
import java.sql.Connection;
import java.sql.DriverManager;

public class SQLconnection {
    
static Connection con;

    
    /**
     *
     * @return
     */
    public static Connection getconnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dsae", "root", "root");
        } catch (Exception e) {
        }
        return con;
    }
}
