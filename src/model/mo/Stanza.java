package model.mo;

import java.time.LocalTime;

public class Stanza {

    private String nome;
    private int id;
    private Lettura actualRead;
    private double maxTemp;
    private double minTemp;
    private double absoluteMin;
    private LocalTime[][] turnOnOffTimes;

    public Stanza(){}

    public Stanza(String name, double maxTemp, double minTemp, double absoluteMin, int id){
        this.nome = name;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.absoluteMin = absoluteMin;
        this.id = id;
    }

    public Stanza(String name, double maxTemp, double minTemp, double absoluteMin){
        this.nome = name;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.absoluteMin = absoluteMin;
    }

    public String getNome() { return nome; }

    public void setNome(String name) { this.nome = name; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public Lettura getActualRead() { return actualRead; }

    public void setActualRead(Lettura actualRead) { this.actualRead = actualRead; }

    public double getMaxTemp() { return maxTemp; }

    public void setMaxTemp(double maxTemp) { this.maxTemp = maxTemp; }

    public double getMinTemp() { return minTemp; }

    public void setMinTemp(double minTemp) { this.minTemp = minTemp; }

    public LocalTime[][] getTurnOnOffTimes() { return turnOnOffTimes; }

    public void setTurnOnOffTimes(LocalTime[][] turnOnOffTimes) { this.turnOnOffTimes = turnOnOffTimes; }

    public double getAbsoluteMin() { return absoluteMin; }

    public void setAbsoluteMin(double absoluteMin) { this.absoluteMin = absoluteMin; }
}
