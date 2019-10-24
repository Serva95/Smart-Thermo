package loginner;

import static controller.MailPack.sendmail;
import static controller.PasswordHash.*;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.DAOFactory;
import model.dao.UtenteDAO;
import model.mo.Utente;

import model.session.dao.LoggedUserDAO;
import model.session.dao.SessionDAOFactory;
import model.session.mo.LoggedUser;

import services.config.*;
import services.logservice.LogService;

public class LoginManagement {
    
    private LoginManagement(){ }
    
    /**logout for all users*/
    public static void logoutUser(HttpServletRequest request, HttpServletResponse response) {
        
        SessionDAOFactory sessionDAOFactory;
        Logger logger = LogService.getApplicationLogger();
        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            loggedUserDAO.destroy();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            throw new RuntimeException(e);
        }
        request.setAttribute("loggedOn",false);
        request.setAttribute("loggedUser", null);
        request.setAttribute("viewUrl", "home");
    }
    
    /**login view page for the user*/
    public static void view(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;
        LoggedUser loggedUser;
        Logger logger = LogService.getApplicationLogger();
        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            loggedUser = loggedUserDAO.find();
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "accedi");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            throw new RuntimeException(e);
        }
    }

    /**login section for the registered user*/
    public static void loginUser(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        LoggedUser loggedUser;
        String applicationMessage = null;
        Logger logger = LogService.getApplicationLogger();
        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();
            String uname = request.getParameter("username");
            String pwd = request.getParameter("pwd");
            boolean rememberMe = ((request.getParameter("remain")!=null)? request.getParameter("remain"): "").equalsIgnoreCase("on");
            int days = 0;
            if(rememberMe)try {days = Integer.parseInt(request.getParameter("days"));}catch (NumberFormatException e){days=0;}

            UtenteDAO userDAO = daoFactory.getUserDAO();
            Utente user = userDAO.findByUsername(uname);
            boolean verified = false;
            try{
                verified = passwordVerifyPBKDF2(pwd, user.getPassword());
            }catch (NullPointerException e){}
            if (user == null || !verified) {
                loggedUserDAO.destroy();
                applicationMessage = "Username o password errati!";
                loggedUser=null;
                request.setAttribute("viewUrl", "accedi");
            } else {
                loggedUser = loggedUserDAO.create(user.getEmail(), user.getUsername(), rememberMe, days);
                userDAO.beginLoginSession(user.getCodice(), loggedUser, LocalDate.now().plusDays(days));
                request.setAttribute("viewUrl", "home");
            }

            daoFactory.commitTransaction();
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
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
