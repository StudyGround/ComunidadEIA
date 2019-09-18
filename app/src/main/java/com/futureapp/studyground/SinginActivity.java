package com.futureapp.studyground;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SinginActivity extends AppCompatActivity {

    public static Activity fa;

    private EditText txtemail;
    private EditText txtpwd;
    private EditText txtcPwd;
    private EditText txtname;
    private Button buttonReturn;
    private Spinner spinPrograma;
    private Spinner spinUniversidad;
    private Button buttonMaterias;

    //Datos a registrar
    private String name = "";
    private String email = "";
    private String pwd = "";
    private String cpwd = "";
    private String univ = "";
    private String programa = "";

    // Variables firebase
    FirebaseAuth auth;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Resources res = getResources();

        fa=this;

        //Cargar archivo xml


        //-------------------------------------------------------------------------------------------

        //instacia firebase auth
       auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        txtemail = (EditText) findViewById(R.id.txtemail);
        txtpwd = (EditText) findViewById(R.id.txtpwd);
        txtcPwd = (EditText) findViewById(R.id.txtcPwd);
        txtname = (EditText) findViewById(R.id.txtname);

        buttonReturn = (Button) findViewById(R.id.buttonReturn);
        spinUniversidad = (Spinner) findViewById(R.id.spinuniversidad);
        spinPrograma = (Spinner) findViewById(R.id.spinprograma);
        buttonMaterias = (Button) findViewById(R.id.buttonMaterias);






        spinPrograma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                programa = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinUniversidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                univ = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




      final String[] materias = res.getStringArray(R.array.mCienciasBasicas);

      buttonMaterias.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if(!programa.isEmpty()){
                  System.out.println("Programa btn"+programa);
                  String[] materiasp=EleccionPrograma(programa);


                  ArrayList<String> allMaterias = new ArrayList<String>();


                  for (int i=0;i<materias.length;i++){
                      allMaterias.add(materias[i]);
                  }


                  for (int i=0;i<materiasp.length;i++){
                      allMaterias.add(materiasp[i]);
                      System.out.println(i+" progra:" +materiasp[i]);
                  }

                  for (int i=0;i<allMaterias.size();i++){
                      System.out.println(i+"programa materias cb:"+allMaterias.get(i).toString());
                  }


                  //Datos para registro
                  email = txtemail.getText().toString();
                  pwd = txtpwd.getText().toString();
                  cpwd = txtcPwd.getText().toString();
                  name = txtname.getText().toString();




                  Intent intent = new Intent(SinginActivity.this, ElectMateriasActivity.class);



                  if (!name.isEmpty() && !email.isEmpty() && !pwd.isEmpty() && !cpwd.isEmpty() && !programa.isEmpty() && !univ.isEmpty()) {
                      if (pwd.length() >= 6) {
                          if (pwd.equals(cpwd)) {
                              intent.putStringArrayListExtra("materias", allMaterias);
                              intent.putExtra("email",email);
                              intent.putExtra("pwd",pwd);
                              intent.putExtra("name",name);
                              intent.putExtra("programa",programa);
                              intent.putExtra("universidad",univ);
                              startActivity(intent);
                          } else {
                              Toast.makeText(SinginActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                          }

                      } else {
                          Toast.makeText(SinginActivity.this, "La contraseña debe contener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                      }
                  } else {
                      Toast.makeText(SinginActivity.this, "Debes completar los campos", Toast.LENGTH_SHORT).show();
                  }

              }
          }
      });




        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SinginActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }



    private String[] EleccionPrograma(String programa){
        Resources res=getResources();

        String[] materiasp=new String[35];
        switch (programa){
            case "Ingeniería de Sistemas":
                materiasp=res.getStringArray(R.array.mSistemas);
                break;
            case "Ingeniería Civil":
                materiasp=res.getStringArray(R.array.mCivil);
                break;
            case "Ingeniería Administrativa":
                materiasp=res.getStringArray(R.array.mAdministrativa);
                break;
            case "Ingeniería Financiera":
                materiasp=res.getStringArray(R.array.mFinanciera);
                break;
            case "Ingeniería Geologica":
                materiasp=res.getStringArray(R.array.mGeologica);
                break;
            case "Ingeniería Industrial":
                materiasp=res.getStringArray(R.array.mIndustrial);
                break;
            case "Ingeniería Mecanica":
                materiasp=res.getStringArray(R.array.mMecanica);
                break;
            case "Ingeniería Mecatronica":
                materiasp=res.getStringArray(R.array.mMecatronica);
                break;
            case "Ingeniería Biomedica":
                materiasp=res.getStringArray(R.array.mBiomedica);
                break;
            case "Ingeniería Ambiental":
                materiasp=res.getStringArray(R.array.mAmbiental);
                break;
            case "Fisica":
                materiasp=res.getStringArray(R.array.mSistemas);
                break;
        }
        return materiasp;
    }
}
