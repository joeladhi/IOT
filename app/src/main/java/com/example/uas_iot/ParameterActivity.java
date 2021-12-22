package com.example.uas_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ParameterActivity extends AppCompatActivity {

    Button btnSimpan;
    EditText pRuangan;
    EditText pObjek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);

        btnSimpan = findViewById(R.id.btnSimpan);
        pRuangan = findViewById(R.id.edtParamRuangan);
        pObjek = findViewById(R.id.edtParamObjek);


        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, Object> m =new HashMap<String, Object>();
                m.put("paramRuangan", Integer.parseInt(pRuangan.getText().toString()));
                m.put("paramObjek", Integer.parseInt(pObjek.getText().toString()));
                FirebaseDatabase.getInstance().getReference().child("node1").setValue(m);

                DatabaseReference onOffRef = FirebaseDatabase.getInstance().getReference("node1/statusSistem");
                DatabaseReference buzzerRef = FirebaseDatabase.getInstance().getReference("node1/kondisiBuzzer");
                DatabaseReference sRuanganRef = FirebaseDatabase.getInstance().getReference("node1/suhuRuangan");
                DatabaseReference sObjekRef = FirebaseDatabase.getInstance().getReference("node1/suhuObjek");

                onOffRef.setValue(0);
                buzzerRef.setValue(0);
                sRuanganRef.setValue(0);
                sObjekRef.setValue(0);

                startActivity(new Intent(ParameterActivity.this, MainActivity.class));
            }
        });
    }
}