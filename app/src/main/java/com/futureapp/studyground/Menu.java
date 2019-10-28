package com.example.colectivoeia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }
    public void conductor (View view){
        Intent uwu=new Intent(getApplication(), Conductor.class);
        startActivity(uwu);
    }
    public void pasajero (View view){
        Intent uwu1=new Intent(getApplication(), Pasajero.class);
        startActivity(uwu1);
    }
}
