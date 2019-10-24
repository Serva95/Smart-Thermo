package model.dao;

import model.dao.exception.DuplicatedObjectException;
import model.mo.Sessione;
import model.mo.Utente;
import model.session.mo.LoggedUser;

import java.time.LocalDate;

public interface UtenteDAO {

    Utente insert(Utente user) throws DuplicatedObjectException;

    void update(Utente user) throws DuplicatedObjectException;

    void delete(Utente user);

    Utente findByUserCodice(long codice);

    Utente findByUserMail(String email);

    Utente findByUsername(String username);

    void beginLoginSession(long codice, LoggedUser loggedUser, LocalDate expire);

    boolean findLoginSession(long codice, LoggedUser loggedUser);

    Sessione[] findAllSessions(LoggedUser loggedUser);
}
