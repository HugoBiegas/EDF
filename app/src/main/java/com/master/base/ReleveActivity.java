package com.master.base;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.master.base.database.DatabaseManager;
import com.master.base.models.Releve;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReleveActivity extends AppCompatActivity {
    private ListView releveListView;
    private DatabaseManager dbManager;
    private long clientId;

    private List<Releve> releves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_releve);

        clientId = getIntent().getLongExtra("CLIENT_ID", -1);
        dbManager = DatabaseManager.getInstance(this);
        setupViews();
        loadReleves();
    }

    private void setupViews() {
        releveListView = findViewById(R.id.releveListView);
        findViewById(R.id.addReleveButton).setOnClickListener(v -> showAddReleveDialog());
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        releveListView.setAdapter(
                new ArrayAdapter<Releve>(this, R.layout.releve_list_item, R.id.releveInfo, new ArrayList<>()) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        Button editButton = view.findViewById(R.id.editReleveButton);
                        Releve releve = getItem(position);
                        editButton.setOnClickListener(v -> showEditReleveDialog(releve));
                        return view;
                    }
                }
        );
    }

    @SuppressLint("DefaultLocale")
    private void loadReleves() {
        releves = dbManager.getReleveDAO().getRelevesByClient(clientId);
        ArrayAdapter<Releve> adapter = (ArrayAdapter<Releve>) releveListView.getAdapter();
        adapter.clear();
        adapter.addAll(releves);

        // Calcul du montant
        TextView montantView = findViewById(R.id.montantTextView);
        TextView montantAReglerTextView = findViewById(R.id.montantAReglerTextView);
        if (releves.size() >= 2) {
            Releve dernierReleve = releves.get(0);
            Releve avantDernierReleve = releves.get(1);
            double difference = dernierReleve.getValeur() - avantDernierReleve.getValeur();
            montantView.setText(String.format("Montant du releve : %.2f", difference));
            montantAReglerTextView.setText(String.format("Montant à régler : %.2f €", (difference * 0.2516)));
        } else if (releves.size() == 1) {
            double difference = releves.get(0).getValeur();
            montantView.setText(String.format("Montant du releve : %.2f", difference));
            montantAReglerTextView.setText(String.format("Montant à régler : %.2f €", (difference * 0.2516)));
        } else {
            montantView.setText("Aucun relevé disponible");
        }
    }

    private void validateReleveValue(double newValue, List<Releve> existingReleves) {
        if (!existingReleves.isEmpty()) {
            double highestValue = existingReleves.stream()
                    .mapToDouble(Releve::getValeur)
                    .max()
                    .orElse(0);
            if (newValue < highestValue) {
                throw new IllegalArgumentException("La nouvelle valeur doit être supérieure ou égale à " + highestValue);}
        }
    }

    private void validateDate(String date, List<Releve> existingReleves) {
        if (!existingReleves.isEmpty()) {
            String latestDate = existingReleves.get(0).getDate();
            if (date.compareTo(latestDate) <= 0) {
                throw new IllegalArgumentException("La date doit être postérieure à " + latestDate);
            }
        }
    }

    private void showAddReleveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_releve, null);
        DatePicker datePicker = view.findViewById(R.id.releveDatePicker);
        EditText valeurInput = view.findViewById(R.id.releveValeurInput);

        if(!releves.isEmpty()){
            // Vérifier si date du dernier relevé = date du jour
            String dateDernierReleve = releves.get(0).getDate();
            String dateAujourdhui = String.format("%tF", Calendar.getInstance());

            if(dateDernierReleve.equals(dateAujourdhui)
                    || dateDernierReleve.compareTo(dateAujourdhui) > 0){
                Toast.makeText(this, "Un relevé existe déjà pour aujourd'hui ou plus tard. Veuillez le modifier.", Toast.LENGTH_LONG).show();
                return;
            }

            valeurInput.setText(releves.get(0).getValeur() + "");
        }

        builder.setView(view)
                .setTitle("Ajouter un relevé")
                .setPositiveButton("Ajouter", (dialog, id) -> {
                    try {
                        double valeur = Double.parseDouble(valeurInput.getText().toString());
                        List<Releve> existingReleves = dbManager.getReleveDAO().getRelevesByClient(clientId);
                        validateReleveValue(valeur, existingReleves);

                        @SuppressLint("DefaultLocale") String date = String.format("%d-%02d-%02d",
                                datePicker.getYear(),
                                datePicker.getMonth() + 1,
                                datePicker.getDayOfMonth());
                        validateDate(date, existingReleves);

                        dbManager.getReleveDAO().insertReleve(date, valeur, clientId);
                        loadReleves();
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void showEditReleveDialog(Releve releve) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_releve, null);
        DatePicker datePicker = view.findViewById(R.id.releveDatePicker);
        EditText valeurInput = view.findViewById(R.id.releveValeurInput);

        // Parse existing date
        String[] dateParts = releve.getDate().split("-");
        datePicker.updateDate(
                Integer.parseInt(dateParts[0]),
                Integer.parseInt(dateParts[1]) - 1,
                Integer.parseInt(dateParts[2])
        );
        valeurInput.setText(String.valueOf(releve.getValeur()));

        builder.setView(view)
                .setTitle("Modifier le relevé")
                .setPositiveButton("Modifier", (dialog, id) -> {
                    @SuppressLint("DefaultLocale") String date = String.format("%d-%02d-%02d",
                            datePicker.getYear(),
                            datePicker.getMonth() + 1,
                            datePicker.getDayOfMonth());
                    releve.setDate(date);
                    releve.setValeur(Double.parseDouble(valeurInput.getText().toString()));
                    dbManager.getReleveDAO().updateReleve(releve);
                    loadReleves();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }


}
