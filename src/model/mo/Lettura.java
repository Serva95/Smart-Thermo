package model.mo;

import java.time.LocalDateTime;

public class Lettura {
    
    private LocalDateTime readingdatetime;
    private double temp;
    private double hum;

    public Lettura(){}

    public Lettura(LocalDateTime readingdatetime, double temp, double hum){
        this.readingdatetime = readingdatetime;
        this.temp = temp;
        this.hum = hum;
    }

    public LocalDateTime getReadingdatetime() {
        return readingdatetime;
    }

    public void setReadingdatetime(LocalDateTime readingdatetime) {
        this.readingdatetime = readingdatetime;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getHum() {
        return hum;
    }

    public void setHum(double hum) {
        this.hum = hum;
    }
    
    
    
}
