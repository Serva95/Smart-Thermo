package model.dao.MYSQLJDBCImpl;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import model.dao.*;
import services.config.Configuration;

public class MySQLJDBCDAOFactory extends DAOFactory {
    
    private Connection connection;
    
    @Override
    public void beginTransaction() {
        try {
            Class.forName(Configuration.DATABASE_DRIVER);
            this.connection = DriverManager.getConnection(Configuration.DATABASE_URL, Configuration.USERNAME, Configuration.PASSWORD_DB);
            this.connection.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void commitTransaction() {
        try {
            this.connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void rollbackTransaction() {
        try {
            this.connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void closeTransaction() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public UtenteDAO getUserDAO() {
        return new UtenteDAOMYSQLJDBCImpl(connection);
    }
    
    @Override
    public BackgroundDAO getBackgroundDao() {
        return new BackgroundDAOMYSQLJDBCImpl(connection);
    }
    
    @Override
    public TempsDAO getTempsDao(){
        return new TempsDAOMYSQLJDBCImpl(connection);
    }

    @Override
    public RoomDAO getRoomDao() { return new RoomDAOMYSQLJDBCImpl(connection); }

    @Override
    public SensorDAO getSensorDAO() { return new SensorDAOMYSQLJDBCImpl(connection); }

}