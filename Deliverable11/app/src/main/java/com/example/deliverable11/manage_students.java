package com.example.deliverable11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class manage_students extends AppCompatActivity {
    EditText name_entry;
    ImageButton add_btn, del_btn;
    FirebaseDatabase database;
    DatabaseReference reference;
    TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);

        name_entry = findViewById(R.id.enter_stud_name_editText);
        del_btn = findViewById(R.id.delete_stud_btn);
        add_btn = findViewById(R.id.add_stud_button);
        display = findViewById(R.id.student_display);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("User").child("Student");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Student student = snapshot.getValue(Student.class);
                    String textDisplay = "";
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        textDisplay = textDisplay + ds.getValue(Student.class).toString() + "\n";
                    }
                    display.setText(textDisplay);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };

        reference.addValueEventListener(postListener);

        del_btn.setOnClickListener(new View.OnClickListener() {
            String name = name_entry.getText().toString().trim();
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name)) {
                    name_entry.setError("Name is required");
                } else {
                    //REMOVE FROM DATABASE
                    database.getReference("User").child("Student").child(name).removeValue();
                    name_entry.setText("");
                }
            }
        });
        reference.addValueEventListener(postListener);
    }
}
