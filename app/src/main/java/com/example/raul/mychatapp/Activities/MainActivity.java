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

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText pass;
    Button registerB;
    Button loginB;
    FirebaseAuth auth;
    ProgressBar progressBar;
    String getEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        email = (EditText) findViewById(R.id.emailEditT);
        pass = (EditText) findViewById(R.id.passEditT);
        registerB = (Button) findViewById(R.id.RegisterB);
        loginB = (Button) findViewById(R.id.LoginB);
        progressBar = (ProgressBar) findViewById(R.id.progBar);

        auth = FirebaseAuth.getInstance();

        registerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, register_activity.class);
                startActivity(i);
            }
        });
        progressBar.setVisibility(View.GONE);
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pass.getText().toString().length() == 0 || email.getText().length() == 0) {
                    Toast.makeText(MainActivity.this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show();
                }
                if (pass.getText().toString().length() < 4 || email.getText().length() < 4) {
                    Toast.makeText(MainActivity.this, "Email is too short.", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    auth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.INVISIBLE);
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "You are logged in.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, mainChat_activity.class);

                                getEmail = intent.putExtra("get_email", email.getText().toString()).toString();

                                startActivity(intent);
                                finish();
                            }
                            if (!task.isSuccessful()) {
                                if (pass.getText().toString().length() < 4) {
                                    pass.setError("Your password is too short.");
                                } else {
                                    Toast.makeText(MainActivity.this, "Authentification error." + task.getException(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });

    }
}
