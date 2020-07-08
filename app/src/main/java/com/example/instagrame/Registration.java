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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registration extends AppCompatActivity {
private EditText userName;
private EditText name;
private EditText email;
private EditText password;
private EditText conPassword;
private Button submit;
private TextView loginUser;
private AlertDialog alertDialog;
private DatabaseReference databaseReference;
private FirebaseAuth auth;
private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registrationMethod();
        databaseReference= FirebaseDatabase.
                getInstance().getReference();


    }
    public void registrationMethod()
    {
        userName=findViewById(R.id.userName);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        conPassword=findViewById(R.id.conPassword);
        loginUser=findViewById(R.id.alreadyUser);
        progressDialog=new ProgressDialog(this);
auth=FirebaseAuth.getInstance();
        submit=findViewById(R.id.register);
        alertDialog =new AlertDialog.Builder
                (Registration.this).create();
        alertDialog.setTitle("Registration Status");

        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent
                        (Registration.this,
                        Login.class));
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait");
                progressDialog.show();
                String getUserName,getName,getPassword,
                        getConPassword,getEmail;
                getUserName=userName.getText().toString();
                getName=name.getText().toString();
                getEmail=email.getText().toString();
                getPassword=password.getText().toString();
                getConPassword=conPassword.getText().toString();
                if(TextUtils.isEmpty(getUserName)||
                        TextUtils.isEmpty(getName)||
                        TextUtils.isEmpty(getPassword)||
                        TextUtils.isEmpty(getEmail)||
                        TextUtils.isEmpty(getConPassword)
                )
                {
alertDialog.setMessage("Empty is not Allowed !");
alertDialog.show();
progressDialog.dismiss();
                }
                else if((getPassword.length())<6 )
                {
                    alertDialog.setMessage
                            ("Password is too Short");
                    alertDialog.show();
                    progressDialog.dismiss();
                }
                else if(!(getPassword.equals(getConPassword)))
                {
                    alertDialog.setMessage
                            ("Password did not match");
                    alertDialog.show();
                    progressDialog.dismiss();
                }
                else
                    {
                    registerUser(getUserName,getName,getEmail,getPassword
                    ,getConPassword);
                }


            }
        });
    }
    private void registerUser(final String userName, final String name,
                              final String email, final String
                                      password, final String
                              conPassword)
    {

        auth.createUserWithEmailAndPassword(email,password).
            addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    HashMap<String ,Object> hashMap=new HashMap<>();
                    hashMap.put("userName",userName);
                    hashMap.put("name",name);
                    hashMap.put("email",email);
                    hashMap.put("password",password);
                    hashMap.put("confirmPassword",conPassword);
                    hashMap.put("id",auth.getCurrentUser().getUid());
                    hashMap.put("bio","");
                    hashMap.put("imageUrl","Default");
                    databaseReference.child("User Details").child(auth.
                            getCurrentUser().getUid()).setValue(hashMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                   progressDialog.dismiss();
                                    if(task.isSuccessful()) {

                                        alertDialog.setMessage("Registration is Done");

                                        alertDialog.show();
                                        startActivity
                                                (new Intent(Registration.this,
           Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent
                .FLAG_ACTIVITY_CLEAR_TOP                                ));
                                        finish();
                                    }
                                    else
                                    {
                                        alertDialog.setMessage("Try again");
                                        alertDialog.show();
                                    }
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
alertDialog.setMessage(e.getMessage());
alertDialog.show();
            }
        });

    }

}
