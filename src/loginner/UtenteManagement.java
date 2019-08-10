package loginner;

import model.dao.DAOFactory;
import model.dao.UtenteDAO;
import model.mo.Utente;
import model.session.dao.LoggedUserDAO;
import model.session.dao.SessionDAOFactory;
import model.session.mo.LoggedUser;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class UtenteManagement {

    private UtenteManagement(){
    }

    public static void profileView(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;
        LoggedUser loggedUser;
        DAOFactory daoFactory = null;
        //Logger logger = LogService.getApplicationLogger();
        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            loggedUser = loggedUserDAO.find();
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);

            UtenteDAO utenteDAO = daoFactory.getUserDAO();
            if(loggedUser!=null){
                Utente utente = utenteDAO.findByUsername(loggedUser.getUsername());

                request.setAttribute("utente", utente);
                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("viewUrl", "profiloUtente");
            }else{
                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", null);
                request.setAttribute("viewUrl", "home");
            }
        } catch (Exception e) {
            //logger.log(Level.SEVERE, "Controller Error", e);
            throw new RuntimeException(e);
        }
    }

}
