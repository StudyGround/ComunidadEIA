package com.futureapp.studyground;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity {

    private final int DURACION_SPLASH = 3000; // 3 segundos


    FirebaseAuth auth2;
    FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference db;
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        auth2=FirebaseAuth.getInstance();



        new Handler().postDelayed(new Runnable() {
            public void run() {
                // Cuando pasen los 3 segundos, pasamos a la actividad principal de la aplicación
                if(auth2.getCurrentUser() != null){
                    FirebaseUser firebaseUser = auth2.getCurrentUser();
                    System.out.println("Estado de autentificacion - Logueado");
                    Intent intent = new Intent(MainActivity.this, ProfileMainActivity.class);
                    startActivity(intent);
                    finish();

                } else{

                    System.out.println("Estado de autentificacion - Cerró sesión");
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            ;
        }, DURACION_SPLASH);
    }


    private void inicialize() {
        auth2=FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = auth2.getCurrentUser();
                if (firebaseUser != null) {


                } else {

                }
            }
        };
    }


}
