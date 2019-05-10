package backgrounder;

public class Variabili {
    
    private boolean ison = false;
    private boolean manuale = false;
    private boolean totaloff = false;
    private boolean tempcmd = false;
    private boolean isrunning = false;
    private String actualtemphum = null;
    private String actualtemp = null;
    private String actualhum = null;
    private double actualtempdbl;
    private double actualhumdbl;
    private final String turnoff = "python turnoff.py";
    private final String turnon = "python turnon.py";
    //private final String[] week = {"lun","mar","mer","gio","ven","sab","dom"};
    private final String[] week = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
    private short reboot;
    
    public double getActualhumdbl() {
        return actualhumdbl;
    }
    public void setActualhumdbl(double actualhumdbl) {
        this.actualhumdbl = actualhumdbl;
    }
    public boolean getIson() {
        return ison;
    }
    public void setIson(boolean ison) {
        this.ison = ison;
    }
    public boolean getManuale() {
        return manuale;
    }
    public void setManuale(boolean manuale) {
        this.manuale = manuale;
    }
    public boolean getTotaloff() {
        return totaloff;
    }
    public void setTotaloff(boolean totaloff) {
        this.totaloff = totaloff;
    }
    public boolean getTempcmd() {
        return tempcmd;
    }
    public void setTempcmd(boolean tempcmd) {
        this.tempcmd = tempcmd;
    }
    public boolean getIsrunning() {
        return isrunning;
    }
    public void setIsrunning(boolean isrunning) {
        this.isrunning = isrunning;
    }
    public String getActualtemphum() {
        return actualtemphum;
    }
    public void setActualtemphum(String actualtemphum) {
        this.actualtemphum = actualtemphum;
    }
    public String getActualtemp() {
        return actualtemp;
    }
    public void setActualtemp(String actualtemp) {
        this.actualtemp = actualtemp;
    }
    public String getActualhum() {
        return actualhum;
    }
    public void setActualhum(String actualhum) {
        this.actualhum = actualhum;
    }
    public double getActualtempdbl() {
        return actualtempdbl;
    }
    public void setActualtempdbl(double actualtempdbl) {
        this.actualtempdbl = actualtempdbl;
    }
    public String getTurnoff() {
        return turnoff;
    }
    public String getTurnon() {
        return turnon;
    }
    public String[] getWeek() {
        return week;
    }
    public short getReboot() {
        return reboot;
    }
    public void setReboot(short reboot) {
        this.reboot = reboot;
    }
    
}
