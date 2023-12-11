

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
interface DataAccessLayer{
     public int insertPlayer(DTOPlayer player);
     public int isPlayerSignedUp(DTOPlayer player);
}

public class DBHandler implements DataAccessLayer{

    @Override
    public int insertPlayer(DTOPlayer player) {
        int result = -1;
        try {
            DriverManager.registerDriver(new ClientDriver());
            Connection con=DriverManager.getConnection("jdbc:derby://localhost:1527/PlayersAndGamplays","root","root");
            // check where the player is already registered or not since if this methode return 0 in this case but if not the player will signed up successfully with result>0
            if(isPlayerSignedUp(player)>0) {result++; return result;}
            PreparedStatement stmt=con.prepareStatement("INSERT INTO ROOT.PLAYERS (NAME,PASSWORD) values ('?','?');");
            String name= player.getName();
            String password= player.getPassword();
            stmt.setString(1,name);
            stmt.setString(2,password);
            result=stmt.executeUpdate();
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
     //   INSERT INTO ROOT.PLAYERS (NAME,PASSWORD) values ('sdf3','vsdf3'); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int isPlayerSignedUp(DTOPlayer player) {
        int result = 0;
        try {
            DriverManager.registerDriver(new ClientDriver());
            Connection con=DriverManager.getConnection("jdbc:derby://localhost:1527/PlayersAndGamplays","root","root");
            PreparedStatement stmt=con.prepareStatement("SELECT * FROM ROOT.PLAYERS WHERE \"NAME\" = ? AND PASSWORD = ?;");
            stmt.setString(1,player.getName());
            stmt.setString(2,player.getPassword());
            ResultSet rs = stmt.executeQuery() ;       
            if(rs.next())  return ++result;

            
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
        
    }
    
}
