package es.ljaraizc.bellotaclim;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    EditText fpNombre, fpApellidos, fpEmail, fpTelefono, fpNacimiento;

    Button fpBadminMaterial, fpBadminHistorico, fpBcerrarSesion;

    TextView fpTVmaterialReservado;
    ListView fpLVlistadoReservas;
    List<String> listadoReservas = new ArrayList<>();
    private ArrayAdapter<String> mAdapterListadoReservas;

    LinearLayout fpLinearLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        /*Modificamos la función del botón atrás.*/
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                cerrarSesionDialogo();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        /*-----------------------------------------------------------------------------------------*/

        String id = this.getArguments().getString("id");

        fpNombre = view.findViewById(R.id.fpNombre);
        fpApellidos = view.findViewById(R.id.fpApellidos);
        fpEmail = view.findViewById(R.id.fpEmail);
        fpTelefono = view.findViewById(R.id.fpTelefono);
        fpNacimiento = view.findViewById(R.id.fpNacimiento);

        //fpTVmaterialReservado = view.findViewById(R.id.fpTVmaterialReservado);

        fpLVlistadoReservas = view.findViewById(R.id.fpLVlistadoReservas);

        fpLinearLayout = view.findViewById(R.id.fpLinearLayout);


        consultarSalasReservadas(id);
        consultarDatosUsuario(id, view);
        consultarMaterialAlquilado(id);

        fpBadminMaterial = view.findViewById(R.id.fpBadminMaterial);

        fpBadminMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActividadAdminMaterial(id);
            }
        });


        fpBadminHistorico = view.findViewById(R.id.fpBadminHistorico);

        fpBadminHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActividadHistorico(id);
            }
        });

        fpBcerrarSesion = view.findViewById(R.id.fpBcerrarSesion);

        fpBcerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesionDialogo();
            }
        });



        return view;
    }

    public void cerrarSesionDialogo(){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        dialogo.setTitle("Cerrar Sesión.");
        dialogo.setMessage("¿Quieres cerrar la sesión?");
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                getActivity().finish();

            }
        });

        dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogo.show();
    }

    public void consultarMaterialAlquilado(String id){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Material")
                .whereEqualTo("Id_escalador", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){

                                //fpTVmaterialReservado.append("Material: " + document.getString("Tipo") + ", marca " + document.getString("Marca") + ", modelo: " + document.getString("Modelo") + "\n");
                                listadoReservas.add("Material: " + document.getString("Tipo") + ", Marca " + document.getString("Marca") + ", Modelo " + document.getString("Modelo") + ", Talla " + document.getString("Talla"));
                                mAdapterListadoReservas = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,listadoReservas);
                                fpLVlistadoReservas.setAdapter(mAdapterListadoReservas);

                            }
                        }
                    }
                });

    }

    public void consultarSalasReservadas(String id){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //fpTVmaterialReservado.setText("");
        listadoReservas.clear();

        //Obtenemos el año, mes y día en el que nos encontramos.
        Calendar calendar = new GregorianCalendar();
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        List<String> listaDia = new ArrayList<>();

        listaDia.clear();
        listaDia.add(año+"-"+(mes+1)+"-"+dia);
        listaDia.add(año+"-"+(mes+1)+"-"+(dia+1));

        db.collection("UsoSalas")
                .where(Filter.and(
                        Filter.equalTo("Escalador", id),
                        Filter.or(
                                Filter.equalTo("Dia", listaDia.get(0)),
                                Filter.equalTo("Dia",listaDia.get(1))
                        )
                )).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){

                                //fpTVmaterialReservado.append("Sala " + document.getString("Tipo") + ", día " + document.getString("Dia") + " a las: " + document.getString("Hora") + "\n");
                                listadoReservas.add("Sala " + document.getString("Tipo") + ", día " + document.getString("Dia") + " a las: " + document.getString("Hora"));
                                mAdapterListadoReservas = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,listadoReservas);
                                fpLVlistadoReservas.setAdapter(mAdapterListadoReservas);
                            }
                        }
                    }
                });
    }
    public void consultarDatosUsuario(String id, View view){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection("Usuarios").document(id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        fpNombre.setText("Nombre: " + document.getString("nombre"));
                        fpApellidos.setText("Apellidos: " + document.getString("apellido"));
                        fpEmail.setText("Email: " + document.getString("email"));
                        fpTelefono.setText("Tlf: " + document.getString("telefono"));
                        fpNacimiento.setText("Año de nacimiento: " + document.getString("año nacimiento"));

                        if (document.getString("rol").equalsIgnoreCase("admin")) fpLinearLayout.setVisibility(View.VISIBLE);

                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("TAG", "No such document");
                    }
                }else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });


    }
    public void abrirActividadAdminMaterial(String id){
        Intent intent = new Intent(getActivity(), AdminMaterialActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("email", fpEmail.getText().toString());
        startActivity(intent);
    }

    public void abrirActividadHistorico(String id){
        Intent intent = new Intent(getActivity(), HistoricoActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("email", fpEmail.getText().toString());
        startActivity(intent);
    }

}