package model.dao.MYSQLJDBCImpl;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import model.dao.BackgroundDAO;

import services.config.Configuration;

import model.dao.DAOFactory;
import model.dao.TempsDAO;
import model.dao.UtenteDAO;

public class MySQLJDBCDAOFactory extends DAOFactory {
    
    private Connection connection;
    
    @Override
    public void beginTransaction() {
        try {
            Class.forName(Configuration.DATABASE_DRIVER);
            this.connection = DriverManager.getConnection(Configuration.DATABASE_URL);
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
    
}