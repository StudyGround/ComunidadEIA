package com.futureapp.studyground;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    private ArrayAdapter<String> adapter;

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



        listView = (ListView) findViewById(R.id.listView);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(adapter);

        arrayList.add("qwerty");
        arrayList.add("123456");

       db.child("Materias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //arrayList=(ArrayList) dataSnapshot.getValue();

               /* for (int i=0;i<arrayList.size();i++){
                    System.out.println("Materia en array list: "+arrayList.get(i));
                    adapter.notifyDataSetChanged();
                }*/


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ERROR FIREBASE",databaseError.getMessage());
            }
        });


        for (int i=0;i<arrayList.size();i++){
            System.out.println("Materia en array list 222 : "+i+":"+arrayList.get(i));
        }





    }
}
