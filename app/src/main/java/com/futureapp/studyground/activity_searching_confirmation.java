package com.futureapp.studyground;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class activity_searching_confirmation extends AppCompatActivity {

    String materia,option;
    TextView txtBusqueda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching_confirmation);

        materia=getIntent().getStringExtra("materiaStudy");
        option=getIntent().getStringExtra("option");

        String text=getString(R.string.find_partner,option,materia);

        txtBusqueda=(TextView) findViewById(R.id.busqueda);

        txtBusqueda.setText(text);

    }
}
