package com.example.recycle_practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText name, phone;
    DatabaseReference reference;

    PersonAdp personAdp;

    List<Person> persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        fetchData();
    }

    private void init() {
        findViewById(R.id.addBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("person"); // firebase initialization

        recyclerView = findViewById(R.id.main_recycle_view); // initialize recycler
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void createDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_box);
        dialog.show();
        dialog.setCancelable(false);

        name = dialog.findViewById(R.id.name);
        phone = dialog.findViewById(R.id.phone);

        dialog.findViewById(R.id.okBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person person = new Person(name.getText().toString().trim(), phone.getText().toString().trim());
                if (isValid(person)) {
                    addToDb(person);
                    dialog.dismiss();
                }
            }
        });

        dialog.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void addToDb(Person person) {

        reference
                .child(person.getName()).setValue(person)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Data added successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private boolean isValid(Person person) {
        boolean valid = true;
        if (person.getName().length() < 3) {
            name.setError("enter valid name");
            valid = false;
        }
        if (person.getPhone().length() < 11 || person.getPhone().length() > 11) {
            phone.setError("enter valid phone no");
            valid = false;
        }
        return valid;
    }

    private void fetchData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                persons = new ArrayList<>();

                for (DataSnapshot i : snapshot.getChildren()) {
                    persons.add(i.getValue(Person.class));
                }

                setDataToRecycler(persons);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setDataToRecycler(List<Person> list)
    {
        personAdp = new PersonAdp(list,MainActivity.this);
        recyclerView.setAdapter(personAdp);
    }
}

