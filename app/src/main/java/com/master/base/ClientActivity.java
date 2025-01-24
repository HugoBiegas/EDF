package com.master.base;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.master.base.database.DatabaseManager;
import com.master.base.models.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientActivity extends AppCompatActivity {
    private ListView clientListView;
    private DatabaseManager dbManager;
    private long agentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        agentId = getIntent().getLongExtra("AGENT_ID", -1);
        dbManager = DatabaseManager.getInstance(this);
        setupViews();
        loadClients();
    }

    private void setupViews() {
        clientListView = findViewById(R.id.clientListView);
        findViewById(R.id.addClientButton).setOnClickListener(v -> showAddClientDialog());
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        clientListView.setAdapter(
                new ArrayAdapter<Client>(this, R.layout.client_list_item, R.id.clientName, new ArrayList<>()) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        Button editButton = view.findViewById(R.id.editClientButton);
                        ImageButton deleteButton = view.findViewById(R.id.deleteClientButton);
                        Client client = getItem(position);

                        view.setClickable(false);

                        editButton.setOnClickListener(v -> {
                            v.setTag("edit_button_clicked");
                            showEditClientDialog(client);
                        });

                        deleteButton.setOnClickListener(v -> showDeleteClientDialog(client));

                        TextView clientName = view.findViewById(R.id.clientName);
                        clientName.setText(client.getNom());

                        return view;
                    }
                }
        );

        clientListView.setOnItemClickListener((parent, view, position, id) -> {
            Client client = (Client) parent.getItemAtPosition(position);
            Intent intent = new Intent(this, ReleveActivity.class);
            intent.putExtra("CLIENT_ID", client.getId());
            startActivity(intent);
        });
    }

    private void showDeleteClientDialog(Client client) {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer le client")
                .setMessage("Voulez-vous vraiment supprimer ce client ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    dbManager.getClientDAO().deleteClient(client.getId());
                    loadClients();
                })
                .setNegativeButton("Non", null)
                .show();
    }

    private void loadClients() {
        List<Client> clients = dbManager.getClientDAO().getClientsByAgent(agentId);
        ArrayAdapter<Client> adapter = (ArrayAdapter<Client>) clientListView.getAdapter();
        adapter.clear();
        adapter.addAll(clients);
    }

    private void showAddClientDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_client, null);
        EditText nameInput = view.findViewById(R.id.clientNameInput);

        builder.setView(view)
                .setTitle("Ajouter un client")
                .setPositiveButton("Ajouter", (dialog, id) -> {
                    String nom = nameInput.getText().toString();
                    dbManager.getClientDAO().insertClient(nom, agentId);
                    loadClients();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void showEditClientDialog(Client client) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_client, null);
        EditText nameInput = view.findViewById(R.id.clientNameInput);
        nameInput.setText(client.getNom());

        builder.setView(view)
                .setTitle("Modifier le client")
                .setPositiveButton("Modifier", (dialog, id) -> {
                    client.setNom(nameInput.getText().toString());
                    dbManager.getClientDAO().updateClient(client);
                    loadClients();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}