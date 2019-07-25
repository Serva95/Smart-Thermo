package model.dao;

import model.dao.exception.DuplicatedObjectException;
import model.mo.Utente;

public interface UtenteDAO {

    Utente insert(Utente user) throws DuplicatedObjectException;

    void update(Utente user) throws DuplicatedObjectException ;

    void delete(Utente user);

    Utente findByUserCodice(long codice);

    Utente findByUserMail(String email);

    Utente findByUsername(String username);
}
