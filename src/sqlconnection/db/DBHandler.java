

package sqlconnection.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

     public int signUp(DTOPlayer player) {
    try {
        // Check if the player with the given name already exists
        if (playerExists(player.getName())) {
            System.out.println("Player with the name " + player.getName() + " already exists.");
            return 0; // Return 0 to indicate that the sign-up failed
        }

        String sqlinsert = "INSERT INTO ROOT.PLAYERS (NAME, PASSWORD, STATUS, SCORE) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = connection.prepareStatement(sqlinsert);
        pst.setString(1, player.getName());
        pst.setString(2, player.getPassword());
        pst.setString(3, "offline");
        pst.setString(4, String.valueOf(player.getScore()));

        int rs = pst.executeUpdate();
        if (rs <= 0) {
            System.out.println("Insert failed");
        } else {
            System.out.println("Insert succeeded");
        }
        return rs;
    } catch (SQLException ex) {
        // Handle SQLException (print or log the exception, and consider proper error handling)
        ex.printStackTrace();
    }
    return 0;
}

// Helper method to check if a player with the given name already exists
private boolean playerExists(String playerName) throws SQLException {
    String query = "SELECT * FROM ROOT.PLAYERS WHERE NAME = ?";
    try (PreparedStatement pst = connection.prepareStatement(query)) {
        pst.setString(1, playerName);
        try (ResultSet resultSet = pst.executeQuery()) {
            if (resultSet.next()) {
                    return true;
            }
        }
    }
    return false;
}
 
       public int signIn(DTOPlayer player) {
        String sqlSelect = "SELECT * FROM ROOT.PLAYERS WHERE NAME = ? AND PASSWORD = ?";

        try (PreparedStatement pst = connection.prepareStatement(sqlSelect)) {
            pst.setString(1, player.getName());
            pst.setString(2, player.getPassword());
            
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                updateStatus(player.getName(), "online");
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

    public void updateStatus(String playerName, String status) throws SQLException {
        String sqlUpdate = "UPDATE ROOT.PLAYERS SET STATUS = ? WHERE NAME = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(sqlUpdate)) {
            updateStatement.setString(1, status);
            updateStatement.setString(2, playerName);
            updateStatement.executeUpdate();
        }
    }   
    public void updateAllPlayersStatusOffline() throws SQLException {
        String sqlUpdate = "UPDATE ROOT.PLAYERS SET STATUS = 'offline'";
        try (PreparedStatement updateStatement = connection.prepareStatement(sqlUpdate)) {
            updateStatement.executeUpdate();
        }
    }
     /*public ArrayList<UserOnline> getOnlinePlayers() throws SQLException {

        ArrayList<UserOnline> onlinePlayers = new ArrayList<>();

        String sql = " SELECT * FROM  ROOT.\"game\" Where ROOT.\"game\".\"status\"='online' ";
        PreparedStatement pst = connection.prepareStatement(sql);

        ResultSet resultSet = pst.executeQuery();
        Gson gson = new GsonBuilder().create();
        while (resultSet.next()) {
            onlinePlayers.add(new UserOnline(
                    resultSet.getString("ip"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("status"),
                    resultSet.getInt("score")
            ));
        }
        return onlinePlayers;
    }
     
  
     */
    public ArrayList<DTOPlayer> getOnlinePlayers() {
        ArrayList<DTOPlayer> onlinePlayers = new ArrayList<>();
        String sqlSelect = "SELECT * FROM ROOT.PLAYERS WHERE STATUS = 'online'";

        try (PreparedStatement selectOnline = connection.prepareStatement(sqlSelect);
             ResultSet rs = selectOnline.executeQuery()) {

            while (rs.next()) {
                // Retrieve values from the result set and create a DTOPlayer object
                String name = rs.getString("NAME");
                String password = rs.getString("PASSWORD");
                int score = rs.getInt("SCORE");
                String status = rs.getString("STATUS");

                System.out.println(status);
                DTOPlayer player = new DTOPlayer(name, password, score, status);
                onlinePlayers.add(player);
            }
        } catch (SQLException ex) {
            return new ArrayList<DTOPlayer>();
        }
        return onlinePlayers;
    }
          public int getPlayerScore(String userName) throws SQLException{
            String sql = "SELECT * FROM ROOT.PLAYERS WHERE NAME = ?";
            PreparedStatement pst= connection.prepareCall(sql);
            pst.setString(1,userName);
            ResultSet rs = pst.executeQuery();
            Gson gson=new GsonBuilder().create();
            if(rs.next()){
                return rs.getInt("score");
            }else{
                return 0;
            }
    }

        public synchronized int getOnlineRate() throws SQLException {
            String sql = "SELECT COUNT(NAME) AS total FROM ROOT.PLAYERS WHERE STATUS = ?";
             PreparedStatement pst = connection.prepareStatement(sql);
             pst.setString(1, "online");
             int count = 0;
             ResultSet rs = pst.executeQuery();
             while (rs.next()) {
                 count = rs.getInt("total");
             }
             return count;     
       }
        public synchronized int getbusyeRate() throws SQLException {
            String sql = "select COUNT(NAME) AS total FROM  ROOT.PLAYERS WHERE STATUS = ?";
             PreparedStatement pst = connection.prepareStatement(sql);
             pst.setString(1, "busy");
             int count = 0;
             ResultSet rs = pst.executeQuery();
             while (rs.next()) {
                 count = rs.getInt("total");
             }
             return count;     
       }
        public synchronized int getOfflineRate() throws SQLException {
            String sql = "select COUNT(NAME) AS total FROM  ROOT.PLAYERS WHERE STATUS = ?";
             PreparedStatement pst = connection.prepareStatement(sql);
             pst.setString(1, "offline");
             int count = 0;
             ResultSet rs = pst.executeQuery();
             while (rs.next()) {
                 count = rs.getInt("total");
             }
             return count;     
       }   
     
     /*public boolean makePlayerBusy(DTOPlayer player1,DTOPlayer player2){
        try {
            String sql = "UPDATE ROOT.PLAYERS SET STATUS = ? WHERE NAME = ? or ROOT.PLAYERS SET STATUS = ? WHERE NAME = ?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, "busy");
            pst.setString(2, player1.getName());
            pst.setString(3, player2.getName());
            int rs = pst.executeUpdate();
            System.out.println(" rs = "+rs);
            return rs!=0;
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return false;
        }  
      }
     */
    /*  public int getPlayerScore(String userName) throws SQLException{
            String sql = "SELECT * FROM ROOT.PLAYERS WHERE NAME = ?";
            PreparedStatement pst= connection.prepareCall(sql);
            pst.setString(1,userName);
            ResultSet rs = pst.executeQuery();
            Gson gson=new GsonBuilder().create();
            if(rs.next()){
                return rs.getInt("score");
            }else{
                return 0;
            }
    }*/

}

