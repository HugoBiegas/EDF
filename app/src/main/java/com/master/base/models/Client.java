package com.master.base.models;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private long id;
    private String nom;
    private long agentId;
    private List<Releve> releves;

    public Client() {
        this.releves = new ArrayList<>();
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public long getAgentId() { return agentId; }
    public void setAgentId(long agentId) { this.agentId = agentId; }
    public List<Releve> getReleves() { return releves; }
    public void setReleves(List<Releve> releves) { this.releves = releves; }

    @Override
    public @NotNull String toString() {
        return nom;
    }


}

