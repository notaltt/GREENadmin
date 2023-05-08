package com.example.health;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {
    EditText idNumber, studentName, phoneNumber;
    Button save, generate;
    DatabaseReference root;
    ImageView barcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = FirebaseDatabase.getInstance("https://health-teknoy-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        //EDIT TEXTS
        idNumber = findViewById(R.id.idNumber);
        studentName = findViewById(R.id.studentName);
        phoneNumber = findViewById(R.id.phoneNumber);

        //IMAGE
        barcode = findViewById(R.id.barcode);

        //BUTTON
        save = findViewById(R.id.buttonSave);
        generate = findViewById(R.id.buttonGenerate);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeNewUser();
            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = studentName.getText().toString().trim();
                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    BitMatrix matrix = writer.encode(name, BarcodeFormat.QR_CODE, 350, 350);
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    barcode.setImageBitmap(bitmap);
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(studentName.getApplicationWindowToken(), 0);
                } catch (WriterException e){
                    e.printStackTrace();
                }
            }
        });

    }

    //FIREBASE write new user
    public void writeNewUser() {
        User user = new User(idNumber.getText().toString(),
                            studentName.getText().toString(),
                            phoneNumber.getText().toString());

        root.child("users").child(user.getIdNumber()).setValue(user);
    }

}