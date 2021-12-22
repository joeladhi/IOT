package com.example.uas_iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ToggleButton OnOff;
    TextView sRuangan;
    TextView sObjek;
    TextView buzzer;
    TextView pRuangan;
    TextView pObjek;
    Button btnParam;

    String suhuRuangan;
    String suhuObjek;
    String paramRuangan;
    String paramObjek;
    String kondisiBuzzer;
    String kondisiSistem;
    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OnOff = findViewById(R.id.tglBtnOnOff);
        sRuangan = findViewById(R.id.txtSRuangan);
        sObjek = findViewById(R.id.txtSObjek);
        buzzer = findViewById(R.id.txtBuzzer);
        pRuangan = findViewById(R.id.txtPRuangan);
        pObjek = findViewById(R.id.txtPObjek);
        btnParam = findViewById(R.id.btnParam);


        dbref = FirebaseDatabase.getInstance().getReference();

        btnParam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ParameterActivity.class));
            }
        });

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                suhuRuangan = dataSnapshot.child("node1/suhuRuangan").getValue().toString();
                sRuangan.setText(suhuRuangan);

                suhuObjek = dataSnapshot.child("node1/suhuObjek").getValue().toString();
                sObjek.setText(suhuObjek);

                paramRuangan = dataSnapshot.child("node1/paramRuangan").getValue().toString();
                pRuangan.setText(paramRuangan);

                paramObjek = dataSnapshot.child("node1/paramObjek").getValue().toString();
                pObjek.setText(paramObjek);

                kondisiBuzzer = dataSnapshot.child("node1/alert").getValue().toString();
                if (kondisiBuzzer.equals("1")) {
                    buzzer.setText("Alarm Menyala");
                } else if (kondisiBuzzer.equals("0")) {
                    buzzer.setText("Alarm iddle");
                }

                kondisiSistem = dataSnapshot.child("node1/statusSistem").getValue().toString();
                if (kondisiSistem.equals("0")) {
                    OnOff.setChecked(false);
                } else {
                    OnOff.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        OnOff.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    DatabaseReference onOffRef = FirebaseDatabase.getInstance().getReference("node1/statusSistem");
                    onOffRef.setValue(1);
                } else {
                    DatabaseReference onOffRef = FirebaseDatabase.getInstance().getReference("node1/statusSistem");
                    DatabaseReference buzzerRef = FirebaseDatabase.getInstance().getReference("node1/kondisiBuzzer");
                    DatabaseReference sRuanganRef = FirebaseDatabase.getInstance().getReference("node1/suhuRuangan");
                    DatabaseReference sObjekRef = FirebaseDatabase.getInstance().getReference("node1/suhuObjek");
                    DatabaseReference pRuanganRef = FirebaseDatabase.getInstance().getReference("node1/paramRuangan");
                    DatabaseReference pObjekRef = FirebaseDatabase.getInstance().getReference("node1/paramObjek");
                    onOffRef.setValue(0);
                    buzzerRef.setValue(0);
                    sRuanganRef.setValue(0);
                    sObjekRef.setValue(0);
                    pRuanganRef.setValue(0);
                    pObjekRef.setValue(0);
                }
            }
        });
    }
}
