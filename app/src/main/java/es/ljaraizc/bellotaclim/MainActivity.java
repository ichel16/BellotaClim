package es.ljaraizc.bellotaclim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button A1_b_iniciar, A1_b_registrarse;
    EditText edNombre, etPass;
    private TextView A1_tv_texto;

    List<String> listaHorasOcupadas = new ArrayList<>();
    List<String> listaPruebas = new ArrayList<>();

    TextView helloWorld, textViewPrueba;
    private FirebaseAuth mAuth;


    Connection conn;

    //Creo cualquier cosa.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mAuth = FirebaseAuth.getInstance();

        edNombre = findViewById(R.id.edNombre);
        etPass = findViewById(R.id.etPass);

        helloWorld = findViewById(R.id.A1_tv_texto);

        //crearMaterial();

        A1_b_iniciar = findViewById(R.id.A1_b_iniciar);

        A1_b_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email="", password="";

                email = edNombre.getText().toString();
                password = etPass.getText().toString();

                if (validarCampos(email, password)){
                    iniciarSesion(email, password);
                }



                //leo@dominio.es -> leoleo
                //registrar(email, password);


            }
        });

        A1_b_registrarse = findViewById(R.id.A1_b_registrarse);
        A1_b_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActividadCrearCuenta();
            }
        });

    }

    public Boolean validarCampos(String email, String password){

        boolean camposValidos = true;

        if (email.isEmpty()){
            camposValidos = false;
            edNombre.setError("Este campo no puede estar vacío.");
        }
        if (password.isEmpty()) {
            camposValidos = false;
            etPass.setError("Este campo no puede estar vacío.");
        }

        return camposValidos;
    }

    public void consultaSimple(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference dr = db.collection("UsoSalas").document();

        db.collection("UsoSalas")
                .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                                        Log.d("TAG",document.getId()+" -> "+document.getData());
                                        listaPruebas.add((document.getData().toString()));
                                }

                            }
                        });

        textViewPrueba.setText(listaPruebas.toString()+"");

    }

    public void iniciarSesion(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            String id = user.getUid();
                            abrirActividadMenuPrincipal(id);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Autentificación fallida.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void updateUI(FirebaseUser account){

        if(account != null){
            Toast.makeText(this,"Te has logueado correctamente.",Toast.LENGTH_SHORT).show();

            //startActivity(new Intent(this,AnotherActivity.class));

        }else {
            //Toast.makeText(this,"You Didnt signed in",Toast.LENGTH_LONG).show();
        }

    }

    public void abrirActividadMenuPrincipal(String id){
        Intent intent = new Intent(this, MenuPrincipalActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("email", edNombre.getText().toString());
        etPass.setText("");
        edNombre.setText("");
        startActivity(intent);
    }

    public void abrirActividadCrearCuenta(){
        Intent intent = new Intent(this, CrearCuentaActivity.class);
        startActivity(intent);
    }

    public void crearMaterial(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        /*Map<String, Object> piesDeGato = new HashMap<>();
        piesDeGato.put("Email", "");
        piesDeGato.put("Id_escalador", "");
        piesDeGato.put("Libre", true);
        piesDeGato.put("Marca", "Scarpa");
        piesDeGato.put("Modelo", "Drago");
        piesDeGato.put("Nombre", "");
        piesDeGato.put("Talla", "38");
        piesDeGato.put("Telefono", "");
        piesDeGato.put("Tipo", "Pies de Gato");*/

        /*Map<String, Object> arnes = new HashMap<>();
        arnes.put("Email", "");
        arnes.put("Id_escalador", "");
        arnes.put("Libre", true);
        arnes.put("Marca", "Singing Rock");
        arnes.put("Modelo", "Top");
        arnes.put("Nombre", "");
        arnes.put("Talla", "M");
        arnes.put("Telefono", "");
        arnes.put("Tipo", "Arnés");*/

        /*Map<String, Object> cuerda = new HashMap<>();
        cuerda.put("Email", "");
        cuerda.put("Id_escalador", "");
        cuerda.put("Libre", true);
        cuerda.put("Marca", "Korda's");
        cuerda.put("Modelo", "Kloe");
        cuerda.put("Nombre", "");
        cuerda.put("Talla", "40m");
        cuerda.put("Telefono", "");
        cuerda.put("Tipo", "Cuerda");*/

        Map<String, Object> casco = new HashMap<>();
        casco.put("Email", "");
        casco.put("Id_escalador", "");
        casco.put("Libre", true);
        casco.put("Marca", "Kong");
        casco.put("Modelo", "Verde");
        casco.put("Nombre", "");
        casco.put("Talla", "L");
        casco.put("Telefono", "");
        casco.put("Tipo", "Casco");



// Add a new document with a generated ID
        db.collection("Material")
                .add(casco)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                    }
                });

    }


}