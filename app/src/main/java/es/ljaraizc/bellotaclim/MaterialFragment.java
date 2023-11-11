package es.ljaraizc.bellotaclim;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    private TextView fmTVresumenReserva;
    private Button fmBreservar;




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

        fmLVmaterial = view.findViewById(R.id.fmLVmaterial);
        fmTVresumenReserva = view.findViewById(R.id.fmTVresumenReserva);
        fmBreservar = view.findViewById(R.id.fmBreservar);

        consultarMaterialDisponible();

        fmLVmaterial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Obtenemos el item seleccionado y lo convertimos a un objeto de tipo Material.
                //Así podemos interactuar con él.
                Material material = (Material) parent.getItemAtPosition(position);

                Toast.makeText(getActivity(), "Reservas: " + material.getTipo() + ", " + material.getMarca() + " - " + material.getModelo(), Toast.LENGTH_SHORT).show();

                fmTVresumenReserva.setText("Reserva seleccionada:\n" + material.getTipo() + ", " + material.getMarca() + " - " + material.getModelo());

                fmBreservar.isClickable();

            }
        });


        return view;
    }

    public void consultarMaterialDisponible(){

        listaMaterial.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Material")
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