package com.example.greeniqadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CoinActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private TextInputLayout username, amount;
    private MaterialButton confirm;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);

        firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("User");

        username = findViewById(R.id.userCoin);
        amount = findViewById(R.id.amountCoin);
        back = findViewById(R.id.backCoin);

        confirm = findViewById(R.id.confirmButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fetchUsername = username.getEditText().getText().toString();
                String fetchAmount = amount.getEditText().getText().toString();

                if(fetchUsername.equals("")){
                    username.setError("All fields must not empty");
                }else if(fetchAmount.equals("")){
                    amount.setError("All fields must not empty");
                }else{
                    username.setError(null);
                    amount.setError(null);
                    databaseReference.orderByChild("userName").equalTo(fetchUsername)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        username.setError(null);
                                        if(fetchAmount.matches("[0-9]+")){
                                            amount.setError(null);
                                            box();
                                        }else{
                                            amount.setError("Input numbers only.");
                                        }
                                    }else{
                                        username.setError("Username does not exists.");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    System.out.println("Error: " + error);
                                }
                            });
                }
            }
        });
    }

    private void box() {
        String fetchUsername = username.getEditText().getText().toString();
        String fetchAmount = amount.getEditText().getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(CoinActivity.this);
        builder.setMessage("Are you sure sending "+fetchAmount+" coins to "+fetchUsername+"?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Query query = databaseReference.orderByChild("userName").equalTo(fetchUsername);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String currentValue = dataSnapshot.child("coin").getValue(String.class);
                            double total = Double.parseDouble(currentValue) + Double.parseDouble(fetchAmount);
                            dataSnapshot.getRef().child("coin").setValue(String.valueOf(total));
                        }
                        Toast.makeText(getApplicationContext(), "Sent "+fetchAmount+" coins to "+fetchUsername, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), CoinActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Failed to send coins", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}