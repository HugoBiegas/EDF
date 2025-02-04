package com.master.base.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.master.base.R;
import com.master.base.database.DatabaseManager;
import com.master.base.models.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView agentListView;
    private DatabaseManager dbManager;
    private ArrayAdapter<Agent> agentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = DatabaseManager.getInstance(this);
        setupViews();
        loadAgents();
    }

    private void setupViews() {
        agentListView = findViewById(R.id.agentListView);
        findViewById(R.id.addAgentButton).setOnClickListener(v -> showAddAgentDialog());
        agentListView.setAdapter(
                new ArrayAdapter<Agent>(this, R.layout.agent_list_item, R.id.agentName, new ArrayList<>()) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        Button editButton = view.findViewById(R.id.editAgentButton);
                        ImageButton deleteButton = view.findViewById(R.id.deleteAgentButton);

                        Agent agent = getItem(position);

                        view.setClickable(false);

                        editButton.setOnClickListener(v -> {
                            v.setTag("edit_button_clicked");
                            showEditAgentDialog(agent);
                        });

                        deleteButton.setOnClickListener(v -> showDeleteAgentDialog(agent));

                        return view;
                    }
                }
        );
        agentListView.setOnItemClickListener((parent, view, position, id) -> {
            Agent agent = (Agent) parent.getItemAtPosition(position);
            Intent intent = new Intent(this, ClientActivity.class);
            intent.putExtra("AGENT_ID", agent.getId());
            startActivity(intent);
        });
    }

    private void showDeleteAgentDialog(Agent agent) {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer l'agent")
                .setMessage("Voulez-vous vraiment supprimer cet agent ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    dbManager.getAgentDAO().deleteAgent(agent.getId());
                    loadAgents();
                })
                .setNegativeButton("Non", null)
                .show();
    }

    private void loadAgents() {
        List<Agent> agents = dbManager.getAgentDAO().getAllAgents();
        agentAdapter = (ArrayAdapter<Agent>) agentListView.getAdapter();
        agentAdapter.clear();
        agentAdapter.addAll(agents);
    }

    private void showAddAgentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_agent, null);
        EditText nameInput = view.findViewById(R.id.agentNameInput);

        builder.setView(view)
                .setTitle("Ajouter un agent")
                .setPositiveButton("Ajouter", (dialog, id) -> {
                    String nom = nameInput.getText().toString();
                    dbManager.getAgentDAO().insertAgent(nom);
                    loadAgents();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void showEditAgentDialog(Agent agent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_agent, null);
        EditText nameInput = view.findViewById(R.id.agentNameInput);
        nameInput.setText(agent.getNom());

        builder.setView(view)
                .setTitle("Modifier l'agent")
                .setPositiveButton("Modifier", (dialog, id) -> {
                    agent.setNom(nameInput.getText().toString());
                    dbManager.getAgentDAO().updateAgent(agent);
                    loadAgents();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}