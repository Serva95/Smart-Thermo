package model.session.dao;

import model.session.mo.LoggedUser;

public interface LoggedUserDAO {

  public LoggedUser create(String email,String username,boolean rememberMe);

  public void update(LoggedUser loggedUser);

  public void destroy();

  public LoggedUser find();
  
}
