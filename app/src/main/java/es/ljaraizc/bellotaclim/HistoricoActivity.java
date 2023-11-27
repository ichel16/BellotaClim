package es.ljaraizc.bellotaclim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class HistoricoActivity extends AppCompatActivity {

    private String id;
    private String email;
    private TextView haTVsocios, haTVreservas;
    private Spinner haSmes, haSyear;
    private ArrayAdapter<String> mAdapterMeses, mAdapterYears;

    private Button haBconsultar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        //Recibimos el id del usuario.
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        email = bundle.getString("email");

        //Obtenemos el mes en el que nos encontramos.
        Calendar calendar = new GregorianCalendar();
        int mes = calendar.get(Calendar.MONTH);

        haTVsocios = findViewById(R.id.haTVsocios);
        consultaNumeroSocios();

        haTVreservas = findViewById(R.id.haTVreservas);

        haSmes = findViewById(R.id.haSmes);
        List<String> meses = crearMeses();
        mAdapterMeses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, meses);
        haSmes.setAdapter(mAdapterMeses);

        //Seleccionamos el mes actual.
        haSmes.setSelection(mes);

        haSyear = findViewById(R.id.haSyear);
        List<String> years = createYears();
        mAdapterYears = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        haSyear.setAdapter(mAdapterYears);

        haBconsultar = findViewById(R.id.haBconsultar);
        haBconsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultaReservasMaterial(haSyear.getSelectedItem().toString(), haSmes.getSelectedItem().toString());
            }
        });

        consultaReservasMaterial(haSyear.getSelectedItem().toString(), haSmes.getSelectedItem().toString());

    }

    public List<String> crearMeses(){
        List<String> meses = new ArrayList<>();
        meses.add("01");
        meses.add("02");
        meses.add("03");
        meses.add("04");
        meses.add("05");
        meses.add("06");
        meses.add("07");
        meses.add("08");
        meses.add("09");
        meses.add("10");
        meses.add("11");
        meses.add("12");

        return meses;
    }

    public List<String> createYears(){
        List<String> years = new ArrayList<>();

        years.add("2023");
        years.add("2024");
        years.add("2025");
        years.add("2026");
        years.add("2027");
        years.add("2028");
        years.add("2029");
        years.add("2030");

        return years;
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

    public void consultaReservasMaterial(String year, String mes){

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
                                if (fecha.equalsIgnoreCase(year+"-"+mes)){

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
                            haTVreservas.setText("En " + obtenerMes(mes) + " de " + year);
                            haTVreservas.append(String.format(" hemos tenido las siguientes reservas.\n\nMaterial reservado: \n%d arneses.\n%d cascos.\n%d cuerdas.\n%d pares de pies de gato.\n", materialUsado[0], materialUsado[1], materialUsado[2], materialUsado[3]));
                            consultasReservasSalas(year, mes);
                        }
                    }
                });
    }

    public String obtenerMes(String mes){

        switch (mes) {
            case "1":
                mes = "Enero";
                break;
            case "2":
                mes = "Febrero";
                break;
            case "3":
                mes = "Marzo";
                break;
            case "4":
                mes = "Abril";
                break;
            case "5":
                mes = "Mayo";
                break;
            case "6":
                mes = "Junio";
                break;
            case "7":
                mes = "Julio";
                break;
            case "8":
                mes = "Agosto";
                break;
            case "9":
                mes = "Septiembre";
                break;
            case "10":
                mes = "Octubre";
                break;
            case "11":
                mes = "Noviembre";
                break;
            case "12":
                mes = "Diciembre";
                break;
        }

        return mes;
    }

    public void consultasReservasSalas(String year, String mes){

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
                                if (fecha.equalsIgnoreCase(year+"-"+mes)){

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
                            haTVreservas.append("Sala de Bulder: " + salaUsada[0] + " reservas.\n"
                            + "Sala de cuerda: " + salaUsada[1] + " reservas.");

                        }
                    }
                });
    }
}