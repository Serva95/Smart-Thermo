package model.mo;

public class Utente{
    private String email;
    private String username;
    private String password;
    private long codice;
    private boolean deleted; //?

    public Utente(){}
    
    public Utente(String email, String username, String password){
        this.email = email;
        this.username = username;
        this.password = password;
    }
    
    public Utente(String email, String username, String password, long codice, boolean deleted){
        this.email = email;
        this.username = username;
        this.password = password;
        this.codice = codice;
        this.deleted = deleted;
    }
    
    public String getEmail() {
        return email;
    }

    public long getCodice() {
        return codice;
    }

    public void setCodice(long codice) {
        this.codice = codice;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    
}
