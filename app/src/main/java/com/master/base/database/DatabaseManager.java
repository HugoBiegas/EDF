package com.master.base.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.master.base.dao.AgentDAO;
import com.master.base.dao.ClientDAO;
import com.master.base.dao.ReleveDAO;
import com.master.base.utils.DataInitializer;

public class DatabaseManager {
    private static DatabaseManager instance;
    private SQLiteDatabase db;
    private AgentDAO agentDAO;
    private ClientDAO clientDAO;
    private ReleveDAO releveDAO;

    private static final String CREATE_TABLE_AGENT =
            "CREATE TABLE IF NOT EXISTS agent (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT NOT NULL)";

    private static final String CREATE_TABLE_CLIENT =
            "CREATE TABLE IF NOT EXISTS client (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT NOT NULL, " +
                    "agent_id INTEGER, FOREIGN KEY(agent_id) REFERENCES agent(id))";

    private static final String CREATE_TABLE_RELEVE =
            "CREATE TABLE IF NOT EXISTS releve (id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT NOT NULL, " +
                    "valeur REAL NOT NULL, client_id INTEGER, FOREIGN KEY(client_id) REFERENCES client(id))";

    private DatabaseManager(Context context) {
        db = context.openOrCreateDatabase("Test2", Context.MODE_PRIVATE, null);
        createTables();
        initializeDAOs();
        initializeData();
    }

    private void createTables() {
        db.execSQL(CREATE_TABLE_AGENT);
        db.execSQL(CREATE_TABLE_CLIENT);
        db.execSQL(CREATE_TABLE_RELEVE);
    }

    private void initializeDAOs() {
        agentDAO = new AgentDAO(db);
        clientDAO = new ClientDAO(db);
        releveDAO = new ReleveDAO(db);
    }

    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context.getApplicationContext());
        }
        return instance;
    }

    public void initializeData() {
        DataInitializer dataInitializer = new DataInitializer(this);
        dataInitializer.initializeData();
    }

    public AgentDAO getAgentDAO() { return agentDAO; }
    public ClientDAO getClientDAO() { return clientDAO; }
    public ReleveDAO getReleveDAO() { return releveDAO; }
    public SQLiteDatabase getDatabase() { return db; }
}