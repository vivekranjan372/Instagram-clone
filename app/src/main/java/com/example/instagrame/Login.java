package com.example.instagrame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
private EditText email;
private EditText password;
private Button login;
private TextView register;
private AlertDialog alertDialog;
private FirebaseAuth auth;
private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login();
    }
    public void login()
    {
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        register=findViewById(R.id.registerNew);
        alertDialog =new AlertDialog.Builder(this).create();

        alertDialog.setTitle("Login Status");
        dialog=new ProgressDialog(this);
        auth=FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        Login.this,Registration.class).addFlags(Intent
                .FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Please Wait");
                dialog.show();
                String getEmail=email.getText().toString();
                String getPassword=password.getText().toString();
                if(TextUtils.isEmpty(getEmail)||
                        TextUtils.isEmpty(getPassword))
                {
                    alertDialog.setMessage("Empty is not Allowed");
                    alertDialog.show();
                    dialog.dismiss();
                }
                else
                {
                    userLogin(getEmail,getPassword);
                }
            }
        });

    }
    private void userLogin(String email ,String password)
    {
        auth.signInWithEmailAndPassword
                (email,password).
                addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        dialog.dismiss();
alertDialog.setMessage("you are login Successfully go for update details");
alertDialog.show();
dialog.dismiss();
Intent intent=new Intent(getApplicationContext(), MainFrame.class);

intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
startActivity(intent);
finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                alertDialog.setMessage(e.getMessage());
                alertDialog.show();
                dialog.dismiss();
            }
        });

    }

}
