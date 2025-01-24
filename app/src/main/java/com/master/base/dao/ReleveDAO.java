package com.master.base.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.master.base.models.Releve;

import java.util.ArrayList;
import java.util.List;

public class ReleveDAO {
    private final SQLiteDatabase db;

    public ReleveDAO(SQLiteDatabase db) {
        this.db = db;
    }

    //INSERT
    public long insertReleve(String date, double valeur, long clientId) {
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("valeur", valeur);
        values.put("client_id", clientId);
        return db.insert("releve", null, values);
    }

    //UPDATE
    public int updateReleve(Releve releve) {
        ContentValues values = new ContentValues();
        values.put("date", releve.getDate());
        values.put("valeur", releve.getValeur());
        values.put("client_id", releve.getClientId());
        return db.update("releve", values, "id = ?",
                new String[]{String.valueOf(releve.getId())});
    }

    //DELETE
    public int deleteReleve(long id) {
        return db.delete("releve", "id = ?",
                new String[]{String.valueOf(id)});
    }


    //GET RELEVES BY CLIENT
    public List<Releve> getRelevesByClient(long clientId) {
        List<Releve> releves = new ArrayList<>();
        Cursor cursor = db.query("releve", null, "client_id = ?",
                new String[]{String.valueOf(clientId)}, null, null, "date DESC");
        while (cursor.moveToNext()) {
            Releve releve = new Releve();
            releve.setId(cursor.getLong(0));
            releve.setDate(cursor.getString(1));
            releve.setValeur(cursor.getDouble(2));
            releve.setClientId(cursor.getLong(3));
            releves.add(releve);
        }
        return releves;
    }
}
