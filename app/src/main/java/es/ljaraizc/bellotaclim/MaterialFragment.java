package es.ljaraizc.bellotaclim;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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

        Material material = new Material();
        material.setTipo("Pies de Gato");
        material.setMarca("Esportiva");
        material.setModelo("Zenit");
        material.setImagen(R.drawable.emoji_rope);
        material.setTalla("Talla: 40");
        material.setNombreEscalador("Leo");

        Material material2 = new Material();
        material2.setTipo("Pies de Gato");
        material2.setMarca("Tenaya");
        material2.setModelo("Rojitos");
        material2.setImagen(R.drawable.emoji_casco);
        material2.setTalla("Talla: 38");
        material2.setNombreEscalador("Leito");

        listaMaterial.add(material);
        listaMaterial.add(material2);

        consultarMaterialDisponible();

        materialListAdapter = new MaterialListAdapter(getActivity(),R.layout.item_material_fila,listaMaterial);

        fmLVmaterial.setAdapter(materialListAdapter);


        return view;
    }

    public void consultarMaterialDisponible(){

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
                                material.setImagen(R.drawable.emoji_pies_gato);

                                listaMaterial.add(material);


                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });


    }

}