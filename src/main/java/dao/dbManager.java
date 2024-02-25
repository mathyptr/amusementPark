package dao;


import java.sql.*;

/**
 * Class that manages a database connection.
 * To have a single instance of the Database this
 * class implements the Singleton pattern.
 */
public class dbManager {
    private String dbName = "amusepark.db";
    
    private static dbManager instance = null; 
    // Private constructor (Singleton pattern)
    private dbManager() {}
    
    public static dbManager getInstance() {
        // Create the object only if there's not an instance:
        if (instance == null) {
            instance = new dbManager();
        }
        return instance;
    }
    
    /**
     * Set the name of the database
     *
     * @param dbName Name of the database
     */
    public void setDatabase(String dbName) {
    	this.dbName = dbName;
    }

    /**
     * @param dbName Name of the database
     * @return Connection to the SQLite database
     */
    public Connection getConnection(String dbName) throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + dbName);
    }

    /**
     * Get the connection to the database
     *
     * @return Connection to the SQLite database
     */
    public Connection getConnection() throws SQLException {
        return getConnection(dbName);
    }

    /**
     * Close the given connection
     *
     * @param connection Connection to close
     */
    public void closeConnection(Connection connection) throws SQLException {
        connection.close();
   }

}