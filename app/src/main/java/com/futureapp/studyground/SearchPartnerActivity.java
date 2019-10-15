package com.futureapp.studyground;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.futureapp.studyground.fcm.Notification;
import com.futureapp.studyground.fcm.Response;
import com.futureapp.studyground.fcm.Sender;
import com.futureapp.studyground.fcm.WS;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchPartnerActivity extends AppCompatActivity {

    private static final String TAG = "search";

    String materia,option;
    TextView txtBusqueda, txtLongitud;

    NotificationChannel channel;

    AlertDialog.Builder builder;


    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching_confirmation);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        materia=getIntent().getStringExtra("materiaStudy");
        option=getIntent().getStringExtra("option");

        String text=getString(R.string.find_partner,option,materia);

        txtBusqueda=(TextView) findViewById(R.id.busqueda);

        txtBusqueda.setText(text);


        enviarNotificacion(materia);


     /*   //notificacion

        NotificationCompat.Builder mBuilder;
        NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);


        createNotificationChannel();

        int icono = R.mipmap.ic_launcher;
        Intent i=new Intent(SearchPartnerActivity.this, MapsActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(SearchPartnerActivity.this, 0, i, 0);



        mBuilder =new NotificationCompat.Builder(getApplicationContext())
                .setContentIntent(pendingIntent)
                .setSmallIcon(icono)
                .setContentTitle("StudyGround")
                .setContentText("Hola, hay alguien que quiere estudiar, "+materia)
                .setVibrate(new long[] {100, 250, 100, 500})
                .setAutoCancel(true)
                .setChannelId("Channel");

        System.out.println(mBuilder);

        mNotifyMgr.notify(1, mBuilder.build());



        builder = new AlertDialog.Builder(this);*/










    }

    public void enviarNotificacion(String materia){
        System.out.println("TOPIC ENTRO A ENVIAR NOTI");

        String topic="/topic/"+subscribeTopic(materia);
        System.out.println("TOPIC : "+topic);

        WS enviarPush= new Retrofit.Builder().baseUrl("https://fcm.googleapis.com/").addConverterFactory(GsonConverterFactory.create()).build().create(WS.class);


        Notification notificacion=new Notification("Studyground","alguien quiere estudiar "+materia.toLowerCase()+" contigo");
        Sender sender=new Sender(topic,notificacion);

        enviarPush.enviarNotificacion(sender).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                System.out.println("TOPIC entra on response");
                if(response.body().getSuccess()==1){
                    Toast.makeText(getApplicationContext(),"Mensaje enviado",Toast.LENGTH_LONG).show();
                    System.out.println("TOPIC MENSAJE ENVIADO");
                }else{
                    Toast.makeText(getApplicationContext(),"Mensaje NO enviado",Toast.LENGTH_SHORT).show();
                    System.out.println("TOPIC MENSAJE NO ENVIADO");
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                System.out.println("TOPIC entra on failure throw: "+t+" call: "+call);
            }
        });

    }

    private String subscribeTopic(final String materiasEscogida) {

        return  materiasEscogida
                .replace(" ","_")
                .replace("á","a")
                .replace("é","e")
                .replace("í","i")
                .replace("ó","o")
                .replace("ú","u")
                .replace("ñ","n")
                .replace(",","_")
                .replace("|","I")
                .replace("Á","A")
                .replace("É","E")
                .replace("Í","I")
                .replace("Ó","O")
                .replace("Ú","U")
                .replace("Ñ","N");

    }









    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.SingOut:
                builder.setMessage("¿Estas seguro de cerrar sesión?")
                        .setCancelable(true)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent=new Intent(SearchPartnerActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("StudyGround");
                alert.setIcon(R.mipmap.ic_launcher);
                alert.show();
                break;
        }
        return true;
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Mensaje";
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


