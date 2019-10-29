package com.futureapp.studyground.viajes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

/*import com.example.colectivoeia.Models.Ruta;
import com.example.colectivoeia.adapters.RutaAdapter;*/
import com.futureapp.studyground.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PasajeroActivity extends AppCompatActivity {


    private DatabaseReference Rutas;
    private TextView mTextView;

    /*private RutaAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<Ruta> mRutasList = new ArrayList<>();*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasajero);


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
