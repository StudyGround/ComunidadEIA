package com.futureapp.studyground;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileMainActivity extends AppCompatActivity {

    private ImageButton teach;
    private ImageButton study;
    private TextView txtWelcome;
    private TextView txtMaterias;
    private Toolbar toolbar;

    FirebaseAuth auth;
    DatabaseReference db;

    ArrayList<String> arrayList=new ArrayList<>();
    ArrayList<String> arrayListTeach=new ArrayList<>();

    String tutor;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

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

                tutor=userPojo.getTutor();

                System.out.println("Datasnapshot tutor: "+tutor);

                if(tutor.equals("No")){
                    teach.setVisibility(0);
                }

                db.child("Materias").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                        arrayList=(ArrayList) dataSnapshot2.getValue();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("ERROR FIREBASE",databaseError.getMessage());
                    }
                });

                db.child("MateriasTeach").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                        arrayListTeach=(ArrayList) dataSnapshot3.getValue();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("ERROR FIREBASE",databaseError.getMessage());
                    }
                });


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
                intent.putStringArrayListExtra("materiasEsc",arrayList);
                intent.putExtra("option","estudiar");
                startActivity(intent);

            }
        });

        teach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileMainActivity.this, StudyActivity.class);
                intent.putStringArrayListExtra("materiasEsc",arrayListTeach);
                intent.putExtra("option","enseñar");
                startActivity(intent);

            }
        });






    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.SingOut:
                Toast.makeText(this,"Pulsaste el boton para cerrar sesion",Toast.LENGTH_LONG);
                break;
        }
        return true;
    }

}
