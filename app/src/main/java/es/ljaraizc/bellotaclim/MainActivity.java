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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button A1_b_iniciar;
    private TextView A1_tv_texto;

    private FirebaseAuth mAuth;

    Connection conn;

    //Creo cualquier cosa.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("apellido", "test1 test2");
        user.put("edad", 22);
        user.put("email", "test@dominio.es");
        user.put("nombre", "nombreTest");
        user.put("pass", "pass");
        user.put("rol", "user");
        user.put("telefono", "654654123");

        //Asisgnas el nombre a un documento.
        db.collection("Usuarios").document("TeVoyABorrar").set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

        //Se crea automáticamente el nombre al documento.
        db.collection("Usuarios")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.println(Log.ERROR,"ALGO",""+e);
                    }
                })
        ;*/

        mAuth = FirebaseAuth.getInstance();

        EditText edNombre = findViewById(R.id.edNombre);
        EditText etPass = findViewById(R.id.etPass);


        A1_b_iniciar = findViewById(R.id.A1_b_iniciar);

        A1_b_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edNombre.getText().toString();
                String password = etPass.getText().toString();

                //leo@dominio.es -> leoleo
                //registrar(email, password);
                iniciarSesion(email, password);

            }
        });


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

                            abrirActividadMenuPrincipal();

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

    public void registrar(String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
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

    public void abrirActividadMenuPrincipal(){
        Intent intent = new Intent(this, MenuPrincipalActivity.class);
        startActivity(intent);
    }

}