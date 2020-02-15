package controller;

import backgrounder.Variabili;
import model.dao.DAOFactory;
import model.dao.TempsDAO;
import model.dao.UtenteDAO;
import model.mo.Lettura;
import model.mo.Utente;
import model.session.dao.LoggedUserDAO;
import model.session.dao.SessionDAOFactory;
import model.session.mo.LoggedUser;

import services.config.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class TermoManagement {

    private TermoManagement() {}

    /**view first temp page for thermo management*/
    public static void view(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;
        LoggedUser loggedUser;
        Utente user;
        DAOFactory daoFactory = null;
        Variabili var = new Variabili();
        try {
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();
            TempsDAO tempsdao = daoFactory.getTempsDao();
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            loggedUser = loggedUserDAO.find();
            loggedUser.setUniqid(loggedUserDAO.identify());
            UtenteDAO utenteDAO = daoFactory.getUserDAO();
            user = utenteDAO.findByUsername(loggedUser.getUsername());
            if(!utenteDAO.findLoginSession(user.getCodice(),loggedUser)) throw new IllegalAccessException("<h1>You must be logged in to see this data. Go away.</h1>");

            daoFactory.commitTransaction();
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "gestioneStanze");
        } catch (Exception e) {
            //logger.log(Level.SEVERE, "Controller Error", e);
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

    public static void insertNewView(HttpServletRequest request, HttpServletResponse response) {

        SessionDAOFactory sessionDAOFactory;
        LoggedUser loggedUser;
        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            loggedUser = loggedUserDAO.find();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "registrazioneStanza");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void insert(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;
        LoggedUser loggedUser;
        Utente user;
        DAOFactory daoFactory = null;
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
            /*messo prima per gestire il blocco catch con errori di form input*/
            request.setAttribute("loggedOn",user!=null);
            request.setAttribute("loggedUser", user);

            String name = request.getParameter("name");
            double maxTemp = Double.parseDouble(request.getParameter("tempMax"));
            double minTemp = Double.parseDouble(request.getParameter("tempMin"));
            double absMin = Double.parseDouble(request.getParameter("tempMinAbs"));
            LocalTime timeOnUno = LocalTime.parse(request.getParameter("timeOnUno"));
            LocalTime timeOffUno = LocalTime.parse(request.getParameter("timeOffUno"));
            LocalTime timeOnDue;
            LocalTime timeOffDue;
            LocalTime timeOnTre;
            LocalTime timeOffTre;
            try {
                timeOnDue = LocalTime.parse(request.getParameter("timeOnDue"));
                timeOffDue = LocalTime.parse(request.getParameter("timeOffDue"));
            }catch (DateTimeParseException e){
                timeOnDue = null;
                timeOffDue = null;
            }
            try {
                timeOnTre = LocalTime.parse(request.getParameter("timeOnTre"));
                timeOffTre = LocalTime.parse(request.getParameter("timeOffTre"));
            }catch (DateTimeParseException e){
                timeOnTre = null;
                timeOffTre = null;
            }

            /**fare controlli sui dati ricevuti e fare inserimento a db*/

            daoFactory.commitTransaction();
            request.setAttribute("appMessage", "meds");
            request.setAttribute("viewUrl", "actualtemp");
        } catch (Exception e) {
            try {
                if (daoFactory != null) {
                    daoFactory.rollbackTransaction();
                    request.setAttribute("appMessage", "meds");
                    request.setAttribute("viewUrl", "actualtemp");
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

}