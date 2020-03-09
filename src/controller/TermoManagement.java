package controller;

import backgrounder.Variabili;
import model.dao.DAOFactory;
import model.dao.RoomDAO;
import model.dao.TempsDAO;
import model.dao.UtenteDAO;
import model.dao.exception.InputFormatException;
import model.mo.Stanza;
import model.mo.Utente;
import model.session.dao.LoggedUserDAO;
import model.session.dao.SessionDAOFactory;
import model.session.mo.LoggedUser;

import services.config.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

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
            RoomDAO roomDAO = daoFactory.getRoomDao();
            user = utenteDAO.findByUsername(loggedUser.getUsername());
            if(!utenteDAO.findLoginSession(user.getCodice(),loggedUser)) throw new IllegalAccessException("<h1>You must be logged in to see this data. Go away.</h1>");
            Stanza[] stanze = roomDAO.findAllRooms();
            daoFactory.commitTransaction();
            request.setAttribute("stanze",stanze);
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
        LoggedUser loggedUser = null;
        Utente user;
        Stanza stanza;
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
            RoomDAO roomDAO = daoFactory.getRoomDao();
            user = utenteDAO.findByUsername(loggedUser.getUsername());
            if(!utenteDAO.findLoginSession(user.getCodice(),loggedUser)) throw new IllegalAccessException("<h1>You must be logged in to see this data. Go away.</h1>");
            String name = request.getParameter("name");
            if (name.length() >= 64) throw new InputFormatException("Lunghezza nome incorretta");
            double maxTemp = Double.parseDouble(request.getParameter("tempMax"));
            double minTemp = Double.parseDouble(request.getParameter("tempMin"));
            double absMin = Double.parseDouble(request.getParameter("tempMinAbs"));
            if(!(maxTemp > minTemp && minTemp > absMin)) throw new InputFormatException("Le temperature inserite non sono corrette");
            stanza = new Stanza(name, maxTemp, minTemp, absMin);
            LocalTime[][] giorniOnOff = new LocalTime[7][];
            for(int i=0; i<7; i++) {
                LocalTime[] onOff = new LocalTime[6];
                onOff[0] = LocalTime.parse(request.getParameter("timeonuno"+i));
                onOff[1] = LocalTime.parse(request.getParameter("timeoffuno"+i));
                try {
                    onOff[2] = LocalTime.parse(request.getParameter("timeondue"+i));
                    onOff[3] = LocalTime.parse(request.getParameter("timeoffdue"+i));
                } catch (DateTimeParseException e) {
                    onOff[2] = null;
                    onOff[3] = null;
                }
                try {
                    if(onOff[2]==null) throw new InputFormatException();
                    onOff[4] = LocalTime.parse(request.getParameter("timeontre"+i));
                    onOff[5] = LocalTime.parse(request.getParameter("timeofftre"+i));
                } catch (DateTimeParseException e) {
                    onOff[4] = null;
                    onOff[5] = null;
                }
                if(onOff[0].isAfter(onOff[1])) throw new InputFormatException("Gli orari inseriti non sono corretti riprova");
                if(onOff[2]!=null){
                    if(onOff[1].isAfter(onOff[2]) || onOff[2].isAfter(onOff[3])) throw new InputFormatException("Gli orari inseriti non sono corretti riprova");
                }
                if(onOff[4]!=null){
                    if(onOff[1].isAfter(onOff[4]) || onOff[3].isAfter(onOff[4]) || onOff[4].isAfter(onOff[5])) throw new InputFormatException("Gli orari inseriti non sono corretti riprova");
                }
                giorniOnOff[i] = onOff;
            }
            stanza.setTurnOnOffTimes(giorniOnOff);
            roomDAO.insert(stanza);
            daoFactory.commitTransaction();
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("appMsg", "Stanza Inserita Correttamente");
            request.setAttribute("viewUrl", "gestioneStanze");
        } catch (Exception e) {
            try {
                if (daoFactory != null) {
                    daoFactory.rollbackTransaction();
                }
            } catch (Throwable t) {}
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("appMsg", "Errore: " + e.getMessage());
            request.setAttribute("viewUrl", "gestioneStanze");
            String err = e.toString().substring(0,19);
            if(!err.equalsIgnoreCase("model.dao.exception")) {
                throw new RuntimeException(e);
            }
        } finally {
            try {
                if (daoFactory != null) {
                    daoFactory.closeTransaction();
                }
            } catch (Throwable t) {}
        }
    }

}