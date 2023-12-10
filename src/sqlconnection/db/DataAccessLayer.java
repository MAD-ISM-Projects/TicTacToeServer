

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
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author Ramez
 */
public interface DataAccessLayer{
     public int insertPlayer(DTOPlayer player);
}
