package model.mo;

import java.time.LocalDateTime;

public class Sensore {
    private int id;
    private String location;
    private String nome;
    private double temperature;
    private LocalDateTime lectureTime;

    public Sensore(int id, String location, String name, double temperature, LocalDateTime lectureTime) {
        this.id = id;
        this.location = location;
        this.nome = name;
        this.temperature = temperature;
        this.lectureTime = lectureTime;
    }

    public Sensore(int id) { this.id = id; }

    public Sensore() {}

    /**getter and setter*/

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public double getTemperature() { return temperature; }

    public void setTemperature(double temperature) { this.temperature = temperature; }

    public LocalDateTime getLectureTime() { return lectureTime; }

    public void setLectureTime(LocalDateTime lectureTime) { this.lectureTime = lectureTime; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }
}
