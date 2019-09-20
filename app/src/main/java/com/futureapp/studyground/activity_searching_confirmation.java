package com.futureapp.studyground;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class activity_searching_confirmation extends AppCompatActivity {

    private static final String TAG = "search";

    String materia,option;
    TextView txtBusqueda;

    Button mostrarNotificacion;

    NotificationChannel channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching_confirmation);

        materia=getIntent().getStringExtra("materiaStudy");
        option=getIntent().getStringExtra("option");

        String text=getString(R.string.find_partner,option,materia);

        txtBusqueda=(TextView) findViewById(R.id.busqueda);

        txtBusqueda.setText(text);



        //notificacion
        mostrarNotificacion=(Button)findViewById(R.id.btnMostrarNotificacion);
        mostrarNotificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder mBuilder;
                NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);


                createNotificationChannel();

                int icono = R.mipmap.ic_launcher;
                Intent i=new Intent(activity_searching_confirmation.this, ProfileMainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(activity_searching_confirmation.this, 0, i, 0);

                mBuilder =new NotificationCompat.Builder(getApplicationContext())
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(icono)
                        .setContentTitle("Titulo")
                        .setContentText("Hola que tal?")
                        .setVibrate(new long[] {100, 250, 100, 500})
                        .setAutoCancel(true)
                        .setChannelId("Channel");

                System.out.println(mBuilder);

                mNotifyMgr.notify(1, mBuilder.build());

            }
        });


    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notify";
            String description = "Notificacion android";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            channel = new NotificationChannel("Channel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
