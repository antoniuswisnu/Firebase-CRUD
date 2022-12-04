package com.example.tugasfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText et1,et2,et3,et4;
    Button btn1,btn2,btn3, btn4;
    RadioGroup rdg;
    RadioButton rdb, rdb1, rdb2;

    DatabaseReference mDRStudent, mDRTeacher;
    String key;
    Student mStudent;
    Teacher mTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);

        rdb1 = findViewById(R.id.radioButton);
        rdb2 = findViewById(R.id.radioButton2);
        rdg = findViewById(R.id.radioGroup);

        mDRStudent = FirebaseDatabase.getInstance().getReference(Student.class.getSimpleName());
        mDRTeacher = FirebaseDatabase.getInstance().getReference(Teacher.class.getSimpleName());

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readData();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });
    }

    private boolean checkButton(String string){
        int radio = rdg.getCheckedRadioButtonId();
        rdb = findViewById(radio);
        return rdb.getText().equals(string);
    }

    private void insertData(){
        String name = et1.getText().toString();
        String address = et2.getText().toString();

        if (checkButton("Student")) {
            Student newStudent = new Student();
            newStudent.setName(name);
            newStudent.setAddress(address);

            mDRStudent.push().setValue(newStudent);
            Toast.makeText(MainActivity.this, "Data has been pushed", Toast.LENGTH_SHORT).show();
        } else if (checkButton("Teacher")){
            Teacher newTeacher = new Teacher();
            newTeacher.setTeacherName(name);
            newTeacher.setTeacherAddress(address);

            mDRTeacher.push().setValue(newTeacher);
            Toast.makeText(MainActivity.this, "Data has been pushed", Toast.LENGTH_SHORT).show();
        }
    }

    private void readData(){
        if(checkButton("Student")){
            mStudent = new Student();
            mDRStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChildren()){
                        for(DataSnapshot currentData : snapshot.getChildren()){
                            key = currentData.getKey();
                            mStudent.setName(currentData.child("name").getValue().toString());
                            mStudent.setAddress(currentData.child("address").getValue().toString());

                        }
                    }
                    et3.setText(mStudent.getName());
                    et4.setText(mStudent.getAddress());
                    Toast.makeText(MainActivity.this, "Data has been shown!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (checkButton("Teacher")){
            mTeacher = new Teacher();
            mDRTeacher.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChildren()){
                        for(DataSnapshot currentData : snapshot.getChildren()){
                            key = currentData.getKey();
                            mTeacher.setTeacherName(currentData.child("teacherName").getValue().toString());
                            mTeacher.setTeacherAddress(currentData.child("teacherAddress").getValue().toString());

                        }
                    }
                    et3.setText(mTeacher.getTeacherName());
                    et4.setText(mTeacher.getTeacherAddress());
                    Toast.makeText(MainActivity.this, "Data has been shown!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void updateData(){

        if(checkButton("Student")){
            Student updateData = new Student();
            updateData.setName(et3.getText().toString());
            updateData.setAddress(et4.getText().toString());

            mDRStudent.child(key).setValue(updateData);
            Toast.makeText(MainActivity.this, "Data has been updated!", Toast.LENGTH_SHORT).show();
        } else if (checkButton("Teacher")){
            Teacher updateData = new Teacher();
            updateData.setTeacherName(et3.getText().toString());
            updateData.setTeacherAddress(et4.getText().toString());

            mDRTeacher.child(key).setValue(updateData);
            Toast.makeText(MainActivity.this, "Data has been updated!", Toast.LENGTH_SHORT).show();
        }

    }

    private void deleteData(){

        if(checkButton("Student")){
            Student deleteData = new Student();
            deleteData.setName(et3.getText().toString());
            deleteData.setAddress(et4.getText().toString());

            mDRStudent.child(key).removeValue();
            Toast.makeText(MainActivity.this, "Data has been deleted!", Toast.LENGTH_SHORT).show();
        } else if (checkButton("Teacher")) {
            Teacher deleteData = new Teacher();
            deleteData.setTeacherName(et3.getText().toString());
            deleteData.setTeacherAddress(et4.getText().toString());

            mDRTeacher.child(key).removeValue();
            Toast.makeText(MainActivity.this, "Data has been deleted!", Toast.LENGTH_SHORT).show();
        }
    }
}