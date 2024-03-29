package com.futureapp.studyground;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.futureapp.studyground.fcm.Notification;
import com.futureapp.studyground.fcm.Response;
import com.futureapp.studyground.fcm.Sender;
import com.futureapp.studyground.fcm.WS;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudyActivity extends AppCompatActivity {

    private ListView listView;
    private TextView txtStudyTeach;
    String opcionTS,op="";


    FirebaseAuth auth;
    DatabaseReference db;


    NotificationChannel channel;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

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


                Intent intent =new Intent(StudyActivity.this, SearchPartnerActivity.class);
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

        builder = new AlertDialog.Builder(this);


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
                                Intent intent=new Intent(StudyActivity.this,LoginActivity.class);
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




    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Mensaje";
            String description = "Notificacion android";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            channel = new NotificationChannel("Channel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
