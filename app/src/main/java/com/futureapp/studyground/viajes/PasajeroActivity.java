package com.futureapp.studyground.viajes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/*import com.example.colectivoeia.Models.Ruta;
import com.example.colectivoeia.adapters.RutaAdapter;*/
import com.futureapp.studyground.LoginActivity;
import com.futureapp.studyground.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PasajeroActivity extends AppCompatActivity {


    private DatabaseReference Rutas;

    AlertDialog.Builder builder;

    /*private RutaAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<Ruta> mRutasList = new ArrayList<>();*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasajero);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        builder = new AlertDialog.Builder(this);


        Rutas = FirebaseDatabase.getInstance().getReference();

        Rutas.child("Rutas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    String desde = dataSnapshot.child("desde").getValue().toString();
                    String hacia = dataSnapshot.child("hacia").getValue().toString();
                    String hora = dataSnapshot.child("hora").getValue().toString();
                    String precio = dataSnapshot.child("precio").getValue().toString();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewRutas);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getRutasfromFirebase();*/

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
                                Intent intent=new Intent(PasajeroActivity.this, LoginActivity.class);
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


    /*private void getRutasfromFirebase (){
        Rutas.child("Rutas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    mRutasList.clear();

                    for(DataSnapshot ds: dataSnapshot.getChildren()) {
                        String desde = ds.child("desde").getValue().toString();
                        mRutasList.add(new Ruta(desde));

                    }

                    mAdapter = new RutaAdapter(mRutasList, R.layout.rutas_view);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }*/
}
