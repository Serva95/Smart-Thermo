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
                
                /*String command = "/home/pi/Desktop/srvrasp/only_one_temp.py";
                Process p = new ProcessBuilder(command).start();
                try {
                    p = Runtime.getRuntime().exec(command);
                } catch (Exception e) { throw new RuntimeException(e); }
                BufferedReader fromexec = new BufferedReader(new InputStreamReader(p.getInputStream()));
                nowreaded = fromexec.readLine();

                //nowreaded = ((int)(Math.random() * 7) + 19)+".5 "+((int)(Math.random() * 9) + 45)+".9";

                var.setActualtemp(nowreaded.substring(0, 4));
                var.setActualhum(nowreaded.substring(5));
                double actualhumdou = Double.parseDouble(nowreaded.substring(5));
                double actualtempdou = Double.parseDouble(var.getActualtemp());
                if (actualhumdou < 101.1) {
                    var.setActualhumdbl(actualhumdou);
                    var.setActualtemphum(nowreaded);
                    var.setActualtempdbl(actualtempdou);
                }*/
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