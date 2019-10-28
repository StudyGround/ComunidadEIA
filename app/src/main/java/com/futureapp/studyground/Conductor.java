package com.example.colectivoeia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Conductor extends AppCompatActivity {


    private Spinner mSpinner;
    private Spinner mSpinner1;
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    public final Calendar c = Calendar.getInstance();
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    EditText etHora, etPrecio;
    private DatabaseReference Rutas;
    //Button btnRegistrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductor);

        Rutas = FirebaseDatabase.getInstance().getReference();


        mSpinner = (Spinner) findViewById(R.id.mSpinner);
        ArrayList<String> direcciones = new ArrayList<String>();
        direcciones.add("-");
        direcciones.add("Exposiciones");
        direcciones.add("Aguacatala");
        direcciones.add("Zúñiga");
        direcciones.add("Rionegro");
        direcciones.add("Universidad EIA, Palmas");
        ArrayAdapter adp = new ArrayAdapter(Conductor.this, android.R.layout.simple_spinner_dropdown_item, direcciones);
        mSpinner.setAdapter(adp);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String direc = (String) mSpinner.getAdapter().getItem(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinner1 = (Spinner) findViewById(R.id.mSpinner1);
        ArrayList<String> direcciones1 = new ArrayList<String>();
        direcciones1.add("-");
        direcciones1.add("Exposiciones");
        direcciones1.add("Aguacatala");
        direcciones1.add("Zúñiga");
        direcciones1.add("Rionegro");
        direcciones1.add("Universidad EIA, Palmas");
        ArrayAdapter adp1 = new ArrayAdapter(Conductor.this, android.R.layout.simple_spinner_dropdown_item, direcciones1);
        mSpinner1.setAdapter(adp1);
        mSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String direc1 = (String) mSpinner1.getAdapter().getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        etHora = (EditText) findViewById(R.id.et_mostrar_hora_picker);
        etPrecio = (EditText) findViewById(R.id.et_costo);
        // btnRegistrar = (Button) findViewById(R.id.btnRegistrar);


    }

    private void obtenerHora() {
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaFormateada = (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                String minutoFormateado = (minute < 10) ? String.valueOf(CERO + minute) : String.valueOf(minute);

                String AM_PM;
                if (hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }

        }, hora, minuto, false);
        recogerHora.show();
    }

    public void UwU(View view) {
        obtenerHora();
    }

   public void registarRuta() {

        String desde = mSpinner.getSelectedItem().toString();
        String hacia = mSpinner1.getSelectedItem().toString();
        String hora = etHora.getText().toString();
        String precio = etPrecio.getText().toString();

        if (!desde.isEmpty() && !hacia.isEmpty()&& !hora.isEmpty()&& !precio.isEmpty()) {

            Rutas lecciones = new Rutas(hora, precio, desde, hacia);
            Rutas.child("Rutas").setValue(lecciones);
            Toast.makeText(this, "Ruta registrada", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_LONG).show();
        }
    }
public void UwU1 (View view){
        registarRuta();
}

public String OwO(){
        String hora= etHora.getText().toString();
        return hora;
        }

}