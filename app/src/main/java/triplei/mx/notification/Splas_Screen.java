package triplei.mx.notification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import triplei.mx.notification.Utilerias.Utilerias_Activity;

public class Splas_Screen extends AppCompatActivity {

    SharedPreferences preferences;
    String IdUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splas__screen);


        preferences = getSharedPreferences("Preference", Context.MODE_PRIVATE);
        final Intent go_to_Login = new Intent(Splas_Screen.this, Main_Activity.class);
        final Intent go_to_roles = new Intent(Splas_Screen.this, Rol_Activity.class);

        IdUsuario = Utilerias_Activity.getIdUsuarioPreference(preferences);

        Thread t = new Thread(){
            public void run(){
                try {
                    Thread.sleep(4000);  // change the time according to your needs(its in milliseconds)


                    if(!TextUtils.isEmpty(IdUsuario)) {
                        go_to_roles.putExtra("IdUsuario", IdUsuario);
                        startActivity(go_to_roles);
                        finish();
                    } else {
                        startActivity(go_to_Login);
                        finish();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();


    }
}
