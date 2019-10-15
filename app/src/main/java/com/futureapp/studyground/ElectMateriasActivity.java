package com.futureapp.studyground;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElectMateriasActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    Button btnRegistro;
    SearchView searchView;

    // Variables firebase
    FirebaseAuth auth;
    DatabaseReference db;
    //Datos para registro
    String email="";
    String pwd="";
    String name="";
    String programa="";
    String univ="";
    String phone="";
    String validRB="no";

    ArrayList<String> materiasEscogidas=new ArrayList<String>() ;
    ArrayList<String> materiasTeach=new ArrayList<String>() ;

    ListViewItemCheckboxBaseAdapter listViewDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elect_materias);

        auth = FirebaseAuth.getInstance();



        db = FirebaseDatabase.getInstance().getReference();

        btnRegistro=(Button) findViewById(R.id.btnRegistro);



        final ArrayList<String> listaMaterias = (ArrayList<String>) getIntent().getStringArrayListExtra("materias");

        email=getIntent().getStringExtra("email");
         pwd=getIntent().getStringExtra("pwd");
         name=getIntent().getStringExtra("name");
         programa=getIntent().getStringExtra("programa");
         univ=getIntent().getStringExtra("univ");
         phone=getIntent().getStringExtra("phone");
         validRB=getIntent().getStringExtra("radioBtn");

        if(validRB.equals("Si")){
            materiasTeach = (ArrayList<String>) getIntent().getStringArrayListExtra("materiasT");
        }


        setTitle("Asignaturas");

        // Get listview checkbox.
        final ListView listViewWithCheckbox = (ListView)findViewById(R.id.listViewCB);

        // Initiate listview data.
        final List<ListViewItemDTO> initItemList = this.getInitViewItemDtoList(listaMaterias);

        // Create a custom list view adapter with checkbox control.
        listViewDataAdapter = new ListViewItemCheckboxBaseAdapter(getApplicationContext(), initItemList);

        listViewDataAdapter.notifyDataSetChanged();

        // Set data adapter to list view.
        listViewWithCheckbox.setAdapter(listViewDataAdapter);

        // When list view item is clicked.
        listViewWithCheckbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
                // Get user selected item.
                Object itemObject = adapterView.getAdapter().getItem(itemIndex);

                // Translate the selected item to DTO object.
                ListViewItemDTO itemDto = (ListViewItemDTO)itemObject;

                // Get the checkbox.
                CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.list_view_item_checkbox);

                // Reverse the checkbox and clicked item check state.
                if(itemDto.isChecked())
                {
                    itemCheckbox.setChecked(false);
                    itemDto.setChecked(false);
                    System.out.println("index checkbox: err "+itemDto.getItemText());
                    materiasEscogidas.remove(itemDto.getItemText());


                }else
                {
                    itemCheckbox.setChecked(true);
                    itemDto.setChecked(true);
                    System.out.println("index checkbox:"+itemDto.getItemText());
                    String materia=itemDto.getItemText();
                    if(!itemDto.getItemText().isEmpty() && !materia.isEmpty()) {
                        System.out.println("index checkbox materia:"+materia );
                        materiasEscogidas.add(materia);
                    }
                }

            }
        });



        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    registerUser();


                    }
                });



        searchView= (SearchView) findViewById(R.id.searchView2);
        searchView.setOnQueryTextListener(this);




    }
    
    @Override
    public boolean onQueryTextSubmit(String query){
        return false;   
    }
    
    @Override
    public boolean onQueryTextChange(String newText){
        String text=newText;
        listViewDataAdapter.filter(text);
        return false;
    }    

    private void registerUser() {
        auth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    System.out.println("index registro fase 1 bien");
                    //Lista con valores para add to firebase
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("email", email);
                    map.put("pwd", pwd);
                    map.put("programa", programa);
                    map.put("universidad", univ);
                    map.put("telefono",phone);
                    map.put("tutor",validRB);

                    final String id = auth.getCurrentUser().getUid();

                    System.out.println("ID EMAIL"+id);

                    db.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()) {

                                System.out.println("index registro fase 2 bien");


                                if(validRB.equals(("Si"))){
                                    registerMateriasTeach(materiasTeach);
                                    subscribeTopicT(materiasTeach);
                                }

                                registerMaterias(materiasEscogidas);
                                subscribeTopic(materiasEscogidas);






                            } else {
                                Toast.makeText(ElectMateriasActivity.this, "No se pudieron crear los datos correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(ElectMateriasActivity.this, "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void registerMaterias(List materiasEscogidas){
        final String id = auth.getCurrentUser().getUid();

        db.child("Users").child(id).child("Materias").setValue(materiasEscogidas).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task3) {
                if(task3.isSuccessful()) {
                    System.out.println("index registro fase 3 bien");
                    Toast.makeText(ElectMateriasActivity.this, "Datos subidos correctamente", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ElectMateriasActivity.this, ProfileMainActivity.class));
                    SinginActivity.fa.finish();
                    finish();


                }else{
                    System.out.println("Fase 3 no exitosa");
                    Toast.makeText(ElectMateriasActivity.this, "Materias no subidas", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }



    private void registerMateriasTeach(List materiasEscogidas){
        final String id = auth.getCurrentUser().getUid();

        db.child("Users").child(id).child("MateriasTeach").setValue(materiasEscogidas).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task4) {
                if(task4.isSuccessful()) {
                    System.out.println("index registro fase 4 bien");

                }else{
                    System.out.println("Fase 4 no exitosa");
                    Toast.makeText(ElectMateriasActivity.this, "Materias a enseñar no subidas", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }


    // Return an initialize list of ListViewItemDTO.
    private List<ListViewItemDTO> getInitViewItemDtoList(List itemTextArr)
    {

        List<ListViewItemDTO> ret = new ArrayList<ListViewItemDTO>();

        int length = itemTextArr.size();

        for(int i=0;i<length;i++)
        {
            String itemText = itemTextArr.get(i).toString();

            ListViewItemDTO dto = new ListViewItemDTO();
            dto.setChecked(false);
            dto.setItemText(itemText);

            ret.add(dto);
        }

        return ret;
    }

    //Suscripcion a topics de materias
    private void subscribeTopic(final List materiasEscogidas) {

        for (int i=0;i<materiasEscogidas.size();i++) {

            final int j=i;
            final String matEscogida=materiasEscogidas.get(i).toString()
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

            // [START subscribe_topics]
            FirebaseMessaging.getInstance().subscribeToTopic(matEscogida)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            System.out.println("Suscrito a:"+matEscogida+" orden"+j);

                            if(j==materiasEscogidas.size()-1){
                                String msg = ("Suscrito");
                                if (!task.isSuccessful()) {
                                    msg = ("Fallo al suscribir");
                                }

                                Toast.makeText(ElectMateriasActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
            // [END subscribe_topics]
        }
    }

    //Suscripcion a topics de materias
    private void subscribeTopicT(final List materiasEscogidas) {

        for (int i=0;i<materiasEscogidas.size();i++) {

            final int j=i;
            final String matEscogida=materiasEscogidas.get(i).toString()
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

            // [START subscribe_topics]
            FirebaseMessaging.getInstance().subscribeToTopic(matEscogida+"_teach")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            System.out.println("Suscrito a:"+matEscogida+" orden"+j);

                            if(j==materiasEscogidas.size()-1){
                                String msg = ("Suscrito");
                                if (!task.isSuccessful()) {
                                    msg = ("Fallo al suscribir");
                                }

                                Toast.makeText(ElectMateriasActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
            // [END subscribe_topics]
        }
    }

}
