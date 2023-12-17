

package sqlconnection.db;

import dto.DTOPlayer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author Ramez
 */


public class DBHandler{
    private Connection connection;
    PreparedStatement pst;
    public DBHandler(){
           try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/players", "root", "root");

        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

       public int signUp(DTOPlayer player) throws SQLException {

      //  String sqlinsert = "INSERT INTO ROOT.\"players\"(ROOT.\"players\".\"ip\",ROOT.\"players\".\"name\",ROOT.\"players\".\"password\",ROOT.\"players\".\"score\")VALUES (?,?,?,?)";
        String sqlinsert="INSERT INTO ROOT.PLAYERS (NAME,PASSWORD,STATUS,SCORE) VALUES (?,?,?,?)";
        PreparedStatement pst = connection.prepareStatement(sqlinsert);
//        pst.setString(1, player.getIp());
        pst.setString(1, player.getName());
        pst.setString(2, player.getPassword());
        
        pst.setString(3, "online");
        pst.setString(4, String.valueOf(player.getScore()));

        int rs = pst.executeUpdate();
        if (rs == 0) {
            System.out.println("insert faild");
        } else {
            System.out.println("insert succeded");
        }
        return rs;
    }
 
       public int signIn(DTOPlayer player) throws SQLException {
        String sqlSelect = "SELECT * FROM ROOT.PLAYERS WHERE NAME = ? AND PASSWORD = ?";

        try (PreparedStatement pst = connection.prepareStatement(sqlSelect)) {
            pst.setString(1, player.getName());
            pst.setString(2, player.getPassword());
            
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return 1;
//                String count = rs.getString(1);
//                if (count > 0) {
//                    // Credentials are valid, user exists
//                    System.out.println("Sign in successful");
//                    /*calling the updateStatus()*/
//                    updateStatus(player.getName(), "online");
//                    return 1;
//                } else {
//                    // No matching user found
//                    System.out.println("Sign in failed: Invalid credentials");
//                    return -1;
//                }
            } else {
                // Error in query execution
                System.out.println("Sign in failed: Database query error");
                return -1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

     private void updateStatus(String playerName, String status) throws SQLException {
        String sqlUpdate = "UPDATE ROOT.PLAYERS SET STATUS = ? WHERE NAME = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(sqlUpdate)) {
            updateStatement.setString(1, status);
            updateStatement.setString(2, playerName);
            updateStatement.executeUpdate();
        }
    }   
    
}
