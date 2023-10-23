package es.ljaraizc.bellotaclim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    private Button A1_b_iniciar;
    private TextView A1_tv_texto;

    Connection conn;

    //Creo cualquier cosa.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        A1_tv_texto = findViewById(R.id.A1_tv_texto);
        A1_b_iniciar = findViewById(R.id.A1_b_iniciar);

        A1_b_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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
        });

    }
}