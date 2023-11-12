package es.ljaraizc.bellotaclim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class AdminMaterialActivity extends AppCompatActivity {

    private Spinner aAMspinner;
    private ArrayAdapter<String> mAemails;
    private List<String> listaEmail = new ArrayList<>();
    private TreeSet<String> treeSetEmail = new TreeSet<>();
    private ListView aAMLVmaterial;
    private MaterialListAdapter materialListAdapter;
    private List<Material> listaMaterial = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_material);

        aAMspinner = findViewById(R.id.aAMspinner);
        aAMLVmaterial = findViewById(R.id.aAMLVmaterial);


        consultarEmails();

        aAMspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                consultarMaterialUsuario(listaEmail.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        aAMLVmaterial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Material material = listaMaterial.get(position);

                Toast.makeText(AdminMaterialActivity.this, "Vas a liberar el siguiente material: " + material.getMarca() + " asociado a " + material.getEmailEscalador(), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder dialogo = new AlertDialog.Builder(AdminMaterialActivity.this);
                dialogo.setTitle("Liberar material");
                dialogo.setMessage("Vas a liberar el siguiente material: " + material.getMarca() + " asociado a " + material.getEmailEscalador());
                dialogo.setCancelable(false);
                dialogo.setPositiveButton("Liberar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(AdminMaterialActivity.this, "CONFIRMADO!", Toast.LENGTH_SHORT).show();
                    }
                });

                dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(AdminMaterialActivity.this, "Proceso cancelado.", Toast.LENGTH_SHORT).show();
                    }
                });
                dialogo.show();

            }
        });

    }

    public void consultarEmails(){

        treeSetEmail.clear();
        listaEmail.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Material")
                .whereEqualTo("Libre", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());

                                Material material = new Material();
                                material.setMarca(document.getString("Marca"));
                                material.setModelo(document.getString("Modelo"));
                                material.setTipo(document.getString("Tipo"));
                                material.setTalla(document.getString("Talla"));
                                material.setEmailEscalador(document.getString("Email"));
                                material.setIdMaterial(document.getId());


                                if (material.getTipo().equalsIgnoreCase("Cuerda")) material.setImagen(R.drawable.emoji_rope);
                                if (material.getTipo().equalsIgnoreCase("Pies de Gato")) material.setImagen(R.drawable.emoji_pies_gato);
                                if (material.getTipo().equalsIgnoreCase("Casco")) material.setImagen(R.drawable.emoji_casco);
                                if (material.getTipo().equalsIgnoreCase("Arnés")) material.setImagen(R.drawable.emoji_arnes);

                                //listaMaterial.add(material);
                                treeSetEmail.add(material.getEmailEscalador());

                            }



                            for (String l: treeSetEmail) {
                                listaEmail.add(l);
                            }

                            //mAemails = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, listaEmails);
                            mAemails = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, listaEmail);
                            aAMspinner.setAdapter(mAemails);


                            //materialListAdapter = new MaterialListAdapter(getBaseContext(),R.layout.item_material_fila,listaMaterial);
                            //aAMLVmaterial.setAdapter(materialListAdapter);

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void consultarMaterialUsuario(String email){

        listaMaterial.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Material")
                .whereEqualTo("Email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());

                                Material material = new Material();
                                material.setMarca(document.getString("Marca"));
                                material.setModelo(document.getString("Modelo"));
                                material.setTipo(document.getString("Tipo"));
                                material.setTalla(document.getString("Talla"));
                                material.setEmailEscalador(document.getString("Email"));
                                material.setIdMaterial(document.getId());


                                if (material.getTipo().equalsIgnoreCase("Cuerda")) material.setImagen(R.drawable.emoji_rope);
                                if (material.getTipo().equalsIgnoreCase("Pies de Gato")) material.setImagen(R.drawable.emoji_pies_gato);
                                if (material.getTipo().equalsIgnoreCase("Casco")) material.setImagen(R.drawable.emoji_casco);
                                if (material.getTipo().equalsIgnoreCase("Arnés")) material.setImagen(R.drawable.emoji_arnes);

                                listaMaterial.add(material);

                            }
                            materialListAdapter = new MaterialListAdapter(getBaseContext(),R.layout.item_material_fila,listaMaterial);
                            aAMLVmaterial.setAdapter(materialListAdapter);

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}