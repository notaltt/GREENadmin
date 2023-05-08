package com.example.greeniqadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EventActivity extends AppCompatActivity {

    private EditText dateInput, dateInput2, title, details, shortDet;
    private ImageView calendarIcon, calendarIcon2, back;
    private MaterialButton confirm;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Event");

        title = findViewById(R.id.eventTxt);
        details = findViewById(R.id.detailsTxt);
        dateInput = findViewById(R.id.date_input);
        shortDet = findViewById(R.id.shortDesc);
        calendarIcon = findViewById(R.id.calendar_icon);
        dateInput2 = findViewById(R.id.date_input2);
        calendarIcon2 = findViewById(R.id.calendar_icon2);

        back = findViewById(R.id.backEvent);

        confirm = findViewById(R.id.confirmButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
            }
        });

        calendarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(EventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Update the text of the date input with the selected date
                        String selectedDate = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                        dateInput.setText(selectedDate);
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        calendarIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Update the text of the date input with the selected date
                        String selectedDate = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                        dateInput2.setText(selectedDate);
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fetchTitle = title.getText().toString();
                String fetchDetails = details.getText().toString();
                String fetchDate1 = dateInput.getText().toString();
                String fetchDate2 = dateInput2.getText().toString();
                String fetchShort = shortDet.getText().toString();

                if(fetchTitle.equals("") || fetchDetails.equals("") || fetchDate1.equals("") || fetchDate2.equals("") || fetchShort.equals("")){
                    title.setError("All fields must not empty");
                    details.setError("All fields must not empty");
                    dateInput.setError("All fields must not empty");
                    dateInput2.setError("All fields must not empty");
                    shortDet.setError("All fields must not empty");
                }else{
                    title.setError(null);
                    details.setError(null);
                    dateInput.setError(null);
                    dateInput2.setError(null);
                    shortDet.setError(null);

                    Event event = new Event(fetchTitle, fetchDetails, fetchDate1, fetchDate2, fetchShort);
                    databaseReference.setValue(event);

                    Toast.makeText(getApplicationContext(), "Posted Event!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}