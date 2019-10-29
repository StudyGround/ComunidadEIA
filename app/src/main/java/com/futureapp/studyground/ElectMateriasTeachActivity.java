package com.futureapp.studyground;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ElectMateriasTeachActivity extends AppCompatActivity  implements SearchView.OnQueryTextListener{


    Button btnMatStudy;
    SearchView searchView;

    // Variables firebase
    FirebaseAuth auth;
    DatabaseReference db;
    //Datos para registro
    String email="";
    String pwd="";
    String name="";
    String programa="";
    String ruta="";
    String phone="";
    String validRB="";

    AlertDialog.Builder builder;

    ArrayList<String> materiasEscogidasT =new ArrayList<String>() ;

    ListViewItemCheckboxBaseAdapter listViewDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elect_materias_teach);

        auth = FirebaseAuth.getInstance();



        db = FirebaseDatabase.getInstance().getReference();

        btnMatStudy =(Button) findViewById(R.id.btnMatTeach);

        builder = new AlertDialog.Builder(this);



        final ArrayList<String> listaMaterias = (ArrayList<String>) getIntent().getStringArrayListExtra("materias");

        email=getIntent().getStringExtra("email");
        pwd=getIntent().getStringExtra("pwd");
        name=getIntent().getStringExtra("name");
        programa=getIntent().getStringExtra("programa");
        ruta=getIntent().getStringExtra("ruta");
        phone=getIntent().getStringExtra("phone");
        validRB=getIntent().getStringExtra("radioBtn");


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
                    materiasEscogidasT.remove(itemDto.getItemText());


                }else
                {
                    itemCheckbox.setChecked(true);
                    itemDto.setChecked(true);
                    System.out.println("index checkbox:"+itemDto.getItemText());
                    String materia=itemDto.getItemText();
                    if(!itemDto.getItemText().isEmpty() && !materia.isEmpty()) {
                        System.out.println("index checkbox materia:"+materia );
                        materiasEscogidasT.add(materia);
                    }
                }

            }
        });



       btnMatStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.setMessage("Seras redirigido a las materias que vas a estudiar")
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(ElectMateriasTeachActivity.this, ElectMateriasActivity.class);
                                //intent
                                intent.putStringArrayListExtra("materiasT", materiasEscogidasT);
                                intent.putStringArrayListExtra("materias", listaMaterias);
                                intent.putExtra("email",email);
                                intent.putExtra("pwd",pwd);
                                intent.putExtra("name",name);
                                intent.putExtra("programa",programa);
                                intent.putExtra("ruta",ruta);
                                intent.putExtra("phone",phone);
                                intent.putExtra("radioBtn",validRB);
                                startActivity(intent);



                            }
                        });

                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("StudyGround");
                alert.setIcon(R.mipmap.ic_launcher);
                alert.show();








            }
        });



        searchView= (SearchView) findViewById(R.id.searchViewT);
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

}
