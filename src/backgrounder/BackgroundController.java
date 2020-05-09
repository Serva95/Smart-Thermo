package backgrounder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.*;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import controller.TempReader;
import model.dao.BackgroundDAO;
import model.dao.DAOFactory;
import services.config.Configuration;

@WebListener
public class BackgroundController implements ServletContextListener {
    
    private ScheduledExecutorService scheduler;
    private TempReader tempReader = new TempReader();
    public static Variabili var = new Variabili();

    @Override
    public void contextInitialized(ServletContextEvent event) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new SomeDailyJob(), 0, 1, TimeUnit.DAYS);
        //scheduler.scheduleAtFixedRate(new SomeHourlyJob(), 0, 1, TimeUnit.HOURS);
        
        scheduler.scheduleAtFixedRate(new SomeQuarterlyJob(), 0, 15, TimeUnit.MINUTES);
        //scheduler.scheduleAtFixedRate(new SomeQuarterlyJob(), 0, 15, TimeUnit.SECONDS);
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }
    
    public class SomeDailyJob implements Runnable {
        @Override
        public void run() {
            DAOFactory daoFactory = null;
            try{
                daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
                daoFactory.beginTransaction();
                BackgroundDAO backdao = daoFactory.getBackgroundDao();
                LocalDate dayToMedify = LocalDate.now().minusDays(5);
                backdao.medify(dayToMedify);
                daoFactory.commitTransaction();
            } catch (Exception e) {
                e.printStackTrace();
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
    
    /*public class SomeHourlyJob implements Runnable {
        @Override
        public void run() {
            // Do your hourly job here.
        }
    }*/
    
    public class SomeQuarterlyJob implements Runnable {
        @Override
        public void run() {
            DAOFactory daoFactory = null;
            try{
                daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
                var = tempReader.getRead(var);
                daoFactory.beginTransaction();
                BackgroundDAO backdao = daoFactory.getBackgroundDao();
                boolean ok = backdao.insert(var);
                if (!ok){ throw new RuntimeException("reading or database error"); }
                daoFactory.commitTransaction();
            } catch (Exception e) {
                e.printStackTrace();
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
}