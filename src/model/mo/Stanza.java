package model.mo;

import java.time.LocalTime;

public class Stanza {

    private String name;
    private int id;
    private Lettura actualRead;
    private double maxTemp;
    private double minTemp;
    private LocalTime[] turnOnTimes;
    private LocalTime[] turnOffTimes;

    public Stanza(){}

    public Stanza(String name, int id){
        this.name = name;
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public Lettura getActualRead() { return actualRead; }

    public void setActualRead(Lettura actualRead) { this.actualRead = actualRead; }

    public double getMaxTemp() { return maxTemp; }

    public void setMaxTemp(double maxTemp) { this.maxTemp = maxTemp; }

    public double getMinTemp() { return minTemp; }

    public void setMinTemp(double minTemp) { this.minTemp = minTemp; }

    public LocalTime[] getTurnOnTimes() { return turnOnTimes; }

    public void setTurnOnTimes(LocalTime[] turnOnTimes) { this.turnOnTimes = turnOnTimes; }

    public LocalTime[] getTurnOffTimes() { return turnOffTimes; }

    public void setTurnOffTimes(LocalTime[] turnOffTimes) { this.turnOffTimes = turnOffTimes; }


}
