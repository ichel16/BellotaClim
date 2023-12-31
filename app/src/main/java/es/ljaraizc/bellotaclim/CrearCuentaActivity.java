package es.ljaraizc.bellotaclim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CrearCuentaActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String numeroSocio = "";

    EditText a1ETnombre, a1ETapellidos, a1ETemail, a1ETtelefono, a1ETedad, a1ETpass;
    Button a1bAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        mAuth = FirebaseAuth.getInstance();

        a1ETnombre = findViewById(R.id.a1ETnombre);
        a1ETapellidos = findViewById(R.id.a1ETapellidos);
        a1ETemail = findViewById(R.id.a1ETemail);
        a1ETtelefono = findViewById(R.id.a1ETtelefono);
        a1ETedad = findViewById(R.id.a1ETedad);
        a1ETpass = findViewById(R.id.a1ETpass);

        crearDialogo();




        a1bAceptar = findViewById(R.id.a1bAceptar);

        a1bAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre, apellidos, email, telefono, edad, pass;

                nombre = a1ETnombre.getText().toString();
                apellidos = a1ETapellidos.getText().toString();
                email = a1ETemail.getText().toString();
                telefono = a1ETtelefono.getText().toString();
                edad = a1ETedad.getText().toString();
                pass = a1ETpass.getText().toString();

                if (validarCampos(nombre, apellidos, email, telefono, edad, pass)){
                    registrar(email, pass);
                }

            }
        });



    }

    public Boolean validarCampos(String nombre, String apellidos, String email, String telefono, String edad, String pass){

        boolean camposValidos = true;

        if (nombre.isEmpty()){
            camposValidos = false;
            a1ETnombre.setError("Este campo no puede estar vacío.");
        }

        if (apellidos.isEmpty()){
            camposValidos = false;
            a1ETapellidos.setError("Este campo no puede estar vacío.");
        }

        if (email.isEmpty()) {
            camposValidos = false;
            a1ETemail.setError("Este campo no puede estar vacío.");
        }

        if (telefono.isEmpty()) {
            camposValidos = false;
            a1ETtelefono.setError("Este campo no puede estar vacío.");
        }else {
            try {
            long movil = Long.parseLong(telefono.toString());
            if (movil < 600000000 | movil > 760000000){
                a1ETtelefono.setError("Móvil no válido.");
                camposValidos = false;
            }
            }catch (Exception e){}

        }

        if (edad.isEmpty()) {
            camposValidos = false;
            a1ETedad.setError("Este campo no puede estar vacío.");
        }else{

            try {
                int year = Integer.parseInt(edad.toString());

                if (year < 1903 | year > 2012){
                    a1ETedad.setError("Introduce una fecha entre 1903 y 2012.");
                    camposValidos = false;
                }

            }catch (Exception e){}

        }

        if (pass.isEmpty()) {
            camposValidos = false;
            a1ETpass.setError("Este campo no puede estar vacío.");
        }

        return camposValidos;
    }

    public void crearUsuario(String id){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> usuario = new HashMap<>();
        usuario.put("nombre", a1ETnombre.getText().toString());
        usuario.put("apellido", a1ETapellidos.getText().toString());
        usuario.put("email", a1ETemail.getText().toString());
        usuario.put("telefono", a1ETtelefono.getText().toString());
        usuario.put("año nacimiento", a1ETedad.getText().toString());
        usuario.put("rol", "user");

        db.collection("Usuarios").document(id).set(usuario)
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

        /*Se crea automáticamente el nombre al documento.
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

    }

    public void crearDialogo(){

        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Número de Socio.");
        dialogo.setMessage("Por seguridad, necesitamos que introduzcas tu número de socio.");
        dialogo.setCancelable(false);

        // creamos la entrada de texto.
        final EditText input = new EditText(this);
        // Expecificamos el tipo de entrada que esperamos recibir.
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        dialogo.setView(input);

        dialogo.setPositiveButton("Adeptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                numeroSocio = input.getText().toString();
                if (numeroSocio.isEmpty())numeroSocio="blanco";
                comprobarNumeroSocio(numeroSocio);
                //Toast.makeText(CrearCuentaActivity.this, input.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });

        dialogo.show();

    }

    public void comprobarNumeroSocio(String numeroSocio){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection("Socios").document(numeroSocio);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        if (document.getBoolean("Registrado")){
                            Toast.makeText(CrearCuentaActivity.this, "Este usuario ya está registrado.", Toast.LENGTH_SHORT).show();
                            a1bAceptar.setEnabled(false);
                        }else {
                            a1ETemail.setText(document.getString("Email"));
                        }
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("TAG", "No such document");
                        a1bAceptar.setEnabled(false);
                        Toast.makeText(CrearCuentaActivity.this, "Necesitas ser socio para poder registrarte.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    public void socioRegistrado(String numeroSocio, String id){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference socioRef = db.collection("Socios").document(numeroSocio);

        socioRef
                .update("Registrado", true,
                        "Id_usuario", id)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

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

                            //Creamos el usuario y al documento le pasamos el Uid del usuario
                            //creado, así se vinculan los datos.
                            crearUsuario(user.getUid());

                            //Actualizamos el documento del socio que se acaba de registrar
                            //Para que nadie más use su número de socio.
                            socioRegistrado(numeroSocio, user.getUid());

                            updateUI(user);

                            finish();

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
            Toast.makeText(this,"Registro completado!",Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(this,"Error al crear el usuario. Pass mínimo 6 caracteres.",Toast.LENGTH_LONG).show();
        }

    }
}