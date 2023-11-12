package es.ljaraizc.bellotaclim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MenuPrincipalActivity extends AppCompatActivity {

    String id;
    String email;
    PerfilFragment perfilFragment = new PerfilFragment();
    MaterialFragment materialFragment = new MaterialFragment();
    BulderFragment boulderFragment = new BulderFragment();
    CuerdaFragment cuerdaFragment = new CuerdaFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        //Recibimos el id del usuario.
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        email = bundle.getString("email");
        //Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

        seleccionarFragment(perfilFragment);

        BottomNavigationView botones_navegacion = findViewById(R.id.botones_navegacion_menu);

        botones_navegacion.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.menu_perfil){
                    seleccionarFragment(perfilFragment);
                }
                if (id == R.id.menu_material){
                    seleccionarFragment(materialFragment);
                }

                if (id == R.id.menu_bulder){
                    seleccionarFragment(boulderFragment);
                }

                if (id == R.id.menu_cuerda){
                    seleccionarFragment(cuerdaFragment);
                }

                return true;
            }
        });

    }


    public void seleccionarFragment(Fragment fragment){

        //Enviamos el id del usuario a los fragment.
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("email", email);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        fragment.setArguments(bundle);

        transaction.replace(R.id.frame_para_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}