package com.example.raul.mychatapp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.raul.mychatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class register_activity extends AppCompatActivity {

    EditText pass;
    EditText email;
    EditText username;
    Button registerB;
    FirebaseAuth auth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        auth = FirebaseAuth.getInstance();
        pass = (EditText) findViewById(R.id.passEditT);
        email = (EditText) findViewById(R.id.emailEditT);
        progressBar = (ProgressBar) findViewById(R.id.progBar2);
        username = (EditText) findViewById(R.id.usernameEt);



        registerB = (Button) findViewById(R.id.RegisterB);
        progressBar.setVisibility(View.GONE);
        registerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass.getText().toString().length() < 4 || email.getText().length() < 4) {
                    pass.setError("Your password is too short.");
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(register_activity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(register_activity.this, "You are successfully registered.", Toast.LENGTH_SHORT).show();
                                DatabaseReference root= FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());
                                progressBar.setVisibility(View.VISIBLE);

                                Map<String,Object> map = new HashMap<String, Object>();
                                map.put("Email",email.getText().toString());
                                map.put("Username",username.getText().toString());
                                root.updateChildren(map); // Users-> test


                                Intent i = new Intent(register_activity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                            if (!task.isSuccessful()) {
                                Toast.makeText(register_activity.this, "Authentication failed." + task.getException(), Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }
}
