package com.futureapp.studyground;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private EditText txtphone;
    private Button buttonReturn;
    private Spinner spinPrograma;
    private Spinner spinUniversidad;
    private Button buttonMaterias;
    private RadioGroup rg;
    private RadioButton rbSi,rbNo;

    //Datos a registrar
    private String name = "";
    private String email = "";
    private String pwd = "";
    private String cpwd = "";
    private String univ = "";
    private String programa = "";
    private String phone="";
    private String rb="";
    private String msg="";
    private String validRB;


    AlertDialog.Builder builder;

    Intent intent;

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

        //instacia firebase auth
       auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        txtemail = (EditText) findViewById(R.id.txtemail);
        txtpwd = (EditText) findViewById(R.id.txtpwd);
        txtcPwd = (EditText) findViewById(R.id.txtcPwd);
        txtname = (EditText) findViewById(R.id.txtname);
        txtphone = (EditText) findViewById(R.id.txtPhone);

        buttonReturn = (Button) findViewById(R.id.buttonReturn);
        spinUniversidad = (Spinner) findViewById(R.id.spinuniversidad);
        spinPrograma = (Spinner) findViewById(R.id.spinprograma);
        buttonMaterias = (Button) findViewById(R.id.buttonMaterias);

        rg=(RadioGroup) findViewById(R.id.rg);
        rbNo=(RadioButton) findViewById(R.id.rbNo);
        rbSi=(RadioButton) findViewById(R.id.rbSi);

        builder = new AlertDialog.Builder(this);




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

              validRB=validarRadioButton();
              System.out.println("Radio button text: "+validRB);

              if(!programa.isEmpty()){
                  System.out.println("Programa btn"+programa);
                  String[] materiasp=EleccionPrograma(programa);


                 final ArrayList<String> allMaterias = new ArrayList<String>();


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
                  phone =txtphone.getText().toString();



                  if(validRB.equals("Si")){
                      msg=getString(R.string.redireccion,"enseñar");
                  }else if(validRB.equals("No")){
                      msg=getString(R.string.redireccion,"estudiar");

                  }

                  System.out.println("Radio msg: "+msg);


                  if (!name.isEmpty() && !email.isEmpty() && !pwd.isEmpty() && !cpwd.isEmpty() && !programa.isEmpty() && !univ.isEmpty() && !phone.isEmpty()) {
                      if (pwd.length() >= 6) {
                          if (pwd.equals(cpwd)) {




                              //alert dialog

                              //Setting message manually and performing action on button click
                              builder.setMessage(msg)
                                      .setCancelable(false)
                                      .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                          public void onClick(DialogInterface dialog, int id) {

                                              if(validRB.equals("Si")){
                                                   intent = new Intent(SinginActivity.this, ElectMateriasTeachActivity.class);

                                              }else if(validRB.equals("No")){
                                                  intent = new Intent(SinginActivity.this,ElectMateriasActivity.class);

                                              }

                                              //intent
                                              intent.putStringArrayListExtra("materias", allMaterias);
                                              intent.putExtra("email",email);
                                              intent.putExtra("pwd",pwd);
                                              intent.putExtra("name",name);
                                              intent.putExtra("programa",programa);
                                              intent.putExtra("ruta",univ);
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

    private String validarRadioButton(){
        if(rbNo.isChecked()){
            rb=rbNo.getText().toString();
        }else if(rbSi.isChecked()){
            rb=rbSi.getText().toString();
        }
        return rb;
    }



    private String[] EleccionPrograma(String programa){
        Resources res=getResources();

        String[] materiasp=new String[35];
        switch (programa){
            case "Ingeniería de Sistemas y Computación":
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
                materiasp=res.getStringArray(R.array.mFisica);
                break;
        }
        return materiasp;
    }




}
