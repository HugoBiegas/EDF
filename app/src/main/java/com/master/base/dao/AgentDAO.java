package com.master.base.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.master.base.models.Agent;

import java.util.ArrayList;
import java.util.List;

public class AgentDAO {
    private final SQLiteDatabase db;

    public AgentDAO(SQLiteDatabase db) {
        this.db = db;
    }

    // CREATE
    public long insertAgent(String nom) {
        ContentValues values = new ContentValues();
        values.put("nom", nom);
        return db.insert("agent", null, values);
    }

    // READ
    public Agent getAgent(long id) {
        Cursor cursor = db.query("agent", null, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            Agent agent = new Agent();
            agent.setId(cursor.getLong(0));
            agent.setNom(cursor.getString(1));
            return agent;
        }
        return null;
    }

    // UPDATE
    public int updateAgent(Agent agent) {
        ContentValues values = new ContentValues();
        values.put("nom", agent.getNom());
        return db.update("agent", values, "id = ?",
                new String[]{String.valueOf(agent.getId())});
    }

    // DELETE
    public int deleteAgent(long id) {
        return db.delete("agent", "id = ?",
                new String[]{String.valueOf(id)});
    }

    //GET ALL
    public List<Agent> getAllAgents() {
        List<Agent> agents = new ArrayList<>();
        Cursor cursor = db.query("agent", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Agent agent = new Agent();
            agent.setId(cursor.getLong(0));
            agent.setNom(cursor.getString(1));
            agents.add(agent);
        }
        cursor.close();
        return agents;
    }
}
