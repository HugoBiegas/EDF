package com.master.base.models;

import org.jetbrains.annotations.NotNull;

import static java.lang.Double.isNaN;

public class Releve {
    private long id;
    private String date;
    private double valeur;
    private long clientId;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public double getValeur() {return valeur;}
    public void setValeur(double valeur) { this.valeur = valeur; }
    public long getClientId() { return clientId; }
    public void setClientId(long clientId) { this.clientId = clientId; }

    @Override
    public @NotNull String toString() {
        return "Date: " + date + ", Valeur: " + valeur;
    }
}

