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
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*import com.example.colectivoeia.Models.Ruta;
import com.example.colectivoeia.adapters.RutaAdapter;*/
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.futureapp.studyground.LoginActivity;
import com.futureapp.studyground.R;
import com.futureapp.studyground.UserPojo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PasajeroActivity extends AppCompatActivity {


    private DatabaseReference Rutas;

    ArrayList<ArrayList<String>> listaRutas=new ArrayList<>();

    RecyclerView recyclerDB;

    AlertDialog.Builder builder;

    String destino,origen,precio,telefono="",nombre,hora,msg;

    ArrayList<String> dataRutas=new ArrayList<>();
    ArrayList<String> telefonos=new ArrayList<>();
    Map<String,String> map=new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasajero);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        builder = new AlertDialog.Builder(this);




        Rutas =FirebaseDatabase.getInstance().getReference().child("Rutas");

        recyclerDB = (RecyclerView) findViewById(R.id.recyclerDB);

        recyclerDB.setHasFixedSize(true);
        recyclerDB.setLayoutManager(new LinearLayoutManager(this));

        final FirebaseRecyclerAdapter mAdapter = new FirebaseRecyclerAdapter<viajesPojo, ViajesHolder>(
                viajesPojo.class, R.layout.activity_recycler, ViajesHolder.class, Rutas) {

            @Override
            public DatabaseReference getRef(int position) {
                System.out.println("RUTAS 2 telefono en touch object: "+telefono);
                return super.getRef(position);
            }

            @Override
            public int getItemViewType(int position) {
                return super.getItemViewType(position);
            }

            @Override
            protected void populateViewHolder(ViajesHolder viajesHolder, viajesPojo viajesPojo, final int i) {
                viajesHolder.setDestino(viajesPojo.getDestino());
                viajesHolder.setHora(viajesPojo.getHora());
                viajesHolder.setOrigen(viajesPojo.getOrigen());
                viajesHolder.setPrecio(viajesPojo.getPrecio());
                viajesHolder.setTelefono(viajesPojo.getTelefono());
                viajesHolder.setNombre(viajesPojo.getNombre());



                telefonos.add(viajesPojo.getTelefono());

                setTelefonos(telefonos);

                map.put((Integer.toString(i)),viajesPojo.getTelefono());

                System.out.println("RUTAS telefonos array list: "+telefonos.toString());

            }

            @Override
            public viajesPojo getItem(int position) {
                return super.getItem(position);
            }
        };

        recyclerDB.setAdapter(mAdapter);



    }

    public ArrayList<String> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(ArrayList<String> telefonos) {
        this.telefonos = telefonos;
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


    private void getRutasfromFirebase (){
        Rutas.child("Rutas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    listaRutas.clear();

                    for(DataSnapshot ds: dataSnapshot.getChildren()) {
                        GenericTypeIndicator<viajesPojo> user=new GenericTypeIndicator<viajesPojo>() {};
                        viajesPojo viajesPojo=ds.getValue(user);

                        nombre=viajesPojo.getNombre();
                        telefono=viajesPojo.getTelefono();
                        origen=viajesPojo.getOrigen();
                        destino=viajesPojo.getDestino();
                        precio=viajesPojo.getPrecio();

                        hora=viajesPojo.getHora();


                        msg=    "Nombre: "+nombre+" ,teléfono: "+telefono+
                                "\n Origen: "+origen+" ,destino: "+destino+
                                "\n Hora: "+hora +
                                "\n valor:"+precio;

                        System.out.println("RUTAS: msg: "+msg);

                        //txtRuta.setText(msg);

                        dataRutas.add(destino);
                        dataRutas.add(origen);
                        dataRutas.add(precio);
                        dataRutas.add(nombre);
                        dataRutas.add(hora);
                        dataRutas.add(telefono);
                        dataRutas.add(msg);

                        listaRutas.add(dataRutas);

                        System.out.println("RUTAS: "+listaRutas);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
