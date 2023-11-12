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

    public Task<QuerySnapshot> queryCollection(String collectionPath) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection(collectionPath);

        // Crea un TaskCompletionSource para controlar la promesa
        final TaskCompletionSource<QuerySnapshot> taskCompletionSource = new TaskCompletionSource<>();

        // Realiza la consulta
        collectionRef.get()
                .addOnSuccessListener(querySnapshot -> {
                    // La consulta fue exitosa, completa la promesa con el resultado
                    taskCompletionSource.setResult(querySnapshot);
                })
                .addOnFailureListener(e -> {
                    // La consulta falló, completa la promesa con un error
                    taskCompletionSource.setException(e);
                });

        // Retorna la promesa
        return taskCompletionSource.getTask();
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
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }





    public void updateUI(FirebaseUser account){

        if(account != null){
            Toast.makeText(this,"You Signed In successfully",Toast.LENGTH_LONG).show();
            //startActivity(new Intent(this,AnotherActivity.class));

        }else {
            Toast.makeText(this,"You Didnt signed in",Toast.LENGTH_LONG).show();
        }

    }

    public void borrar(){
        ConexionBDD cBDD = new ConexionBDD();
        String nombre ="";

        try {

            conn = cBDD.conclass();

            if(conn!=null){

                String query ="SELECT * FROM usuario  WHERE id_usuario = 1";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);

                while (rs.next()){
                    nombre = rs.getString(2);
                }

                if (nombre!=null){
                    Toast.makeText(MainActivity.this, "Bienvenido " + nombre, Toast.LENGTH_SHORT).show();
                    //abrirActividadDatosUsuario(email);
                    //abrirActividadReservar(email);

                }else {
                    Toast.makeText(MainActivity.this, "Usuario y contraseña erróneos.", Toast.LENGTH_SHORT).show();
                }

                A1_tv_texto.setText(""+nombre);
            }

        }catch (Exception e){
            Log.e("Error: ", e.getMessage());
        }

    }

    public void abrirActividadMenuPrincipal(String id){
        Intent intent = new Intent(this, MenuPrincipalActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("email", edNombre.getText().toString());
        startActivity(intent);
    }

    public void abrirActividadCrearCuenta(){
        Intent intent = new Intent(this, CrearCuentaActivity.class);
        startActivity(intent);
    }

}