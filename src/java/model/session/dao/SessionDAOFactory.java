package model.session.dao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.session.dao.CookieImpl.CookieSessionDAOFactory;

public abstract class SessionDAOFactory {

  // List of DAO types supported by the factory
  public static final String COOKIEIMPL = "CookieImpl";

  public abstract void initSession(HttpServletRequest request, HttpServletResponse response);

  public abstract LoggedUserDAO getLoggedUserDAO();

  public static SessionDAOFactory getSesssionDAOFactory(String whichFactory) {

    if (whichFactory.equals(COOKIEIMPL)) {
      return new CookieSessionDAOFactory();
    } else {
      return null;
    }
  }
}

