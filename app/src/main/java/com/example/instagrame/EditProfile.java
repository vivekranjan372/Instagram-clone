package com.example.instagrame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.instagrame.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
private Button editPicture;
private CircleImageView profilePicture;
private Button editBio;
private TextView editTextBio;
private Toolbar toolbar;
private Uri imageUri;
private ProgressDialog progressDialog;
private FirebaseUser currentUser;
private String imageUrl;
private AlertDialog alertDialog;
private String popUpImageShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        editPicture=findViewById(R.id.edit_picture);
        profilePicture=findViewById(R.id.profile_image);
        editBio=findViewById(R.id.edit_bio);
        editTextBio=findViewById(R.id.edit_bio_text);

        alertDialog=new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Image Upload");


        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");

editBio.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(EditProfile.this,EditBio.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
});


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
progressDialog=new ProgressDialog(this);
currentUser=FirebaseAuth.getInstance().getCurrentUser();

editPicture.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        openImage();
    }
});

getDetails();

    }

    public  void popUpImage(View view) {
        Dialog dialog=new Dialog(this,android.R.style.Theme_DeviceDefault);
        dialog.getWindow().requestFeature(Window
                .FEATURE_ACTION_BAR);


        ImageView imageView=new ImageView(this);
        if(popUpImageShow.equals("Default"))
        {
            Picasso.get().load(R.mipmap.ic_launcher).into(imageView);
        }
        else {
            Picasso.get().load(popUpImageShow).
                    placeholder(R.mipmap.ic_launcher).into(imageView);
        }

        dialog.addContentView(imageView,new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.
                        MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT
        ));
        dialog.show();
    }

    private void getDetails() {
        FirebaseDatabase.getInstance().getReference().child("User Details").child(currentUser.getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user=dataSnapshot.getValue(User.class);
                        popUpImageShow=user.getImageUrl();
                        if(popUpImageShow.equals("Default"))
                        {
                            Picasso.get().load(R.mipmap.ic_launcher).into(profilePicture);
                        }
                        else {
                            Picasso.get().load(popUpImageShow).
                                    placeholder(R.mipmap.ic_launcher).into(profilePicture);
                        }

                        editTextBio.setText(user.getBio());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

    private void openImage() {
        CropImage.activity().start(EditProfile.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK)
        {
CropImage.ActivityResult result=CropImage.getActivityResult(data);
imageUri=result.getUri();
profilePicture.setImageURI(imageUri);
editProfileImage();

        }

    }


    private  void editProfileImage()
    {
       if(imageUri!=null)
       {
           progressDialog.setMessage("Uploading");
           progressDialog.show();
           final StorageReference storage=FirebaseStorage.getInstance().getReference().child("Image Post").child(System.currentTimeMillis()+"."+getExtension(imageUri));
           StorageTask uploadingTask=storage.putFile(imageUri);
           uploadingTask.continueWithTask(new Continuation() {
               @Override
               public Object then(@NonNull Task task) throws Exception {
                   if(task.isSuccessful())
                   {
                       return storage.getDownloadUrl();
                   }
                   else
                   {
                       throw task.getException();
                   }
               }
           }).addOnCompleteListener(new OnCompleteListener<Uri>() {
               @Override
               public void onComplete(@NonNull Task<Uri> task) {

                   Uri downloadUri=task.getResult();
                   imageUrl=downloadUri.toString();
           FirebaseDatabase.getInstance().getReference().child("User Details").child(currentUser.getUid())
                           .child("imageUrl").setValue(imageUrl)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                              if(task.isSuccessful())
                              {
                            FirebaseDatabase.getInstance()
                            .getReference().child("User Details").child(currentUser
                            .getUid()).addValueEventListener(new ValueEventListener() {
                                  @Override
                                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                      User user=dataSnapshot.getValue(User.class);

                                      Picasso.get().load(user.
                                              getImageUrl()).into(profilePicture);
                                      progressDialog.dismiss();
                                      alertDialog.setMessage("Your Profile Image is Successfully" +
                                              "Updated");
                                      alertDialog.show();
                                  }

                                  @Override
                                  public void onCancelled(@NonNull DatabaseError databaseError) {

                                  }
                              });
                              }
                            }
                        });
               }
           });
       }
    }


    private String getExtension(Uri uri) {

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri));

    }


}
