package model.dao;

import model.dao.MYSQLJDBCImpl.MySQLJDBCDAOFactory;

public abstract class DAOFactory {

  // List of DAO types supported by the factory
  public static final String MYSQLJDBCIMPL = "MySQLJDBCImpl";

  public abstract void beginTransaction();
  public abstract void commitTransaction();
  public abstract void rollbackTransaction();
  public abstract void closeTransaction();
  
  
  public abstract UtenteDAO getUserDAO();
  public abstract BackgroundDAO getBackgroundDao();
  public abstract TempsDAO getTempsDao();
  public abstract RoomDAO getRoomDao();
  public abstract SensorDAO getSensorDAO();

  public static DAOFactory getDAOFactory(String whichFactory) {

    if (whichFactory.equals(MYSQLJDBCIMPL)) {
      return new MySQLJDBCDAOFactory();
    } else {
      return null;
    }
  }
}

