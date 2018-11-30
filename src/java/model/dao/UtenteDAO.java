package model.dao;

import model.dao.exception.DuplicatedObjectException;
import model.mo.Utente;

public interface UtenteDAO {

    public Utente insert(Utente user) throws DuplicatedObjectException;

    public void update(Utente user) throws DuplicatedObjectException ;

    public void delete(Utente user);

    public Utente findByUserCodice(long codice);

    public Utente findByUserMail(String email);

}
