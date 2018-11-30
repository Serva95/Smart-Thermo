package model.dao;

import backgrounder.Variabili;
import model.dao.exception.DuplicatedObjectException;

public interface BackgroundDAO {
    
    public boolean insert(Variabili var) throws DuplicatedObjectException ;
    
    /*public void update(Utente user) throws DuplicatedObjectException ;
    
    public void delete(Utente user);
    
    public Utente findByUserCodice(long codice);
    
    public Utente findByUserMail(String email);
    */
}
