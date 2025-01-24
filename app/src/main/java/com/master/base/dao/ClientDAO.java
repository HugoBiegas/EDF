package com.master.base.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.master.base.models.Client;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    private final SQLiteDatabase db;

    public ClientDAO(SQLiteDatabase db) {
        this.db = db;
    }

    //INSERT
    public long insertClient(String nom, long agentId) {
        ContentValues values = new ContentValues();
        values.put("nom", nom);
        values.put("agent_id", agentId);
        return db.insert("client", null, values);
    }

    //UPDATE
    public int updateClient(Client client) {
        ContentValues values = new ContentValues();
        values.put("nom", client.getNom());
        values.put("agent_id", client.getAgentId());
        return db.update("client", values, "id = ?",
                new String[]{String.valueOf(client.getId())});
    }

    //DELETE
    public int deleteClient(long id) {
        return db.delete("client", "id = ?",
                new String[]{String.valueOf(id)});
    }


    //GET CLIENTS BY AGENT
    public List<Client> getClientsByAgent(long agentId) {
        List<Client> clients = new ArrayList<>();
        Cursor cursor = db.query("client", null, "agent_id = ?",
                new String[]{String.valueOf(agentId)}, null, null, null);
        while (cursor.moveToNext()) {
            Client client = new Client();
            client.setId(cursor.getLong(0));
            client.setNom(cursor.getString(1));
            client.setAgentId(cursor.getLong(2));
            clients.add(client);
        }
        return clients;
    }
}
