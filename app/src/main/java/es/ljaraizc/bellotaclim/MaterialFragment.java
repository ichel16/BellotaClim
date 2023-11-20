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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MaterialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MaterialFragment extends Fragment {


    private ListView fmLVmaterial;
    private List<Material> listaMaterial = new ArrayList<>();
    private MaterialListAdapter materialListAdapter;
    private Spinner fmStipo;
    private ArrayAdapter<String> mAdapterTipoMaterial;
    private TextView fmTVresumenReserva;
    private Button fmBreservar;
    private Material material;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MaterialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MaterialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MaterialFragment newInstance(String param1, String param2) {
        MaterialFragment fragment = new MaterialFragment();
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
        View view = inflater.inflate(R.layout.fragment_material, container, false);

        String idEscalador = this.getArguments().getString("id");
        String email = this.getArguments().getString("email");

        fmLVmaterial = view.findViewById(R.id.fmLVmaterial);
        fmTVresumenReserva = view.findViewById(R.id.fmTVresumenReserva);
        fmBreservar = view.findViewById(R.id.fmBreservar);

        fmTVresumenReserva.setText("Pulsa sobre el elemento que quieres reservar.");

        //Con el Spinner ya no es necesario.
        //consultarMaterialDisponible();

        fmLVmaterial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Obtenemos el item seleccionado y lo convertimos a un objeto de tipo Material.
                //Así podemos interactuar con él.
                material = (Material) parent.getItemAtPosition(position);

                //Toast.makeText(getActivity(), "Reservas: " + material.getTipo() + ", " + material.getMarca() + " - " + material.getModelo(), Toast.LENGTH_SHORT).show();

                fmTVresumenReserva.setText("Reserva seleccionada:\n" + material.getTipo() + ", " + material.getMarca() + " - " + material.getModelo());
                //fmTVresumenReserva.append("\n ID: " + material.getIdMaterial());

                fmBreservar.setEnabled(true);

            }
        });

        fmStipo = view.findViewById(R.id.fmStipo);

        List<String> tipoMaterial = new ArrayList<>();
        tipoMaterial.add("Sin Filtro");
        tipoMaterial.add("Casco");
        tipoMaterial.add("Arnés");
        tipoMaterial.add("Pies de Gato");
        tipoMaterial.add("Cuerda");

        mAdapterTipoMaterial = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item,tipoMaterial);
        fmStipo.setAdapter(mAdapterTipoMaterial);

        fmStipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    consultarMaterialDisponible();
                }else {
                    consultarMaterialDisponiblePorTipo(tipoMaterial.get(position).toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fmBreservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                realizarReserva(material, email, idEscalador);
                consultarMaterialDisponible();

            }
        });


        return view;
    }

    public void realizarReserva(Material m, String email, String id){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference materialRef = db.collection("Material").document(m.getIdMaterial());

        materialRef
                .update("Libre", false,"Email", email, "Id_escalador", id)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "¡Reserva realiza!", Toast.LENGTH_SHORT).show();
                    }
                });



    }

    public void consultarMaterialDisponible(){

        listaMaterial.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Material")
                .whereEqualTo("Libre", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("TAG", document.getId() + " => " + document.getData());

                                Material material = new Material();
                                material.setMarca(document.getString("Marca"));
                                material.setModelo(document.getString("Modelo"));
                                material.setTipo(document.getString("Tipo"));
                                material.setTalla(document.getString("Talla"));

                                material.setIdMaterial(document.getId());

                                if (material.getTipo().equalsIgnoreCase("Cuerda")) material.setImagen(R.drawable.emoji_rope);
                                if (material.getTipo().equalsIgnoreCase("Pies de Gato")) material.setImagen(R.drawable.emoji_pies_gato);
                                if (material.getTipo().equalsIgnoreCase("Casco")) material.setImagen(R.drawable.emoji_casco);
                                if (material.getTipo().equalsIgnoreCase("Arnés")) material.setImagen(R.drawable.emoji_arnes);

                                listaMaterial.add(material);

                            }

                            materialListAdapter = new MaterialListAdapter(getActivity(),R.layout.item_material_fila,listaMaterial);
                            fmLVmaterial.setAdapter(materialListAdapter);

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void consultarMaterialDisponiblePorTipo(String tipo){

        listaMaterial.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Material")
                .whereEqualTo("Libre", true)
                .whereEqualTo("Tipo", tipo)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("TAG", document.getId() + " => " + document.getData());

                                Material material = new Material();
                                material.setMarca(document.getString("Marca"));
                                material.setModelo(document.getString("Modelo"));
                                material.setTipo(document.getString("Tipo"));
                                material.setTalla(document.getString("Talla"));

                                material.setIdMaterial(document.getId());

                                if (material.getTipo().equalsIgnoreCase("Cuerda")) material.setImagen(R.drawable.emoji_rope);
                                if (material.getTipo().equalsIgnoreCase("Pies de Gato")) material.setImagen(R.drawable.emoji_pies_gato);
                                if (material.getTipo().equalsIgnoreCase("Casco")) material.setImagen(R.drawable.emoji_casco);
                                if (material.getTipo().equalsIgnoreCase("Arnés")) material.setImagen(R.drawable.emoji_arnes);

                                listaMaterial.add(material);

                            }

                            materialListAdapter = new MaterialListAdapter(getActivity(),R.layout.item_material_fila,listaMaterial);
                            fmLVmaterial.setAdapter(materialListAdapter);

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}