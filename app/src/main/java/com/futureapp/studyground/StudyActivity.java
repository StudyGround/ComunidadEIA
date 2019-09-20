package com.futureapp.studyground;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class StudyActivity extends AppCompatActivity {

    private ListView listView;
    private TextView txtStudyTeach;
    String opcionTS,op="";


    FirebaseAuth auth;
    DatabaseReference db;


    NotificationChannel channel;

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

                Intent intent =new Intent(StudyActivity.this, SearchPartnerActivity.class);
                intent.putExtra("materiaStudy",item);

                if(opcionTS.equals("enseñar")){
                    op="aprender";
                }else{
                    op="estudiar o enseñar ";
                }

                intent.putExtra("option",op);


             /*   NotificationCompat.Builder mBuilder;
                NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);


                createNotificationChannel();

                int icono = R.mipmap.ic_launcher;
                Intent i=new Intent(StudyActivity.this, MapsActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(StudyActivity.this, 0, i, 0);

                mBuilder =new NotificationCompat.Builder(getApplicationContext())
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(icono)
                        .setContentTitle("StudyGround")
                        .setContentText("Hola, hay alguien que quiere estudiar, "+item)
                        .setVibrate(new long[] {100, 250, 100, 500})
                        .setAutoCancel(true)
                        .setChannelId("Channel");

                System.out.println(mBuilder);

                mNotifyMgr.notify(1, mBuilder.build());*/


                startActivity(intent);
            }
        });

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
