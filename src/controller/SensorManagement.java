package controller;

import model.dao.DAOFactory;
import model.dao.RoomDAO;
import model.dao.SensorDAO;
import model.dao.UtenteDAO;
import model.mo.Sensore;
import model.mo.Stanza;
import model.mo.Utente;
import model.session.dao.LoggedUserDAO;
import model.session.dao.SessionDAOFactory;
import model.session.mo.LoggedUser;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SensorManagement {
    private SensorManagement() {}

    public static void view(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;
        LoggedUser loggedUser;
        Utente user;
        DAOFactory daoFactory = null;
        try {
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            loggedUser = loggedUserDAO.find();
            loggedUser.setUniqid(loggedUserDAO.identify());
            UtenteDAO utenteDAO = daoFactory.getUserDAO();
            SensorDAO sensorDAO = daoFactory.getSensorDAO();
            user = utenteDAO.findByUsername(loggedUser.getUsername());
            if(!utenteDAO.findLoginSession(user.getCodice(),loggedUser)) throw new IllegalAccessException("<h1>You must be logged in to see this data. Go away.</h1>");
            Sensore[] sensori = sensorDAO.findAllSensors();
            daoFactory.commitTransaction();
            request.setAttribute("sensori", sensori);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "gestioneSensori");
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
}
