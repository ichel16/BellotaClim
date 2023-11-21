package es.ljaraizc.bellotaclim;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CuerdaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CuerdaFragment extends Fragment {

    private Spinner fcSpinnerDia;
    private Spinner fcSpinnerHora;

    private List<String> horario = new ArrayList<>();
    private List<String> listaHorasOcupadas = new ArrayList<>();
    private List<String> listaHora = new ArrayList<>();
    List<String> listaDia = new ArrayList<>();
    private ArrayAdapter<String> mAdapterHorario, mAdapterHora, mAdapterDia;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CuerdaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CuerdaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CuerdaFragment newInstance(String param1, String param2) {
        CuerdaFragment fragment = new CuerdaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cuerda, container, false);

        String id = this.getArguments().getString("id");

        //Obtenemos el año, mes y día en el que nos encontramos.
        Calendar calendar = new GregorianCalendar();
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        listaDia.clear();
        listaDia.add(año+"-"+(mes+1)+"-"+dia);
        listaDia.add(año+"-"+(mes+1)+"-"+(dia+1));

        fcSpinnerDia = view.findViewById(R.id.fcSpinnerDia);
        mAdapterDia = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item,listaDia);
        fcSpinnerDia.setAdapter(mAdapterDia);

        crearHoras();
        fcSpinnerHora = view.findViewById(R.id.fcSpinnerHora);
        mAdapterHora = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item,listaHora);
        fcSpinnerHora.setAdapter(mAdapterHora);

        // ---------> Consultar Aforo con Spinner.
        fcSpinnerDia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View views, int position, long id) {
                consultarAforo(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // -------> Botón reservar cita.
        Button fbBReservar = view.findViewById(R.id.fcBReservar);

        fbBReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                consultarUsuarioTieneCita(view, id);
                //crearReserva(id);
                //consultarAforo(view);

            }
        });

        return view;
    }

    public void crearHoras(){

        listaHora.clear();
        listaHora.add("08:00");
        listaHora.add("10:00");
        listaHora.add("12:00");
        listaHora.add("16:00");
        listaHora.add("18:00");
        listaHora.add("20:00");
    }

    public void crearReserva(String id){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> reserva = new HashMap<>();
        reserva.put("Dia", fcSpinnerDia.getSelectedItem().toString());
        reserva.put("Escalador", id);
        reserva.put("Hora", fcSpinnerHora.getSelectedItem().toString());
        reserva.put("Tipo", "Cuerda");

        // Add a new document with a generated ID
        db.collection("UsoSalas")
                .add(reserva)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });

    }

    public void consultarAforo(View view){

        listaHorasOcupadas.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("UsoSalas")
                .whereEqualTo("Dia", fcSpinnerDia.getSelectedItem().toString())
                .whereEqualTo("Tipo","Cuerda")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                listaHorasOcupadas.add(document.getString("Hora"));
                                //listaHorasOcupadas.add(document.getData().toString());
                            }

                            dibujarHorario(listaDia.get(fcSpinnerDia.getSelectedItemPosition()), view);

                        }else {
                            Log.d("TAG","Error: ", task.getException());
                        }
                    }
                });


    }

    public void dibujarHorario(String fecha, View view){

        horario.clear();

        int ocho=0;
        int diez=0;
        int doce=0;
        int dieciseis=0;
        int dieciocho=0;
        int veinte=0;


        for (int i = 0; i < listaHorasOcupadas.size(); i++) {
            if (listaHorasOcupadas.get(i).equalsIgnoreCase("08:00")){
                ocho++;
            }
            if (listaHorasOcupadas.get(i).equalsIgnoreCase("10:00")){
                diez++;
            }
            if (listaHorasOcupadas.get(i).equalsIgnoreCase("12:00")){
                doce++;
            }
            if (listaHorasOcupadas.get(i).equalsIgnoreCase("16:00")){
                dieciseis++;
            }
            if (listaHorasOcupadas.get(i).equalsIgnoreCase("18:00")){
                dieciocho++;
            }
            if (listaHorasOcupadas.get(i).equalsIgnoreCase("20:00")){
                veinte++;
            }

        }

        horario.add(fecha + " [08:00 - 10:00] - Aforo al: "+ ocho*20 +"%");
        horario.add(fecha + " [10:00 - 12:00] - Aforo al: "+ diez*20 +"%");
        horario.add(fecha + " [12:00 - 14:00] - Aforo al: "+ doce*20 +"%");
        horario.add(fecha + " [16:00 - 18:00] - Aforo al: "+ dieciseis*20 +"%");
        horario.add(fecha + " [18:00 - 20:00] - Aforo al: "+ dieciocho*20 +"%");
        horario.add(fecha + " [20:00 - 22:00] - Aforo al: "+ veinte*20 +"%");

        ListView fmLVhorario;
        mAdapterHorario = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1,horario);
        fmLVhorario = view.findViewById(R.id.fcLvHorario);
        fmLVhorario.setAdapter(mAdapterHorario);

    }

    public void consultarHora(View view, String id){

        int[] contar = {0};

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("UsoSalas")
                .whereEqualTo("Dia", fcSpinnerDia.getSelectedItem().toString())
                .whereEqualTo("Hora", fcSpinnerHora.getSelectedItem().toString())
                .whereEqualTo("Tipo","Cuerda")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                contar[0]++;
                            }

                            if (contar[0]>=5){
                                Toast.makeText(getContext(), "¡Sala llena! Por favor, elige otra hora.", Toast.LENGTH_SHORT).show();
                            }else {
                                crearReserva(id);
                                consultarAforo(view);
                                //Toast.makeText(getContext(), ""+contar[0], Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Log.d("TAG","Error: ", task.getException());
                        }
                    }
                });

    }

    public void consultarUsuarioTieneCita(View view, String id){
        boolean[] tieneReserva = {false};

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("UsoSalas")
                .whereEqualTo("Dia", fcSpinnerDia.getSelectedItem().toString())
                .whereEqualTo("Escalador", id)
                .whereEqualTo("Tipo","Cuerda")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                tieneReserva[0]=true;
                            }

                            if (tieneReserva[0]){
                                Toast.makeText(getContext(), "Solo puedes realizar una reserva por día.", Toast.LENGTH_SHORT).show();
                            }else {
                                consultarHora(view, id);
                                //Toast.makeText(getContext(), ""+contar[0], Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Log.d("TAG","Error: ", task.getException());
                        }
                    }
                });
    }


}