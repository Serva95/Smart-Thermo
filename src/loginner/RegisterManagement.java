package loginner;

import static controller.PasswordHash.passwordhash;

import java.io.File;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;

import java.security.MessageDigest;

import java.time.LocalDate;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import javax.xml.bind.DatatypeConverter;

import model.dao.DAOFactory;
import model.dao.UtenteDAO;
import model.dao.exception.DuplicatedObjectException;

import model.mo.Utente;

import model.session.dao.LoggedUserDAO;
import model.session.dao.SessionDAOFactory;
import model.session.mo.LoggedUser;

import services.config.Configuration;
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
            String digconv = passwordhash(pwd);
            Utente toinsert = new Utente(email, username, digconv);
            try {
                utenteDAO.insert(toinsert);
                applicationMessage = "<h3>Thanks for register</h3>"
                        + "<h3>Welcome "+ email +"</h3>";
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
    public static void profileView(HttpServletRequest request, HttpServletResponse response) {
        
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
            
            if(loggedUser!=null && loggedUser.getTipo()==1){
                Utente utente = utenteDAO.findByUserMail(loggedUser.getMail());
                
                daoFactory.commitTransaction();
                
                request.setAttribute("utente", utente);
                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("viewUrl", "profiloUtente");
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
    
    
    public static void profileEdit(HttpServletRequest request, HttpServletResponse response) {
        
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
            
            if(loggedUser!=null && loggedUser.getTipo()==1){
                
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
    
    
    public static void update(HttpServletRequest request, HttpServletResponse response){
        
        SessionDAOFactory sessionDAOFactory;
        DAOFactory daoFactory = null;
        LoggedUser loggedUser;
        String applicationMessage = null;
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
            
            if(loggedUser!=null && loggedUser.getTipo()==1){
                Utente au = utenteDAO.findByUserMail(loggedUser.getMail());
                Utente utente = new Utente();
                
                long cod = Long.parseLong(request.getParameter("cod"));
                utente.setCodice(cod);
                
                String email = request.getParameter("email");
                String pwd = request.getParameter("pwd");
                String phone = request.getParameter("phone");
                String campanello = request.getParameter("campanello");
                String address = request.getParameter("address");
                String citta = request.getParameter("citta");
                String name = request.getParameter("name");
                String surname = request.getParameter("surname");
                String username = request.getParameter("username");
                LocalDate data = LocalDate.parse(request.getParameter("birthday"));
                
                int cap;
                try{
                    cap = Integer.parseInt(request.getParameter("cap"));
                }catch (NumberFormatException e){
                    cap=0;
                }
                
                boolean cambiamenti = false;
                
                if(au.getEmail().equalsIgnoreCase(email) || "".equals(email)){utente.setEmail(au.getEmail());
                }else{ utente.setEmail(email); cambiamenti = true;}
                
                utente.setCellulare((au.getCellulare().equalsIgnoreCase(phone) || "".equals(phone))? au.getCellulare(): phone);
                utente.setNome_campanello((au.getNome_campanello().equalsIgnoreCase(campanello) || "".equals(campanello))? au.getNome_campanello(): campanello);
                utente.setNome((au.getNome().equalsIgnoreCase(name) || "".equals(name))? au.getNome(): name);
                utente.setCognome((au.getCognome().equalsIgnoreCase(surname) || "".equals(surname))? au.getCognome(): surname);
                utente.setUsername((au.getUsername().equalsIgnoreCase(username) || "".equals(username))? au.getUsername(): username);
                utente.setCittà((au.getCittà().equalsIgnoreCase(citta) || "".equals(citta))? au.getCittà(): citta);
                utente.setVia((au.getVia().equalsIgnoreCase(address) || "".equals(address))? au.getVia(): address);
                utente.setCap((au.getCap()==cap || cap == 0)? au.getCap(): cap);
                utente.setData_nascita((au.getData_nascita().isEqual(data))? au.getData_nascita(): data);
                
                if(!"".equals(pwd)){utente.setPassword(passwordhash(pwd)); cambiamenti = true;}else{utente.setPassword(au.getPassword());}
                
                Part filePart = request.getPart("profilepic");
                String foto = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                
                String picname;
                String nome_pic = utente.getEmail();
                MessageDigest todig = MessageDigest.getInstance("SHA1");
                byte[] pichash = todig.digest(nome_pic.getBytes(StandardCharsets.UTF_8));
                picname = DatatypeConverter.printHexBinary(pichash);
                
                String filename = System.getProperty("user.home")
                        + File.separatorChar + "Documenti"
                        + File.separatorChar + "NetBeansProjects"
                        + File.separatorChar + "eatit"
                        + File.separatorChar + "web"
                        + File.separatorChar + "pic"
                        + File.separatorChar
                        + picname + ".jpg";
                
                File file = new File(filename);
                utente.setUrlfoto(picname+".jpg");
                try {
                    utenteDAO.update(utente);
                    applicationMessage = "<h3>Hai aggiornato i tuoi dati con successo!</h3>";
                    success = true;
                } catch (DuplicatedObjectException e) {
                    applicationMessage = "<h3>La mail da te usata è già esistente per un altro utente riprova con una diversa</h3>";
                    success = false;
                    logger.log(Level.INFO, "Tentativo di update di user già esistente");
                }
                
                if(success){
                    File fileold = new File(System.getProperty("user.home")
                            + File.separatorChar + "Documenti"
                            + File.separatorChar + "NetBeansProjects"
                            + File.separatorChar + "eatit"
                            + File.separatorChar + "web"
                            + File.separatorChar + "pic"
                            + File.separatorChar
                            + au.getUrlfoto());
                    if(!"".equals(foto)){
                        try(InputStream fileContent = filePart.getInputStream()){
                            Files.copy(fileContent, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }
                        if(fileold.exists() && !fileold.getName().equalsIgnoreCase(picname+".jpg")){
                            fileold.delete();
                        }
                    }else{
                        if(!file.exists()){
                            if(!fileold.renameTo(file)){
                                throw new RuntimeException("errore rinomina file");
                            }
                            utente.setUrlfoto(foto);
                        }else{
                            utente.setUrlfoto(au.getUrlfoto());
                        }
                    }
                    
                    
                    
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
                    request.setAttribute("success", success);
                }else{
                    request.setAttribute("applicationMessage", applicationMessage);
                    request.setAttribute("loggedOn", loggedUser!=null);
                    request.setAttribute("success", success);
                    request.setAttribute("loggedUser", loggedUser);
                    request.setAttribute("email", utente.getEmail());
                    request.setAttribute("viewUrl", "response");
                }
            }else{
                
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("loggedOn", loggedUser!=null);
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
