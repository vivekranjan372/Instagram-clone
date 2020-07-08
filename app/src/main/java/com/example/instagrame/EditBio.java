package com.example.instagrame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.instagrame.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditBio extends AppCompatActivity {
private Button save;
private CircleImageView profileImage;
private EditText editBio;
private FirebaseUser currentUser;
private AlertDialog alertDialog;
private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bio);
currentUser= FirebaseAuth.getInstance().getCurrentUser();
alertDialog =new AlertDialog.Builder(this).create();
alertDialog.setTitle("Bio Info");
progressDialog=new ProgressDialog(this);
        save=findViewById(R.id.save);


        profileImage=findViewById(R.id.profile_image);
        editBio=findViewById(R.id.edit_bio);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Bio");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getUserInfo();
    }

    private void getUserInfo() {
        FirebaseDatabase.getInstance().getReference().child("User Details")
        .child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               final User user= dataSnapshot.getValue(User.class);
                Picasso.get().load(user.getImageUrl()).into(profileImage);
                editBio.setText(user.getBio());
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.setMessage("Updating Bio");
                        progressDialog.show();
                        if((TextUtils.isEmpty(editBio.getText())))
                        {
                            progressDialog.dismiss();
                            alertDialog.setMessage("Please enter " +
                                    "something about yourSelf");
                            alertDialog.show();
                        }
                        else
                        {
                            String updateBio=editBio.getText().toString();
                         FirebaseDatabase.getInstance().getReference()
                         .child("User Details").child(currentUser.getUid())
                         .child("bio").setValue(updateBio);
                         progressDialog.dismiss();
                            alertDialog.setMessage("Your Bio is Successfully Updated");
                            alertDialog.show();
                            editBio.setText("");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })  ;
    }
}
