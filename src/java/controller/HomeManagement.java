package controller;

import backgrounder.Variabili;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import model.dao.BackgroundDAO;
import model.dao.DAOFactory;
import model.dao.TempsDAO;
import model.mo.Lettura;

import services.config.Configuration;

import model.session.mo.LoggedUser;
import model.session.dao.SessionDAOFactory;
import model.session.dao.LoggedUserDAO;

public class HomeManagement {
    
    private HomeManagement() {}
    
    public static void view(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;
        LoggedUser loggedUser;
        //Logger logger = LogService.getApplicationLogger();
        
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
    
    public static void getmeds(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;
        LoggedUser loggedUser;
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
            
            String total = "";
            
            try (PrintWriter out = response.getWriter()) {
                out.println(total);
                out.flush();
            }catch (IOException e){}
            
        daoFactory.commitTransaction();
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
    
    public static void gettemp(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;
        LoggedUser loggedUser;
        //Logger logger = LogService.getApplicationLogger();
        
        String nowreaded = "";
        Variabili var = new Variabili();
        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            loggedUser = loggedUserDAO.find();
            boolean done = true;
            while(done){
                String command = "/home/pi/Desktop/srvrasp/only_one_temp.py";
                Process p = new ProcessBuilder(command).start();
                try {
                    p = Runtime.getRuntime().exec(command);
                } catch (IOException e) { throw new RuntimeException(e); }
                BufferedReader fromexec = new BufferedReader(new InputStreamReader(p.getInputStream()));
                nowreaded = fromexec.readLine();
                double actualhumdou = Double.parseDouble(nowreaded.substring(5));
                if (actualhumdou < 100.5) {
                    var.setActualhumdbl(actualhumdou);
                    var.setActualtemphum(nowreaded);
                    var.setActualtemp(nowreaded.substring(0, 4));
                    var.setActualhum(nowreaded.substring(5));
                    var.setActualtempdbl(Double.parseDouble(var.getActualtemp()));
                    done = false;
                }
            }
            String total = var.getActualtemp() + " HUM: " + var.getActualhum();
            
            try (PrintWriter out = response.getWriter()) {
                out.println(total);
                out.flush();
            }catch (IOException e){}
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void gettempview(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAOFactory;
        LoggedUser loggedUser;
        DAOFactory daoFactory = null;
        //Logger logger = LogService.getApplicationLogger();
        try {
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();
            TempsDAO tempsdao = daoFactory.getTempsDao();
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            loggedUser = loggedUserDAO.find();
            
            double min = 0;
            double max = 0;
            Lettura[] days = tempsdao.Readtoday(LocalDate.now());
            if(days.length > 0){
                min = days[0].getTemp();
                max = min;
                for(Lettura extr : days){
                    if(extr.getTemp()<min) min = extr.getTemp();
                    if(extr.getTemp()>max) max = extr.getTemp();
                }
            }
            if((max-min)<1){
                max += 0.1;
                min -= 0.1;
            }else{
                max += 0.5;
                min -= 0.5;
            }
            Lettura[] meds = tempsdao.Readmeds(7);
            daoFactory.commitTransaction();
            
            request.setAttribute("days", days);
            request.setAttribute("min", min);
            request.setAttribute("max", max);
            request.setAttribute("meds", meds);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "actualtemp");
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