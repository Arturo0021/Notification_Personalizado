package triplei.mx.notification;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;

import triplei.mx.notification.Business.Business_Usuario;
import triplei.mx.notification.Entities.Collection_Usuario;
import triplei.mx.notification.Entities.Entities_Rol;

public class Usuarios_Activity extends AppCompatActivity {

    Collection_Usuario collection_usuario;
    Entities_Rol entities_rol;
    TextView text_avatar_tool;
    TextView text_periodo_tool;
    SwipeMenuListView Swipe_list;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios_);
        Toolbar toolbar_usuarios = (Toolbar) findViewById(R.id.toolbar_usuarios);
        setSupportActionBar(toolbar_usuarios);
        getSupportActionBar().setTitle("");
        toolbar_usuarios.setBackground(getResources().getDrawable(R.drawable.fondo, null));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar_usuarios.setNavigationIcon(getResources().getDrawable(R.drawable.back_tool, null));
        toolbar_usuarios.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Usuarios_Activity.this, Rol_Activity.class);
                startActivity(intent);
            }
        });

        context = this;

        entities_rol = (Entities_Rol)getIntent().getSerializableExtra("Collection_Rol");

        text_avatar_tool = (TextView) findViewById(R.id.text_avatar_tool);
        text_periodo_tool = (TextView) findViewById(R.id.text_periodo_tool);
        Swipe_list = (SwipeMenuListView) findViewById(R.id.Swipe_list);

        text_avatar_tool.setText(entities_rol.getNombreRol().substring(0,1));
        text_periodo_tool.setText(entities_rol.getFecha_Inicio() + " - " + entities_rol.getFecha_Fin());


        try {
            collection_usuario = Business_Usuario.Get_Usuarios(context, entities_rol.getRolId(), entities_rol.getIdPeriodo().toLowerCase());
        } catch (Exception e){
            e.getMessage();
        }

        Adapter_Usuarios adapter = new Adapter_Usuarios(context, R.layout.adapter_usuarios, collection_usuario);
        Swipe_list.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem call_user = new SwipeMenuItem(getApplicationContext());
                // set item background
                call_user.setBackground(new ColorDrawable(getResources().getColor(R.color.blanco, null)));
                // set item width
                call_user.setWidth(170);
                // set a icon
                call_user.setIcon(R.drawable.call);
                // add to menu
                menu.addMenuItem(call_user);

                SwipeMenuItem msg_user = new SwipeMenuItem(getApplicationContext());
                msg_user.setBackground(new ColorDrawable(getResources().getColor(R.color.blanco, null)));
                msg_user.setWidth(170);
                msg_user.setIcon(R.drawable.msg);
                menu.addMenuItem(msg_user);

            }
        };

        Swipe_list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        if(collection_usuario.get(position).getTelefono().equals("") || collection_usuario.get(position).getTelefono() == null) {
                            Toast.makeText(context, "No Existe Numero Registrado", Toast.LENGTH_SHORT).show();
                            return false;
                        } else {

                            if (ContextCompat.checkSelfPermission( Usuarios_Activity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ) {
                                ActivityCompat.requestPermissions( Usuarios_Activity.this, new String[] {  android.Manifest.permission.CALL_PHONE}, 1);
                            } else {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + collection_usuario.get(position).getTelefono()));
                                startActivity(intent);
                            }
                        }
                        break;

                    case 1:

                        if(collection_usuario.get(position).getTelefono().equals("") || collection_usuario.get(position).getTelefono() == null) {
                            Toast.makeText(context, "No Existe Numero Registrado", Toast.LENGTH_SHORT).show();
                            return false;
                        } else {

                            if (ContextCompat.checkSelfPermission( Usuarios_Activity.this, android.Manifest.permission.SEND_SMS ) != PackageManager.PERMISSION_GRANTED ) {
                                ActivityCompat.requestPermissions( Usuarios_Activity.this, new String[] {  android.Manifest.permission.SEND_SMS }, 1);
                            } else {

                                PackageManager pm = Usuarios_Activity.this.getPackageManager();

                                if (!pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY) && !pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_CDMA)) {
                                    Toast.makeText(Usuarios_Activity.this, "Lo sentimos, tu dispositivo probablemente no pueda enviar SMS...", Toast.LENGTH_SHORT).show();
                                    return false;
                                }

                                String SENT = "SMS_SENT";
                                String DELIVERED = "SMS_DELIVERED";
                                String mensaje = "Estimado: " + collection_usuario.get(position).getNombre() + " tienes " + collection_usuario.get(position).getSin_Validar() + " días sin validar es urgente que realices este proceso.";

                                //---when the SMS has been sent---
                                registerReceiver(new BroadcastReceiver(){
                                    @Override
                                    public void onReceive(Context arg0, Intent arg1) {
                                        switch (getResultCode())
                                        {
                                            case Activity.RESULT_OK:
                                                Toast.makeText(getBaseContext(), "SMS Enviado",Toast.LENGTH_SHORT).show();
                                                break;
                                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                                Toast.makeText(getBaseContext(), "Falla Genérica",Toast.LENGTH_SHORT).show();
                                                break;
                                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                                Toast.makeText(getBaseContext(), "No Service",Toast.LENGTH_SHORT).show();
                                                break;
                                            case SmsManager.RESULT_ERROR_NULL_PDU:
                                                Toast.makeText(getBaseContext(), "Null PDU",Toast.LENGTH_SHORT).show();
                                                break;
                                            case SmsManager.RESULT_ERROR_RADIO_OFF:
                                                Toast.makeText(getBaseContext(), "Radio Off",Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                    }
                                }, new IntentFilter(SENT));

                                //---when the SMS has been delivered---
                                registerReceiver(new BroadcastReceiver(){
                                    @Override
                                    public void onReceive(Context arg0, Intent arg1) {
                                        switch (getResultCode())
                                        {
                                            case Activity.RESULT_OK:
                                                Toast.makeText(getBaseContext(), "SMS Entregado", Toast.LENGTH_SHORT).show();
                                                break;
                                            case Activity.RESULT_CANCELED:
                                                Toast.makeText(getBaseContext(), "SMS No Entregado", Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                    }
                                }, new IntentFilter(DELIVERED));

                                SmsManager sms = SmsManager.getDefault();

                                PendingIntent sentPI = PendingIntent.getBroadcast(Usuarios_Activity.this, 0, new Intent(SENT), 0);
                                PendingIntent deliveredPI = PendingIntent.getBroadcast(Usuarios_Activity.this, 0,  new Intent(DELIVERED), 0);

                                ArrayList<String> smsBodyParts = sms.divideMessage(mensaje);
                                ArrayList<PendingIntent>  sentpending = new ArrayList<>();
                                ArrayList<PendingIntent>  deliversentpending = new ArrayList<>();

                                for(int i = 0; i < smsBodyParts.size(); i++){
                                    sentpending.add(sentPI);
                                    deliversentpending.add(deliveredPI);
                                }

                                sms.sendMultipartTextMessage(collection_usuario.get(position).getTelefono(), null, smsBodyParts, sentpending, deliversentpending);
                                //sms.sendMultipartTextMessage("5573613221", null, smsBodyParts, sentpending, deliversentpending);

                            }

                        }

                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        Swipe_list.setMenuCreator(creator);

    }


}
