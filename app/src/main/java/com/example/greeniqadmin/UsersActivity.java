package com.example.greeniqadmin;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.Objects;

public class UsersActivity extends AppCompatActivity {

    private TextInputLayout email, username, password, phone, location;
    private MaterialButton create, update, read, delete, instruction;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, databaseReferenceCarbon, databaseReferenceCertificate;
    private FirebaseAuth firebaseAuth;

    private String fetchEmail, fetchUsername, fetchPassword, fetchPhone, fetchLocation;
    private ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("User");
        databaseReferenceCarbon = firebaseDatabase.getReference("Carbon Data");
        databaseReferenceCertificate = firebaseDatabase.getReference("Certificate");

        firebaseAuth = FirebaseAuth.getInstance();


        email = findViewById(R.id.userEmail);
        username = findViewById(R.id.mangerUsername);
        password = findViewById(R.id.managePassword);
        phone = findViewById(R.id.managePhonenumber);
        location = findViewById(R.id.manageLocation);

        create = findViewById(R.id.createButton);
        update = findViewById(R.id.updateButton);
        read = findViewById(R.id.readButton);
        delete = findViewById(R.id.deleteButton);
        instruction = findViewById(R.id.instructionButton);

        back = findViewById(R.id.backUsers);

        instruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UsersActivity.this);
                builder.setMessage("CREATE \n" +
                        "Fill all fields to create an account \n\n" +
                        "READ \n" +
                        "Enter user email to display all of the user's data \n\n" +
                        "UPDATE \n" +
                        "Use READ first before updating user's data \n\n" +
                        "DELETE \n" +
                        "Enter user email to delete all of the user's data");
                builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
        
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAccount();
            }
        });
        
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readAccount();
            }
        });
        
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAccount();
            }
        });
    }

    private void deleteAccount(){
        fetchEmail = email.getEditText().getText().toString();

        if(fetchEmail.equals("")){
            email.setError("Fill this field");
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(UsersActivity.this);
            builder.setMessage("Are you deleting all data of "+fetchEmail+"?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Query query = databaseReference.orderByChild("email").equalTo(fetchEmail);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                dataSnapshot.getRef().removeValue();
                            }
                            Toast.makeText(getApplicationContext(), "Deleted "+fetchEmail, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), UsersActivity.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "Fail to delete "+fetchEmail, Toast.LENGTH_SHORT).show();
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

    private void readAccount() {
        fetchEmail = email.getEditText().getText().toString();

        if(fetchEmail.equals("")){
            email.setError("Fill this field");
        }else{
            Query query = databaseReference.orderByChild("email").equalTo(fetchEmail);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String valueUsername = dataSnapshot.child("userName").getValue(String.class);
                        String valuePassword = dataSnapshot.child("password").getValue(String.class);
                        String valuePhone = dataSnapshot.child("phoneNumber").getValue(String.class);
                        String valueLocation = dataSnapshot.child("location").getValue(String.class);

                        username.getEditText().setText(valueUsername);
                        password.getEditText().setText(valuePassword);
                        phone.getEditText().setText(valuePhone);
                        location.getEditText().setText(valueLocation);

                        Toast.makeText(getApplicationContext(), "Reading all data of "+fetchEmail, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Can't find "+fetchEmail, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateAccount() {
        fetchEmail = email.getEditText().getText().toString();
        fetchUsername = username.getEditText().getText().toString();
        fetchPassword = password.getEditText().getText().toString();
        fetchPhone = phone.getEditText().getText().toString();
        fetchLocation = location.getEditText().getText().toString();

        Query query = databaseReference.orderByChild("email").equalTo(fetchEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    dataSnapshot.getRef().child("userName").setValue(fetchUsername);
                    dataSnapshot.getRef().child("password").setValue(fetchPassword);
                    dataSnapshot.getRef().child("phone").setValue(fetchPhone);
                    dataSnapshot.getRef().child("location").setValue(fetchLocation);

                    Toast.makeText(getApplicationContext(), "Updating data of "+fetchEmail, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createAccount() {
        fetchEmail = email.getEditText().getText().toString();
        fetchUsername = username.getEditText().getText().toString();
        fetchPassword = password.getEditText().getText().toString();
        fetchPhone = phone.getEditText().getText().toString();
        fetchLocation = location.getEditText().getText().toString();

        if(fetchEmail.equals("") || fetchPassword.equals("") || fetchUsername.equals("") || fetchLocation.equals("") || fetchPhone.equals("")){
            email.setError("All fields must not empty");
            username.setError("All fields must not empty");
            password.setError("All fields must not empty");
            phone.setError("All fields must not empty");
            location.setError("All fields must not empty");
        } else{
            email.setError(null);
            username.setError(null);
            password.setError(null);
            phone.setError(null);
            location.setError(null);

            databaseReference.orderByChild("userName").equalTo(fetchUsername)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                username.setError("Username already exists.");
                            }else{
                                username.setError(null);
                                if(fetchEmail.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")){
                                    if(fetchPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$")){
                                        if(fetchPhone.matches("^(09|\\+639)\\d{9}$")){
                                            try {
                                                firebaseAuth.createUserWithEmailAndPassword(fetchEmail, fetchPassword).addOnCompleteListener(task -> {
                                                    if(task.isSuccessful()){
                                                        FirebaseUser user = task.getResult().getUser();
                                                        String userId = Objects.requireNonNull(user).getUid();
                                                        String emptyCert = "https://firebasestorage.googleapis.com/v0/b/greeniq-ce821.appspot.com/o/certificate%2FUntitled-1.jpg?alt=media&token=5d9cb3f7-af0c-45a5-937c-4bc4520e6574";
                                                        String defaultProfile = "https://firebasestorage.googleapis.com/v0/b/greeniq-ce821.appspot.com/o/images%2F1e98af88-102c-4ffd-a85c-c450162cd7d7?alt=media&token=d1d09296-b020-422b-a584-2fb40719bb66";
                                                        databaseReference.child(userId).setValue(new ProfileAdmin(fetchUsername, fetchPhone, defaultProfile, fetchLocation, fetchEmail, fetchPassword, userId, "0"));
                                                        databaseReferenceCarbon.child(userId).setValue(new CarbonDataAdmin("1", "1", "1"));
                                                        databaseReferenceCertificate.child(fetchUsername).child("default").setValue(new CertificateAdmin(emptyCert));
                                                        startActivity(new Intent(getApplicationContext(), UsersActivity.class));
                                                        Toast.makeText(getApplicationContext(),"Created an account for, "+fetchUsername+"!", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(),"Registration failed!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            } catch (NullPointerException e){
                                                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            } catch (IllegalArgumentException e){
                                                Toast.makeText(getApplicationContext(),"You've left a field empty!", Toast.LENGTH_SHORT).show();
                                            }
                                            phone.setError(null);
                                        }else{
                                            phone.setError("Invalid Phone number!");
                                        }
                                        password.setError(null);
                                    }else{
                                        password.setError("Should have one uppercase and lowercase character, one digit number, and one special character.");
                                    }
                                    email.setError(null);
                                }else{
                                    email.setError("Invalid Email!");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }
}