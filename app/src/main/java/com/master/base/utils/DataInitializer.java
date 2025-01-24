package com.master.base.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.master.base.dao.AgentDAO;
import com.master.base.dao.ClientDAO;
import com.master.base.dao.ReleveDAO;
import com.master.base.database.DatabaseManager;

import java.util.Random;

public class DataInitializer {
    private final DatabaseManager dbManager;
    public DataInitializer(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void initializeData() {
        if (isDataAlreadyInitialized()) return;

        AgentDAO agentDAO = dbManager.getAgentDAO();
        ClientDAO clientDAO = dbManager.getClientDAO();
        ReleveDAO releveDAO = dbManager.getReleveDAO();

        long[] agentIds = new long[5];
        String[] agentNames = {"Martin Dupont", "Julie Martin", "Thomas Bernard", "Sophie Petit", "Lucas Dubois"};

        for (int i = 0; i < agentNames.length; i++) {
            agentIds[i] = agentDAO.insertAgent(agentNames[i]);
        }

        String[] clientNames = {"Entreprise A", "Société B", "Commerce C", "Boutique D", "Magasin E",
                "Restaurant F", "Cabinet G", "Clinique H", "Garage I", "Bureau J"};

        for (String clientName : clientNames) {
            long agentId = agentIds[new Random().nextInt(agentIds.length)];
            long clientId = clientDAO.insertClient(clientName, agentId);

            // Valeurs croissantes fixes
            double[] valeurs = {125.50, 225.75, 350.25};
            // Dates croissantes
            String[] dates = {"2024-01-15", "2024-02-15", "2024-03-15"};

            for (int i = 0; i < 3; i++) {
                releveDAO.insertReleve(dates[i], valeurs[i], clientId);
            }
        }
    }

    private boolean isDataAlreadyInitialized() {
        Cursor cursor = dbManager.getDatabase().rawQuery("SELECT COUNT(*) FROM agent", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }
}