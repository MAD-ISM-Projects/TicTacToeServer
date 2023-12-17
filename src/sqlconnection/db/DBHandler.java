

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
        String sqlinsert="INSERT INTO ROOT.PLAYERS (IP,NAME,PASSWORD,STATUS,SCORE) VALUES (?,?,?,?,?)";
        PreparedStatement pst = connection.prepareStatement(sqlinsert);
        pst.setString(1, player.getIp());
        pst.setString(2, player.getName());
        pst.setString(3, player.getPassword());
        
        pst.setString(4, "online");
        pst.setString(5, String.valueOf(player.getScore()));

        int rs = pst.executeUpdate();
        if (rs == 0) {
            System.out.println("insert faild");
        } else {
            System.out.println("insert succeded");
        }
        return rs;
    }
   
        
    
}
