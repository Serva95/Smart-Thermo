package controller;

import backgrounder.BackgroundController;
import backgrounder.Variabili;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import model.dao.DAOFactory;
import model.dao.TempsDAO;
import model.dao.UtenteDAO;
import model.mo.Lettura;
import model.mo.Utente;
import services.config.*;
import model.session.mo.LoggedUser;
import model.session.dao.SessionDAOFactory;
import model.session.dao.LoggedUserDAO;

public class HomeManagement {

    private HomeManagement() {}
    
    public static void view(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;
        LoggedUser loggedUser;
        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            loggedUser = loggedUserDAO.find();
            
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "home");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**per quanti giorni di media mi servono*/
    public synchronized static void getmeds(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        Variabili var = new Variabili();
        try {
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();
            TempsDAO tempsdao = daoFactory.getTempsDao();
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            int number_of_reads = Integer.parseInt(request.getParameter("number"));
            Lettura[] meds = tempsdao.Readmeds(number_of_reads);
            StringBuilder output= new StringBuilder();
            for(Lettura outmedrd : meds) output.append(outmedrd.getReadingdatetime().toLocalDate().toString()).append(",");
            output.append("#");
            for(Lettura outmedrd : meds) output.append(outmedrd.getTemp()).append(",");
            output.append("#");
            for(Lettura outmedrd : meds) output.append(outmedrd.getHum()).append(",");

            try (PrintWriter out = response.getWriter()) {
                out.println(output);
                out.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
            
        daoFactory.commitTransaction();
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

    /**update dynamically all the graphs when "gettempvie" page is loaded*/
    public static void updateTemps(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        try {
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();
            TempsDAO tempsdao = daoFactory.getTempsDao();
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            LocalTime time = LocalTime.parse(request.getParameter("dati"));
            Lettura last = tempsdao.Readlast();
            daoFactory.commitTransaction();
            String total = "";
            if(last.getReadingdatetime().toLocalTime().compareTo(time)!=0){//=0 if are equals
                total = last.getTemp() +"?"
                        +last.getReadingdatetime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))+"?"
                        + last.getHum();
            }
            try (PrintWriter out = response.getWriter()) {
                out.println(total);
                out.flush();
            }catch (IOException e){}
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

    /**send actual read of temp and hum*/
    public static void gettemp(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;
        TempReader tempReader = new TempReader();
        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            Variabili bg = BackgroundController.var;
            bg = tempReader.getRead(bg);
            String total = bg.getActualtemp() + "&deg; <b>HUM:</b> " + bg.getActualhum() + "&percnt; - AT "
                    + bg.getLettura().getReadingdatetime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            try (PrintWriter out = response.getWriter()) {
                out.println(total);
                out.flush();
            }catch (IOException e){}
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**first call for load actual temp page*/
    public static void gettempview(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;
        LoggedUser cookieLoggedUser;
        Utente loggedUser;
        String uniqid;
        DAOFactory daoFactory = null;
        try {
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();
            TempsDAO tempsdao = daoFactory.getTempsDao();
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            cookieLoggedUser = loggedUserDAO.find();
            cookieLoggedUser.setUniqid(loggedUserDAO.identify());
            UtenteDAO utenteDAO = daoFactory.getUserDAO();
            loggedUser = utenteDAO.findByUsername(cookieLoggedUser.getUsername());
            if(!utenteDAO.findLoginSession(loggedUser.getCodice(),cookieLoggedUser)) throw new IllegalAccessException("<h1>You must be logged in to see this data. Go away.</h1>");
            Lettura[] days = tempsdao.Readtoday(LocalDate.now());
            Lettura[] meds = tempsdao.Readmeds(7);
            daoFactory.commitTransaction();
            
            request.setAttribute("days", days);
            request.setAttribute("meds", meds);
            request.setAttribute("loggedOn",cookieLoggedUser!=null);
            request.setAttribute("loggedUser", cookieLoggedUser);
            request.setAttribute("viewUrl", "actualtemp");
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
}