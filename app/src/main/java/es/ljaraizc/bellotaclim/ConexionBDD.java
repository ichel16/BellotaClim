package es.ljaraizc.bellotaclim;

import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBDD {

    public Connection conclass(){

        StrictMode.ThreadPolicy tpolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tpolicy);

        Connection conn = null;

        try {

            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://ep-bold-hall-83905967.eu-central-1.aws.neon.fl0.io/rocodromo",
                    "fl0user",
                    "PoIQkw7Ee4mS");


        }catch (Exception e){
            Log.e("Error: ", e.getMessage());
        }

        return conn;
    }

}
