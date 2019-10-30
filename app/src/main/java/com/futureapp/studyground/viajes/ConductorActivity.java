package com.futureapp.studyground.viajes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.futureapp.studyground.LoginActivity;
import com.futureapp.studyground.ProfileMainActivity;
import com.futureapp.studyground.R;
import com.futureapp.studyground.SearchPartnerActivity;
import com.futureapp.studyground.UserPojo;
import com.futureapp.studyground.fcm.Data;
import com.futureapp.studyground.fcm.Notification;
import com.futureapp.studyground.fcm.Response;
import com.futureapp.studyground.fcm.Sender;
import com.futureapp.studyground.fcm.WS;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.widget.Toast.LENGTH_SHORT;

public class ConductorActivity extends AppCompatActivity {


    private Spinner spinOrigen;
    private Spinner spinDestino;
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    public final Calendar c = Calendar.getInstance();
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    EditText etHora, etPrecio;


    private DatabaseReference Rutas;
    FirebaseAuth auth;
    DatabaseReference db;
    String id,telefono,origen,destino,horaMap,precio,tDestino,tOrigen,name;
    Button btnRegistrar;
    ImageButton btnObtenerHora;
    LocationManager locationManager;
    double longitude, latitude;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductor);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        builder = new AlertDialog.Builder(this);

        Rutas = FirebaseDatabase.getInstance().getReference("Rutas");

        auth = FirebaseAuth.getInstance();
        id=auth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance().getReference("Users").child(id);

        btnRegistrar=(Button) findViewById(R.id.btnRegistrar);
        btnObtenerHora=(ImageButton) findViewById(R.id.ib_obtener_hora);

        //mapa google maps
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        ActivityCompat.requestPermissions(ConductorActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, /* Este codigo es para identificar tu request */ 1);

        if (ContextCompat.checkSelfPermission(ConductorActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
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

        btnObtenerHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerHora();
            }
        });


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<UserPojo> user=new GenericTypeIndicator<UserPojo>() {};
                        UserPojo userPojo=dataSnapshot.getValue(user);



                        telefono=userPojo.getTelefono();
                        name=userPojo.getName();



                        origen = spinOrigen.getSelectedItem().toString();
                        destino = spinDestino.getSelectedItem().toString();
                        horaMap = etHora.getText().toString();
                        precio = etPrecio.getText().toString();

                        tDestino=subscribeTopic(destino);
                        tOrigen=subscribeTopic(origen);

                        String msg=name+" abrió ruta desde: "+origen+" hasta: "+destino;

                        registarRuta(origen,destino,horaMap,precio,telefono,name);
                        enviarNotificacion("/topics/"+tDestino,msg,telefono,latitude,longitude,name);
                        enviarNotificacion("/topics/"+tOrigen,msg,telefono,latitude,longitude,name);

                        System.out.println("Datasnapshot telefono: "+telefono);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });






        spinOrigen = (Spinner) findViewById(R.id.spinnerOrigen);
       
        spinOrigen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String direc = (String) spinOrigen.getAdapter().getItem(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinDestino = (Spinner) findViewById(R.id.spinnerDestino);

        
        spinDestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String direc1 = (String) spinDestino.getAdapter().getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        etHora = (EditText) findViewById(R.id.et_mostrar_hora_picker);
        etPrecio = (EditText) findViewById(R.id.et_costo);

    }

    private void obtenerHora() {
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaFormateada = (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                String minutoFormateado = (minute < 10) ? String.valueOf(CERO + minute) : String.valueOf(minute);

                String AM_PM;
                if (hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }

        }, hora, minuto, false);
        recogerHora.show();
    }


   public void registarRuta(String origen, String destino, String hora, String precio, String telefono,String nombre) {



        if (!origen.isEmpty() && !destino.isEmpty()&& !hora.isEmpty()&& !precio.isEmpty()) {

            Map<String, Object> map = new HashMap<>();

            map.put("origen",origen);
            map.put("destino",destino);
            map.put("hora",hora);
            map.put("precio",precio);
            map.put("telefono",telefono);
            map.put("nombre",nombre);

            Rutas.child(telefono).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()) {

                        System.out.println("Datos subidos");

                    }else {

                    }

                }
            });


        } else {
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_LONG).show();
        }
    }






    public void enviarNotificacion(String topic,String msg ,String telefono , double lat, double lon, String name){
        System.out.println("TOPIC ENTRO A ENVIAR NOTI");



        System.out.println("TOPIC : "+topic);

        WS enviarPush= new Retrofit.Builder().baseUrl("https://fcm.googleapis.com/").addConverterFactory(GsonConverterFactory.create()).build().create(WS.class);

        //Data

        String longitud= Double.toString(lon);
        String latitud= Double.toString(lat);



        Notification notificacion=new Notification("Comunidad EIA",msg,"OPEN_ACTIVITY_1");
        Data data=new Data(latitud,longitud,name,telefono);

        Sender sender=new Sender(topic,notificacion,data,true);


        System.out.println("TOPIC data: "+data.getName()+data.getLatitude()+data.getLongitude()+"  tel: "+data.getTelefono());


        enviarPush.enviarNotificacion(sender).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                System.out.println("TOPIC entra on response");
                System.out.println("TOPIC satisfaccion: "+response.isSuccessful());


                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Mensaje enviado",Toast.LENGTH_LONG).show();
                    System.out.println("TOPIC MENSAJE ENVIADO");

                    Intent intent=new Intent(ConductorActivity.this, ProfileMainActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(),"Mensaje NO enviado", LENGTH_SHORT).show();
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
                                Intent intent=new Intent(ConductorActivity.this, LoginActivity.class);
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


}