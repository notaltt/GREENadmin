package com.example.greeniqadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CertificateActivity extends AppCompatActivity {

    private MaterialButton send;
    private TextInputLayout name, certificate;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);

        firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Certificate");

        send = findViewById(R.id.sendButton);
        name = findViewById(R.id.nameCert);
        certificate = findViewById(R.id.urlCert);
        back = findViewById(R.id.backCert);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fetchName = name.getEditText().getText().toString();
                String fetchUrl = certificate.getEditText().getText().toString();

                if(fetchName.equals("")){
                    name.setError("Fill this field.");
                }else if(fetchUrl.equals("")){
                    certificate.setError("Fill this field.");
                }else {
                    name.setError(null);
                    certificate.setError(null);
                    databaseReference.child(fetchName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                long childCount = snapshot.getChildrenCount();
                                String newChildNode = "url" + childCount;

                                databaseReference.child(fetchName).child(newChildNode).setValue(new CertificateAdmin(fetchUrl));
                                databaseReference.child(fetchName).child("default").removeValue();
                                databaseReference.child(fetchName).child("z*").setValue(new CertificateAdmin("https://firebasestorage.googleapis.com/v0/b/greeniq-ce821.appspot.com/o/certificate%2Fend.jpg?alt=media&token=b1d444fe-3dd2-4a00-ac79-ec468cd7d679"));
                            }else{
                                name.setError("Username does not exists.");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}