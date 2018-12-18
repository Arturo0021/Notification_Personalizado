package triplei.mx.notification;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.nio.charset.StandardCharsets;

import triplei.mx.notification.Business.Business_Download_Information;
import triplei.mx.notification.Utilerias.Utilerias_Activity;

public class Main_Activity extends AppCompatActivity {

    TextView ver_info;
    EditText input_usuario;
    EditText input_password;
    Button btn_login;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_);

        getWindow().setStatusBarColor(getResources().getColor(R.color.negro, null));
        btn_login = (Button)findViewById(R.id.btn_login);
        ver_info = (TextView)findViewById(R.id.ver_info);
        input_usuario = (EditText)findViewById(R.id.input_usuario);
        input_password = (EditText)findViewById(R.id.input_password);
        context = this;
        startService(new Intent(this, Instance_Token.class));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            AlertDialog.Builder builder = new AlertDialog.Builder(Main_Activity.this);

            if(ContextCompat.checkSelfPermission(Main_Activity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                builder.setTitle("Permisos");
                builder.setMessage("Es Necesario Aceptar Los Siguientes Permisos");
                builder.setIcon(R.drawable.ic_launcher_foreground);
                builder.setCancelable(false);
                builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(Main_Activity.this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_NETWORK_STATE,  Manifest.permission.SEND_RESPOND_VIA_MESSAGE, Manifest.permission.INTERNET}, 1);
                    }
                });
                builder.show();
            }
        }


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input_usuario.getText().toString().equals("")) {
                    ver_info.setText(R.string.valida_usuario);
                } else if(input_password.getText().toString().equals("")) {
                    ver_info.setText(R.string.valida_psw);
                } else {
                    ver_info.setText("");

                    if(validateNetwork()){
                        new Thread_Valida_Usuario().execute();
                    } else {
                        // Ir a la Base de Datos Local, Si no, Respuesta = 1

                    }

                }
            }
        });

    }

    class Thread_Valida_Usuario extends AsyncTask<Void, Void, String>{

        private ProgressDialog dialog;
        String respuesta;
        String error;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Main_Activity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage(getString(R.string.load));
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            String usuario = input_usuario.getText().toString();
            byte[] data_user = usuario.getBytes(StandardCharsets.UTF_8);
            String base64_user = Base64.encodeToString(data_user, Base64.DEFAULT);

            String psw = input_password.getText().toString();
            byte[] data_psw = psw.getBytes(StandardCharsets.UTF_8);
            String base64_psw = Base64.encodeToString(data_psw, Base64.DEFAULT);

            try {
                String  var_token = FirebaseInstanceId.getInstance().getToken();
                respuesta =  triplei.mx.notification.Business.Business_Usuario.Valida_Credenciales(base64_user, base64_psw, var_token);

               if (respuesta != null){
                   if(!respuesta.equals("")) {
                        Business_Download_Information.Download_Usuario(context, var_token);
                   } else {
                      respuesta = "1";
                   }
               } else {
                   respuesta = "1";
               }

            } catch (Exception e) {
                error = e.getMessage();
                respuesta = "2";
            }

            return respuesta;
        }


        @Override
        protected void onPostExecute(String aBoolean) {

            dialog.dismiss();
            switch (aBoolean){
                case "1":
                    ver_info.setText(R.string.credenciales);
                    break;
                case "2":
                    ver_info.setText(error);
                    break;
                default:
                    Intent intent = new Intent(Main_Activity.this, Rol_Activity.class);
                    intent.putExtra("IdUsuario", aBoolean);
                    startActivity(intent);
                    finish();
                    break;
            }

        }
    }

    private boolean validateNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


}
