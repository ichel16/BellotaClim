package es.ljaraizc.bellotaclim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MenuPrincipalActivity extends AppCompatActivity {

    PerfilFragment perfilFragment = new PerfilFragment();
    //MaterialFragment materialFragment = new MaterialFragment();
    BulderFragment boulderFragment = new BulderFragment();
    //CuerdaFragment cuerdaFragment = new CuerdaFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        seleccionarFragment(perfilFragment);

        BottomNavigationView botones_navegacion = findViewById(R.id.botones_navegacion_menu);

        botones_navegacion.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.menu_perfil){
                    seleccionarFragment(perfilFragment);
                }


                if (id == R.id.menu_bulder){
                    seleccionarFragment(boulderFragment);
                }

                return true;
            }
        });

    }


    public void seleccionarFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_para_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}