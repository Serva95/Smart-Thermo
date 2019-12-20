package controller;

import model.dao.DAOFactory;
import model.dao.UtenteDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Sessione;
import model.mo.Utente;
import model.session.dao.LoggedUserDAO;
import model.session.dao.SessionDAOFactory;
import model.session.mo.LoggedUser;
import services.config.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static controller.PasswordHash.passwordHashPBKDF2;
import static controller.PasswordHash.passwordVerifyPBKDF2;

public class UtenteManagement {

    private UtenteManagement(){}

    public static void profileView(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        Utente user;
        LoggedUser loggedUser;
        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();
            loggedUser = loggedUserDAO.find();
            loggedUser.setUniqid(loggedUserDAO.identify());
            UtenteDAO utenteDAO = daoFactory.getUserDAO();
            user = utenteDAO.findByUsername(loggedUser.getUsername());
            if(!utenteDAO.findLoginSession(user.getCodice(),loggedUser)) throw new IllegalAccessException("<h1>You must be logged in to see this data. Go away.</h1>");
            Sessione sessioni[] = utenteDAO.findAllSessions(loggedUser);
            daoFactory.commitTransaction();
            request.setAttribute("sessioni", sessioni);
            request.setAttribute("utente", user);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "profiloUtente");

        } catch (Exception e) {
            try {
                if (daoFactory != null) {
                    daoFactory.rollbackTransaction();
                }
            } catch (Throwable t) {}
            throw new RuntimeException(e);
        } finally {
            try {
                if (daoFactory != null) {
                    daoFactory.closeTransaction();
                }
            } catch (Throwable t) {}
        }
    }

    public static void profileEdit(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;
        LoggedUser loggedUser;
        DAOFactory daoFactory = null;
        Utente user;
        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();
            loggedUser = loggedUserDAO.find();
            loggedUser.setUniqid(loggedUserDAO.identify());
            UtenteDAO utenteDAO = daoFactory.getUserDAO();
            user = utenteDAO.findByUsername(loggedUser.getUsername());
            if(!utenteDAO.findLoginSession(user.getCodice(),loggedUser)) throw new IllegalAccessException("<h1>You must be logged in to see this data. Go away.</h1>");

            daoFactory.commitTransaction();
            request.setAttribute("utente", user);
            request.setAttribute("loggedOn", true);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "profiloUtenteEdit");
        } catch (Exception e) {
            try {
                if (daoFactory != null) {
                    daoFactory.rollbackTransaction();
                }
            } catch (Throwable t) { }
            throw new RuntimeException(e);
        } finally {
            try {
                if (daoFactory != null) {
                    daoFactory.closeTransaction();
                }
            } catch (Throwable t) { }
        }
    }

    public static void update(HttpServletRequest request, HttpServletResponse response){

        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        LoggedUser loggedUser;
        Utente user;
        String applicationMessage = null;
        Boolean success = false;
        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();
            loggedUser = loggedUserDAO.find();
            loggedUser.setUniqid(loggedUserDAO.identify());
            UtenteDAO utenteDAO = daoFactory.getUserDAO();
            user = utenteDAO.findByUsername(loggedUser.getUsername());
            if(!utenteDAO.findLoginSession(user.getCodice(),loggedUser)) throw new IllegalAccessException("<h1>You must be logged in to see this data. Go away.</h1>");
            if(user!=null){
                Utente au = user;
                Utente utente = new Utente();
                long cod = Long.parseLong(request.getParameter("cod"));
                utente.setCodice(cod);
                String email = request.getParameter("email");
                String pwd = request.getParameter("pwd");
                String oldpwd = request.getParameter( "oldpwd");
                String username = request.getParameter("username");
                boolean cambiamenti = false;
                boolean pswdError = true;
                if(passwordVerifyPBKDF2(oldpwd, user.getPassword())) {
                    pswdError = false;
                    if (au.getEmail().equalsIgnoreCase(email) || "".equals(email)) utente.setEmail(au.getEmail());
                    else {
                        utente.setEmail(email);
                        cambiamenti = true;
                    }
                    utente.setUsername((au.getUsername().equalsIgnoreCase(username) || "".equals(username)) ? au.getUsername() : username);
                    if (!"".equals(pwd)) {
                        utente.setPassword(passwordHashPBKDF2(pwd));
                        cambiamenti = true;
                    } else utente.setPassword(au.getPassword());
                    try {
                        utenteDAO.update(utente);
                        applicationMessage = "<h3>Hai aggiornato i tuoi dati con successo!</h3>";
                        success = true;
                    } catch (DuplicatedObjectException e) {
                        applicationMessage = "<h3>La mail da te usata è già esistente per un altro utente riprova con una diversa</h3>";
                        success = false;
                    }
                }
                if(success){
                    request.setAttribute("viewUrl", "profiloUtenteEdit");
                    request.setAttribute("loggedUser", loggedUser);
                    if(cambiamenti){
                        loggedUserDAO.destroy();
                        request.setAttribute("viewUrl", "home");
                        request.setAttribute("loggedUser", null);
                    }else{
                        request.setAttribute("utente", user);
                    }
                    daoFactory.commitTransaction();
                    request.setAttribute("applicationMessage", applicationMessage);
                    request.setAttribute("loggedOn", !cambiamenti);
                    request.setAttribute("email", loggedUser.getMail());
                    request.setAttribute("success", true);
                }else if(pswdError){
                    request.setAttribute("viewUrl", "profiloUtenteEdit");
                    request.setAttribute("loggedUser", loggedUser);
                    request.setAttribute("utente", user);
                    request.setAttribute("applicationMessage", "Password attuale inserita in modo errato");
                    request.setAttribute("loggedOn", !cambiamenti);
                }else{
                    request.setAttribute("applicationMessage", applicationMessage);
                    request.setAttribute("loggedOn", loggedUser!=null);
                    request.setAttribute("success", false);
                    request.setAttribute("loggedUser", loggedUser);
                    request.setAttribute("email", utente.getEmail());
                    request.setAttribute("viewUrl", "response");
                }
            }else{
                request.setAttribute("applicationMessage", null);
                request.setAttribute("loggedOn", loggedUser!=null);
                request.setAttribute("viewUrl", "home");
            }
        } catch (Exception e) {
            try {
                if (daoFactory != null) {
                    daoFactory.rollbackTransaction();
                }
            } catch (Throwable t) { }
            throw new RuntimeException(e);
        } finally {
            try {
                if (daoFactory != null) {
                    daoFactory.closeTransaction();
                }
            } catch (Throwable t) { }
        }
    }

}