package com.futureapp.studyground;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudyActivity extends AppCompatActivity {

    private ListView listView;
    private TextView txtStudyTeach;
    String opcionTS,op="";


    FirebaseAuth auth;
    DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        auth=FirebaseAuth.getInstance();
        String id=auth.getCurrentUser().getUid();
        db= FirebaseDatabase.getInstance().getReference("Users").child(id);
        final ArrayList<String> arrayList = (ArrayList<String>) getIntent().getStringArrayListExtra("materiasEsc");


        txtStudyTeach=(TextView) findViewById(R.id.textTwice);
        opcionTS=getIntent().getStringExtra("option");
        String option=getString(R.string.studyTeach,opcionTS);

        txtStudyTeach.setText(option);


        listView = (ListView) findViewById(R.id.listView);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = arrayList.get(position);
                Toast.makeText(getApplicationContext(), "You selected : " + item, Toast.LENGTH_SHORT).show();

                Intent intent =new Intent(StudyActivity.this,activity_searching_confirmation.class);
                intent.putExtra("materiaStudy",item);

                if(opcionTS.equals("enseñar")){
                    op="aprender";
                }else{
                    op="estudiar o enseñar ";
                }

                intent.putExtra("option",op);
                startActivity(intent);
            }
        });

    }
}
