package model.session.mo;

public class LoggedUser {
  
  private String mail;
  private String username;
  private String uniqid;

  public String getUniqid() { return uniqid; }

  public void setUniqid(String uniqid) { this.uniqid = uniqid; }

  public String getMail() {
    return mail;
  }

  public void setMail(String userId) {
    this.mail = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String surname) {
    this.username = surname;
  }
  
}
