package es.ljaraizc.bellotaclim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HistoricoActivity extends AppCompatActivity {

    private String id;
    private String email;
    private TextView haTVsocios, haTVreservas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        //Recibimos el id del usuario.
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        email = bundle.getString("email");

        haTVsocios = findViewById(R.id.haTVsocios);

        consultaNumeroSocios();

        haTVreservas = findViewById(R.id.haTVreservas);

        consultaReservasMaterial();


    }

    public void consultaNumeroSocios(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query = db.collection("Socios");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    //socios[0] = "" + snapshot.getCount();
                    haTVsocios.setText("Tenemos un total de " + snapshot.getCount() + " socios, de los cuales se han registrado ");
                    consultaNumeroSociosRegistrados();
                    //Log.d(TAG, "Count: " + snapshot.getCount());
                } else {
                    //Log.d(TAG, "Count failed: ", task.getException());
                }
            }
        });
    }

    public void consultaNumeroSociosRegistrados(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query = db.collection("Usuarios");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    //socios[0] = "" + snapshot.getCount();
                    haTVsocios.append(snapshot.getCount() + " en nuestra APP.");
                    //Log.d(TAG, "Count: " + snapshot.getCount());
                } else {
                    //Log.d(TAG, "Count failed: ", task.getException());
                }
            }
        });

    }

    public void consultaReservasMaterial(){

        //Posicion del array:
        //0 Arnés, 1 Casco, 2 Cuerda, 3 Pies de Gato.
        int[] materialUsado = {0, 0, 0, 0};

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("MaterialHistorico")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            String fecha;
                            String tipo;

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                tipo = document.getString("Tipo");

                                fecha = document.getString("Dia");
                                fecha = fecha.substring(0, fecha.length()-3);
                                if (fecha.equalsIgnoreCase("2023-11")){

                                    switch (tipo) {
                                        case "Arnés":
                                            materialUsado[0]++;
                                            break;
                                        case "Casco":
                                            materialUsado[1]++;
                                            break;
                                        case "Cuerda":
                                            materialUsado[2]++;
                                            break;
                                        case "Pies de Gato":
                                            materialUsado[3]++;
                                            break;
                                    }
                                }

                            }
                            haTVreservas.setText("Material reservado: \n"
                                    + materialUsado[0] + " arneses.\n"
                                    + materialUsado[1] + " cascos.\n"
                                    + materialUsado[2] + " cuerdas.\n"
                                    + materialUsado[3] + " pares de pies de gato.\n");
                            consultasReservasSalas();
                        }
                    }
                });

    }

    public void consultasReservasSalas(){

        //Posicion del array:
        //0 Bulder, 1 Cuerda.
        int[] salaUsada = {0,0};

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("UsoSalas")

                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            String fecha;
                            String tipo;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                tipo = document.getString("Tipo");
                                fecha = document.getString("Dia");

                                fecha = fecha.substring(0, fecha.length()-3);
                                if (fecha.equalsIgnoreCase("2023-11")){

                                    switch (tipo) {
                                        case "Bulder":
                                            salaUsada[0]++;
                                            break;
                                        case "Cuerda":
                                            salaUsada[1]++;
                                            break;
                                    }

                                }
                            }
                            haTVreservas.append("La sala de Bulder se ha reservado: " + salaUsada[0] + " veces.\n"
                            + "La sala de cuerda se ha reservado: " + salaUsada[1] + " veces.");

                        }
                    }
                });

    }
}