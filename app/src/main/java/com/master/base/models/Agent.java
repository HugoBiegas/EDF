package com.master.base.models;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Agent {
    private long id;
    private String nom;
    private List<Client> clients;

    public Agent() {
        this.clients = new ArrayList<>();
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public List<Client> getClients() { return clients; }
    public void setClients(List<Client> clients) { this.clients = clients; }

    @Override
    public @NotNull String toString() {
        return nom;
    }
}
