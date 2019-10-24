package loginner;

import static controller.PasswordHash.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.DAOFactory;
import model.dao.UtenteDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Utente;
import model.session.dao.LoggedUserDAO;
import model.session.dao.SessionDAOFactory;
import model.session.mo.LoggedUser;
import services.config.*;
import services.logservice.LogService;

@MultipartConfig(fileSizeThreshold=1024*1024*5, // 5MB
        maxFileSize=1024*1024*10,      // 10MB
        maxRequestSize=1024*1024*50)

public class RegisterManagement{

    private RegisterManagement(){
    }
    
    public static void registerView(HttpServletRequest request, HttpServletResponse response) {
        
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
            request.setAttribute("viewUrl", "RegistrazioneUtente");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            throw new RuntimeException(e);
        }
    }
    
    /**register new user in the system*/
    public static void insert(HttpServletRequest request, HttpServletResponse response){
        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        LoggedUser loggedUser;
        String applicationMessage;
        Boolean success;
        Logger logger = LogService.getApplicationLogger();
        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            loggedUser = loggedUserDAO.find();
            
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();
            UtenteDAO utenteDAO = daoFactory.getUserDAO();
            String email = request.getParameter("email");
            String pwd = request.getParameter("pwd");
            String username = request.getParameter("username");
            String digconv = passwordHashPBKDF2(pwd);
            Utente toinsert = new Utente(email, username, digconv);
            try {
                utenteDAO.insert(toinsert);
                applicationMessage = "<h3>Grazie di esserti registrato</h3>"
                        + "<h3>Benvenuto "+ email +"</h3>";
                success = true;
            } catch (DuplicatedObjectException e) {
                applicationMessage = "<h3>Attenzione utente già esistente!</h3>"
                        + "<h3>Esiste già un utente con indirizzo mail: "+ email +" prova ad usarne uno diverso</h3>"
                        + "<h4>Se hai qualche problema durante la registrazione clicca nel men&ugrave in alto su aiuto</h4>";
                success = false;
                logger.log(Level.INFO, "Tentativo di inserimento di contatto già esistente");
            }
            daoFactory.commitTransaction();
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("loggedOn", loggedUser!=null);
            request.setAttribute("success", success);
            request.setAttribute("email", email);
            request.setAttribute("viewUrl", "response");
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) {
                    daoFactory.rollbackTransaction();
                }
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);
            
        } finally {
            try {
                if (daoFactory != null) {
                    daoFactory.closeTransaction();
                }
            } catch (Throwable t) {
            }
        }
        
    }
    
    /*
    public static void delete(HttpServletRequest request, HttpServletResponse response) {
        
        SessionDAOFactory sessionDAOFactory;
        LoggedUser loggedUser;
        DAOFactory daoFactory = null;
        String msg;
        Logger logger = LogService.getApplicationLogger();
        
        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            loggedUser = loggedUserDAO.find();
            
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();
            
            UtenteDAO utenteDAO = daoFactory.getUserDAO();
            
            if(loggedUser!=null && loggedUser.getTipo()==1){
                Utente u = new Utente();
                u.setEmail(loggedUser.getMail());
                utenteDAO.delete(u);
                msg = "<h3>Utente eliminato TOTALMENTE con successo</h3>"
                        + "<br>"
                        + "<h3>Ci dispiace che tu abbia fatto questo. </h3>"
                        + "<br>"
                        + "<h4>Se vuoi dirci il perch&egrave; di questa tua scelta puoi usare la sezione di aiuto per scrivere un messaggio. Grazie.</h4>"
                        + "<br>";
                
                loggedUserDAO.destroy();
                
                daoFactory.commitTransaction();
                
                request.setAttribute("loggedOn",false);
                request.setAttribute("loggedUser", null);
                request.setAttribute("applicationMessage", msg);
                request.setAttribute("viewUrl", "response");
            }else{
                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("viewUrl", "home");
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) {
                    daoFactory.rollbackTransaction();
                }
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);
            
        } finally {
            try {
                if (daoFactory != null) {
                    daoFactory.closeTransaction();
                }
            } catch (Throwable t) {
            }
        }
        
    }
    
    
    public static void myOrdersView(HttpServletRequest request, HttpServletResponse response) {
        
        SessionDAOFactory sessionDAOFactory;
        LoggedUser loggedUser;
        DAOFactory daoFactory = null;
        Logger logger = LogService.getApplicationLogger();
        
        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            loggedUser = loggedUserDAO.find();
            
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();
            
            UtenteDAO utenteDAO = daoFactory.getUserDAO();
            OrdineDAO ordineDAO = daoFactory.getOrdineDAO();
            RistoranteDAO ristoranteDAO = daoFactory.getRistoranteDAO();
            QuestionarioDAO questDAO = daoFactory.getQuestionarioDAO();
            
            if(loggedUser!=null && loggedUser.getTipo()==1){
                Utente utente = utenteDAO.findByUserMail(loggedUser.getMail());
                List<Ordine> orders = ordineDAO.findByUtente(utente);
                Ordine[] ords = new Ordine[orders.size()];
                ords = orders.toArray(ords);
                
                for(int i=0; i<ords.length; i++){
                    Ristorante rs = ristoranteDAO.findByRistoranteCodShort(ords[i].getRistorante().getCodice());
                    ords[i].setRistorante(rs);
                    if(questDAO.findCodiceOrd(ords[i].getCodice())){
                        Questionario quest = new Questionario();
                        quest.setUtente(utente);
                        ords[i].setQuestionario(quest);
                    }
                }
                
                daoFactory.commitTransaction();
                
                request.setAttribute("utente", utente);
                request.setAttribute("ord", ords);
                request.setAttribute("loggedOn", true);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("viewUrl", "gestioneOrdiniUser");
            }else{
                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("viewUrl", "home");
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) {
                    daoFactory.rollbackTransaction();
                }
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);
            
        } finally {
            try {
                if (daoFactory != null) {
                    daoFactory.closeTransaction();
                }
            } catch (Throwable t) {
            }
        }
        
    }
    
    
    public static void insertQuestView(HttpServletRequest request, HttpServletResponse response) {
        
        SessionDAOFactory sessionDAOFactory;
        LoggedUser loggedUser;
        Logger logger = LogService.getApplicationLogger();
        
        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            loggedUser = loggedUserDAO.find();
            
            long codice=Long.parseLong(request.getParameter("codord"));
            
            request.setAttribute("loggedOn", loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("ordine", codice);
            request.setAttribute("viewUrl", "Questionario");
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            throw new RuntimeException(e);
        }
    }
    
    
    public static void insertQuestionario(HttpServletRequest request, HttpServletResponse response){
        
        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        LoggedUser loggedUser;
        String applicationMessage;
        Boolean success;
        
        Logger logger = LogService.getApplicationLogger();
        
        try {
            sessionDAOFactory = SessionDAOFactory.getSesssionDAOFactory(Configuration.SESSION_IMPL);
            sessionDAOFactory.initSession(request, response);
            
            LoggedUserDAO loggedUserDAO = sessionDAOFactory.getLoggedUserDAO();
            loggedUser = loggedUserDAO.find();
            
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
            daoFactory.beginTransaction();
            QuestionarioDAO questionarioDAO = daoFactory.getQuestionarioDAO();
            UtenteDAO utenteDao = daoFactory.getUserDAO();
            OrdineDAO ordineDAO = daoFactory.getOrdineDAO();
            
            if(loggedUser!=null && loggedUser.getTipo()==1){
                Utente user=utenteDao.findByUserMail(loggedUser.getMail());
                
                double qualita = Double.parseDouble(request.getParameter("group1"));
                double adeguatezza = Double.parseDouble(request.getParameter("group2"));
                double punt = 0;
                if(!request.getParameter("group3").equalsIgnoreCase("on")){
                    punt = Double.parseDouble(request.getParameter("group3"));
                }
                double correttezza = Double.parseDouble(request.getParameter("group4"));
                double cortesia = Double.parseDouble(request.getParameter("group5"));
                double scelta = Double.parseDouble(request.getParameter("group7"));
                Ordine ordine = ordineDAO.findByCodice(Long.parseLong(request.getParameter("ordine")));
                
                questionarioDAO.insert(
                        qualita,
                        adeguatezza,
                        correttezza,
                        punt,
                        cortesia,
                        scelta,
                        user,
                        ordine
                );
                
                applicationMessage = "<h3>Grazie per aver compilato il questionario</h3>";
                success = true;
                
                daoFactory.commitTransaction();
                
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("loggedOn", true);
                request.setAttribute("success", success);
                request.setAttribute("ordine", ordine);
                request.setAttribute("viewUrl", "QuestionarioCompletato");
                request.setAttribute("loggedUser", loggedUser);
                
            }else{
                
                daoFactory.commitTransaction();
                
                request.setAttribute("loggedOn", loggedUser!=null);
                request.setAttribute("viewUrl", "home");
                request.setAttribute("loggedUser", loggedUser);
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) {
                    daoFactory.rollbackTransaction();
                }
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);
            
        } finally {
            try {
                if (daoFactory != null) {
                    daoFactory.closeTransaction();
                }
            } catch (Throwable t) {
            }
        }
        
    }
*/
}
