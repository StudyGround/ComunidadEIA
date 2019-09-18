package com.futureapp.studyground;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileMainActivity extends AppCompatActivity {

    private ImageButton teach;
    private ImageButton study;
    private TextView txtWelcome;

    FirebaseAuth auth;
    DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_profile_main);

        auth = FirebaseAuth.getInstance();
        String id=auth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance().getReference("Users").child(id);


        //read firebase database (real time)
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<UserPojo> user=new GenericTypeIndicator<UserPojo>() {};
                UserPojo userPojo=dataSnapshot.getValue(user);

                String text=getString(R.string.welcome_messages,userPojo.getName());
                String materias[]=userPojo.getMaterias();
                String mm="Materias escogidas: ";
                for(int i=0;i<materias.length;i++){
                    System.out.println("materia"+(i+1)+" : "+materias[i]);
                    mm.concat(materias[i]);
                }

                //mostramos en el textview
                txtWelcome.setText(text);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ERROR FIREBASE",databaseError.getMessage());
            }
        });





        teach=(ImageButton) findViewById(R.id.teachButton);
        study=(ImageButton) findViewById(R.id.studyButton);
        txtWelcome=(TextView) findViewById(R.id.welcome);

        study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileMainActivity.this, StudyActivity.class);
                startActivity(intent);

            }
        });

        teach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileMainActivity.this, StudyActivity.class);
                startActivity(intent);

            }
        });






    }

}
