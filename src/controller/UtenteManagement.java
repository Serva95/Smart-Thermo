package controller;

import model.dao.DAOFactory;
import model.dao.UtenteDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Utente;
import model.session.dao.LoggedUserDAO;
import model.session.dao.SessionDAOFactory;
import model.session.mo.LoggedUser;
import services.config.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static controller.PasswordHash.passwordHashPBKDF2;

public class UtenteManagement {

    private UtenteManagement(){}

    public static void profileView(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        LoggedUser loggedUser;
        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();
            loggedUser = loggedUserDAO.find();
            if(loggedUser == null) throw new IllegalAccessException("<h1>You must be logged in to see this data. Go away.</h1>");
            UtenteDAO userDAO = daoFactory.getUserDAO();
            if(loggedUser.getUsername()!=null){
                Utente user;
                user = userDAO.findByUsername(loggedUser.getUsername());

                daoFactory.commitTransaction();
                request.setAttribute("utente", user);
                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("viewUrl", "profiloUtente");
            }else{
                daoFactory.commitTransaction();
                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", null);
                request.setAttribute("viewUrl", "home");
            }
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
        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            loggedUser = loggedUserDAO.find();
            if(loggedUser == null) throw new IllegalAccessException("<h1>You must be logged in to see this data. Go away.</h1>");
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();
            UtenteDAO utenteDAO = daoFactory.getUserDAO();
            if(loggedUser!=null){
                Utente utente = utenteDAO.findByUserMail(loggedUser.getMail());
                daoFactory.commitTransaction();
                request.setAttribute("utente", utente);
                request.setAttribute("loggedOn", true);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("viewUrl", "profiloUtenteEdit");
            }else{
                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);
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

    public static void update(HttpServletRequest request, HttpServletResponse response){

        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        LoggedUser loggedUser;
        String applicationMessage = null;
        Boolean success;
        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            loggedUser = loggedUserDAO.find();
            if(loggedUser == null) throw new IllegalAccessException("<h1>You must be logged in to see this data. Go away.</h1>");
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();
            UtenteDAO utenteDAO = daoFactory.getUserDAO();
            if(loggedUser!=null){
                Utente au = utenteDAO.findByUserMail(loggedUser.getMail());
                Utente utente = new Utente();
                long cod = Long.parseLong(request.getParameter("cod"));
                utente.setCodice(cod);
                String email = request.getParameter("email");
                String pwd = request.getParameter("pwd");
                String username = request.getParameter("username");
                boolean cambiamenti = false;
                if(au.getEmail().equalsIgnoreCase(email) || "".equals(email)) utente.setEmail(au.getEmail());
                else{
                    utente.setEmail(email);
                    cambiamenti = true;
                }
                utente.setUsername((au.getUsername().equalsIgnoreCase(username) || "".equals(username))? au.getUsername(): username);
                if(!"".equals(pwd)){
                    utente.setPassword(passwordHashPBKDF2(pwd));
                    cambiamenti = true;
                }else utente.setPassword(au.getPassword());
                try {
                    utenteDAO.update(utente);
                    applicationMessage = "<h3>Hai aggiornato i tuoi dati con successo!</h3>";
                    success = true;
                } catch (DuplicatedObjectException e) {
                    applicationMessage = "<h3>La mail da te usata è già esistente per un altro utente riprova con una diversa</h3>";
                    success = false;
                }
                if(success){
                    request.setAttribute("viewUrl", "response");
                    request.setAttribute("loggedUser", loggedUser);
                    if(cambiamenti){
                        loggedUserDAO.destroy();
                        request.setAttribute("viewUrl", "home");
                        request.setAttribute("loggedUser", null);
                    }
                    daoFactory.commitTransaction();
                    request.setAttribute("applicationMessage", applicationMessage);
                    request.setAttribute("loggedOn", !cambiamenti);
                    request.setAttribute("email", loggedUser.getMail());
                    request.setAttribute("success", true);
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