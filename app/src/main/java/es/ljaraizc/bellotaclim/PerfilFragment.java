package es.ljaraizc.bellotaclim;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    EditText fpNombre, fpApellidos, fpEmail, fpTelefono, fpNacimiento;

    TextView fpTVmaterialReservado;

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

        String id = this.getArguments().getString("id");

        fpNombre = view.findViewById(R.id.fpNombre);
        fpApellidos = view.findViewById(R.id.fpApellidos);
        fpEmail = view.findViewById(R.id.fpEmail);
        fpTelefono = view.findViewById(R.id.fpTelefono);
        fpNacimiento = view.findViewById(R.id.fpNacimiento);

        fpTVmaterialReservado = view.findViewById(R.id.fpTVmaterialReservado);

        fpLinearLayout = view.findViewById(R.id.fpLinearLayout);


        consultarDatosUsuario(id, view);

        return view;
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

                        fpNombre.setText(document.getString("nombre"));
                        fpApellidos.setText(document.getString("apellido"));
                        fpEmail.setText(document.getString("email"));
                        fpTelefono.setText(document.getString("telefono"));
                        fpNacimiento.setText(document.getString("año nacimiento"));

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

        fpTVmaterialReservado.setText("");

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

                                        fpTVmaterialReservado.append("Sala " + document.getString("Tipo") + ", día " + document.getString("Dia") + " a las: " + document.getString("Hora") + "\n");

                                    }
                                }
                            }
                        });








    }
}