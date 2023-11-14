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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class AdminMaterialActivity extends AppCompatActivity {

    private Spinner aAMspinner;
    private ArrayAdapter<String> mAemails;
    private List<String> listaEmail = new ArrayList<>();
    private TreeSet<String> treeSetEmail = new TreeSet<>();
    private ListView aAMLVmaterial;
    private MaterialListAdapter materialListAdapter;
    private List<Material> listaMaterial = new ArrayList<>();
    private int posicionEmail = 0;

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
                posicionEmail = position;
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

                        crearHistorico(material);

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

        Button aAMbVolver = findViewById(R.id.aAMbVolver);

        aAMbVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    public void crearHistorico(Material m){

        //Obtenemos el año, mes y día en el que nos encontramos.
        Calendar calendar = new GregorianCalendar();
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        String fecha = (año+"-"+(mes+1)+"-"+dia);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> materialHistorico = new HashMap<>();

        materialHistorico.put("Id_material", m.getIdMaterial());
        materialHistorico.put("Email", m.getEmailEscalador());
        materialHistorico.put("Dia", fecha);

        db.collection("MaterialHistorico")
                .add(materialHistorico)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                        liberarMaterial(m);
                    }
                });

    }

    public void liberarMaterial(Material m){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference materialRef = db.collection("Material").document(m.getIdMaterial());

        materialRef
                .update("Libre", true,"Email", "", "Id_escalador", "")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "Datos actualizados.");

                        //Actualizamos el listado para que desaparezca el objeto liberado.
                        consultarMaterialUsuario(listaEmail.get(posicionEmail));
                    }

                });
    }



}