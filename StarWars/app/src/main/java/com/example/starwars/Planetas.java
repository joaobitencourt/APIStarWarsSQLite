package com.example.starwars;

public class Planetas {
    private int idPlanet;
    private String planete;
    private String climate;
    private  String ground;

    public Planetas(String planete, String climate, String ground) {
        this.planete = planete;
        this.climate = climate;
        this.ground = ground;
    }

    public Planetas(){
    }

    public int getIdPlanet() {
        return idPlanet;
    }

    public void setIdPlanet(int idPlanet) {
        this.idPlanet = idPlanet;
    }

    public String getPlanete() {
        return planete;
    }

    public void setPlanete(String planete) {
        this.planete = planete;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getGround() {
        return ground;
    }

    public void setGround(String ground) {
        this.ground = ground;
    }
}
