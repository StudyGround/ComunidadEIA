package com.futureapp.studyground;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.futureapp.studyground.fcm.Data;
import com.futureapp.studyground.fcm.Notification;
import com.futureapp.studyground.fcm.Response;
import com.futureapp.studyground.fcm.Sender;
import com.futureapp.studyground.fcm.WS;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchPartnerActivity extends AppCompatActivity {

    private static final String TAG = "search";

    String materia,option,msg,topic;
    TextView txtBusqueda, txtLongitud;

    NotificationChannel channel;

    AlertDialog.Builder builder;


    Bundle bundle;

    FirebaseAuth auth;
    DatabaseReference db;
    String id,token="",telefono="",name="";

    double longitudeGPS,latitudeGPS, longitude, latitude;

    LocationManager locationManager;

    Map<String,String> map=new HashMap<>();

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

        builder = new AlertDialog.Builder(this);

        //MAPA

        //mapa google maps
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        ActivityCompat.requestPermissions(SearchPartnerActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, /* Este codigo es para identificar tu request */ 1);

        if (ContextCompat.checkSelfPermission(SearchPartnerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }



        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location.getLatitude()!=0 && location.getLongitude()!=0) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }else{

        }
        System.out.println("GPS dd lon"+longitude+" lat: "+latitude);


        auth = FirebaseAuth.getInstance();
        id=auth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance().getReference("Users").child(id);


        //read firebase database (real time)
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<UserPojo> user = new GenericTypeIndicator<UserPojo>() {};
                UserPojo userPojo = dataSnapshot.getValue(user);

                token=userPojo.getToken();
                telefono=userPojo.getTelefono();
                name=userPojo.getName();
                System.out.println("TOPIC phone: "+telefono);

                if(longitude!=0 && latitude!=0) {
                    System.out.println("GPS bb lonLat" + longitude + " , " + latitude);
                    System.out.println("TOPIC phone2: "+telefono);



                    if(option.equals("aprender")){
                        msg=name+" quiere enseñar "+materia.toLowerCase();


                    }else{
                        msg=name+" quiere aprender "+materia.toLowerCase();
                        topic="/topics/"+subscribeTopic(materia)+"_teach";
                        enviarNotificacion(topic,msg ,telefono,latitude,longitude,name);

                        msg=name+" quiere estudiar "+materia.toLowerCase();
                    }

                    topic="/topics/"+subscribeTopic(materia);
                    enviarNotificacion(topic,msg ,telefono,latitude,longitude,name);

                }else{
                    System.out.println("GPS cc lonLat" + longitude + " , " + latitude);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });










    }



    public void enviarNotificacion(String topic,String msg ,String telefono , double lat, double lon, String name){
        System.out.println("TOPIC ENTRO A ENVIAR NOTI");



        System.out.println("TOPIC : "+topic);

        WS enviarPush= new Retrofit.Builder().baseUrl("https://fcm.googleapis.com/").addConverterFactory(GsonConverterFactory.create()).build().create(WS.class);

        //Data

        String longitud= Double.toString(lon);
        String latitud= Double.toString(lat);



        Notification notificacion=new Notification("Studyground",msg);
        Data data=new Data(latitud,longitud,name,telefono);

        Sender sender=new Sender(topic,notificacion,data);


        System.out.println("TOPIC data: "+data.getName());


        enviarPush.enviarNotificacion(sender).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                System.out.println("TOPIC entra on response");
                System.out.println("TOPIC satisfaccion: "+response.isSuccessful());


                if(response.isSuccessful()){
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


