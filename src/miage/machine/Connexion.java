package miage.machine;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Classe de connexion singleton
 * @author maxime
 */
public class Connexion {
    
    private static final String driver = Main.properties.getProperty("driver");
    private static final String dbURL = Main.properties.getProperty("dbURL");
    private static final String dbUser = Main.properties.getProperty("dbUser");
    private static final String dbPassword = Main.properties.getProperty("dbPassword");
    private static Connection conn = null;
    final static Logger LOGGER = Logger.getLogger(Connexion.class.getName());
    
    /**
     * Get connexion instance.
     * - Check if the driver exist
     * - get the connexion from the driver
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException 
     * @throws java.sql.SQLException 
     */
    public static Connection getInstance() throws Exception{
            if(conn == null){
                try{
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
                }
                catch(ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e){
                    Main.debug(e, LOGGER);
                    throw new Exception(Main.errorMessages.get("errorDB"));
                }
            }		
            return conn;	
    }
}
