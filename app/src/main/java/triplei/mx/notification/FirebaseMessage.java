package triplei.mx.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Random;

import triplei.mx.notification.Business.Business_Usuario;
import triplei.mx.notification.Entities.Collection_Rol;
import triplei.mx.notification.Entities.Entities_Rol;
import triplei.mx.notification.Entities.Entities_Usuario;


public class FirebaseMessage extends FirebaseMessagingService {

    NotificationManager notifManager;
    Entities_Rol usuarios_rol;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if(remoteMessage.getData().size() > 0){

            JSONObject read = new JSONObject(remoteMessage.getData());
            Collection_Rol collection = new Collection_Rol();
            Entities_Rol rol = new Entities_Rol();

            try {

                rol.setIdPeriodo(read.getString("periodo").toLowerCase());
                rol.setNombreRol(read.getString("nombreRol"));
                rol.setRolId(read.getString("rol"));
                rol.setTotal(read.getInt("sinValidar"));
                rol.setIdUsuario(read.getString("usuario").toLowerCase());

                usuarios_rol =  Business_Usuario.Consulta_Roles_Usuario(FirebaseMessage.this, rol);

                rol.setNombreUsuario(usuarios_rol.getNombreUsuario());
                rol.setFecha_Inicio(usuarios_rol.getFecha_Inicio());
                rol.setFecha_Fin(usuarios_rol.getFecha_Fin());
                rol.setTelefono(usuarios_rol.getTelefono());

            } catch (Exception e) {
                e.getMessage();
            }

            Log.d("#TAG", "Mensaje: " + remoteMessage.getData());

            Notification_Message(rol);

        }

        if(remoteMessage.getNotification() != null){
            Log.d("#TAG", "MENSAJE: " + remoteMessage.getNotification().getBody());
        }

    }

    private void Notification_Message(Entities_Rol rol) {

        int ID_NOTIFICATION = new Random().nextInt();
        int GROUP_ID_NOTIFICATION = 1;
        final String GROUP_KEY_WORK = "triplei.mx.notification";
        String CHANNEL_ID = "notif";
        String CHANEL_TITLE = "canal";
        Intent intent_roles;
        PendingIntent pendingIntent;
        PendingIntent pending_call;
        Intent intent_call;
        Notification summaryNotification = null;

        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = notifManager.getNotificationChannel(CHANNEL_ID);
            if (channel == null) {
                channel = new NotificationChannel(CHANNEL_ID, CHANEL_TITLE, importance);
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(channel);
            }
        }

        Bitmap bitmapIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.circle_cancel);

        intent_roles = new Intent(FirebaseMessage.this, Rol_Activity.class);
        pendingIntent = PendingIntent.getActivity(FirebaseMessage.this, 0, intent_roles, 0);

            intent_call = new Intent(Intent.ACTION_CALL);
            intent_call.setData(Uri.parse("tel:" + rol.getTelefono()));
            pending_call = PendingIntent.getActivity(FirebaseMessage.this, 0, intent_call,0);


        Notification newMessageNotification1 = new NotificationCompat.Builder(FirebaseMessage.this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_single)
                .setContentTitle(rol.getNombreUsuario() + " - " + rol.getFecha_Inicio())
                .setContentText(rol.getNombreRol() + " Tiene " + rol.getTotal() + " Días Sin Validar.")
                .setGroup(GROUP_KEY_WORK)
                .setLargeIcon(bitmapIcon)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .addAction(R.drawable.call, "Llamar", pending_call)
                //.addAction(R.drawable.resumen, "Ver", pendingIntent)
                .setAutoCancel(true)
                .build();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            summaryNotification = new NotificationCompat.Builder(FirebaseMessage.this, CHANNEL_ID)
                    .setContentTitle("Usuarios Sin Validar")
                    .setSmallIcon(R.drawable.notification_group)
                    .setStyle(new NotificationCompat.InboxStyle()
                            .setSummaryText("Usuarios que no han validado"))
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_ALL)
                    .setGroup(GROUP_KEY_WORK)
                    .setGroupSummary(true)
                    .build();
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        SystemClock.sleep(2000);
        notificationManager.notify(ID_NOTIFICATION, newMessageNotification1);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationManager.notify(GROUP_ID_NOTIFICATION, summaryNotification);
        }
        newMessageNotification1.flags = Notification.FLAG_AUTO_CANCEL; // una vez seleccionada la notificacion se cerrara


    }

}
