package controller;

import backgrounder.Variabili;
import model.mo.Lettura;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

public class TempReader {

    public TempReader(){}

    private String command = "/home/pi/Desktop/srvrasp/only_one_temp.py";
    private String nowreaded = "";

    public synchronized Variabili getRead(Variabili var){
        boolean done = true;
        boolean isbefore = true;
        Lettura lettura = new Lettura();
        LocalDateTime now = LocalDateTime.now().minusMinutes(5);
        try {
            isbefore = var.getLettura().getReadingdatetime().isBefore(now);
        }catch (Exception e){}
        if(isbefore) {
            while (done) {
                Process p;/*
                try {
                    p = Runtime.getRuntime().exec(command);
                    BufferedReader fromexec = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    nowreaded = fromexec.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }*/

                nowreaded = ((int)(Math.random() * 7) + 19)+".5 "+((int)(Math.random() * 9) + 45)+".9";

                var.setActualhum(nowreaded.substring(5));
                var.setActualtemp(nowreaded.substring(0, 4));
                double actualhumdou = Double.parseDouble(var.getActualhum());
                double actualtempdou = Double.parseDouble(var.getActualtemp());
                if (actualhumdou < 99.9 && actualtempdou < 99.9) {
                    lettura.setHum(actualhumdou);
                    lettura.setTemp(actualtempdou);
                    lettura.setReadingdatetime(LocalDateTime.now());
                    var.setLettura(lettura);
                    var.setActualtemphum(nowreaded);
                    done = false;
                }
            }
        }
        return var;
    }
}
