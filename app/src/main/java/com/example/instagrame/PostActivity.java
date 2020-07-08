package com.example.instagrame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hendraanggrian.appcompat.socialview.Hashtag;
import com.hendraanggrian.appcompat.widget.HashtagArrayAdapter;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.List;

public class PostActivity extends AppCompatActivity {
private ImageView closeView;
private ImageView imageAdded;
private TextView post;
private Uri imageUri;
private AlertDialog alertDialog;
 SocialAutoCompleteTextView description;
 private String imageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        closeView=findViewById(R.id.close_image);
        imageAdded=findViewById(R.id.image_added);
        post=findViewById(R.id.post);
        description=findViewById(R.id.description);
        alertDialog=new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Post status");

        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(

                         PostActivity.this, MainFrame.class));
                 finish();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upload();
            }
        });
        CropImage.activity().start(PostActivity.this);


    }

    private void upload() {
        final ProgressDialog progressDialog=new ProgressDialog(PostActivity.this);
        progressDialog.setMessage("Uploading Image");
        progressDialog.show();
        if(imageUri!=null)
        {
            final StorageReference filePath= FirebaseStorage.getInstance().
                    getReference().child("Image Post").
                    child(System.currentTimeMillis() +
                    "."+getFileExtension(imageUri));
            StorageTask uploadTask=filePath.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
Uri downloadUri=task.getResult();
imageUrl=downloadUri.toString();
                    DatabaseReference reference= FirebaseDatabase
                            .getInstance().getReference("Image Post");
                    String postId=reference.push().getKey();
                    HashMap<String,Object>map=new HashMap<>();
                    map.put("postId",postId);
                    map.put("imageUrl",imageUrl);
                    map.put("description",description.getText().toString());
                    map.put("publisher", FirebaseAuth.getInstance()
                    .getCurrentUser().getUid());
                    reference.child(postId).setValue(map);
                    DatabaseReference hashRef =FirebaseDatabase.getInstance()
                            .getReference().child("HashTags");
                List<String> hashTag=description.getHashtags();
                if(!hashTag.isEmpty())
                {
                    for(String tag:hashTag)
                    {
                        map.clear();
                        map.put("tag",tag.toLowerCase());
                        map.put("postId",postId) ;
                        hashRef.child(tag.toLowerCase()).child(postId).setValue(map);

                    }
                }
                progressDialog.dismiss();

                    Toast.makeText(PostActivity.this,"Your Response Successfully" +
                            " Submitted",Toast.LENGTH_LONG).show();
                startActivity(new Intent(PostActivity.this,
                        MainFrame.class));
                finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    alertDialog.setMessage(e.getMessage());
                    alertDialog.show();
                }
            });
        }
        else
        {
            alertDialog.setMessage("Please Select a Image");
            alertDialog.show();
        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode==RESULT_OK)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            assert result != null;
            imageUri=result.getUri();
            imageAdded.setImageURI(imageUri);

        }
        else
        {

            startActivity(new Intent(PostActivity.
                    this, MainFrame.class));
            finish();
        }


    }
    public class ThreadOperation implements Runnable
    {


        @Override
        public void run()
        {
            Looper.prepare();
            synchronized (this)
            {
                try {
                    wait(3000);
                    alertDialog.setMessage("try again");
                    alertDialog.show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                catch (WindowManager.BadTokenException e) {

                    Log.e("WindowManagerBad ", e.toString());
                }
            }


        }
    }
    public class ImageSuccess implements Runnable
    {


        @Override
        public void run()
        {
            Looper.prepare();
            synchronized (this)
            {
                try {
                    wait(3000);
                    alertDialog.setMessage("Image is Uploaded Successfully");
                    alertDialog.show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                catch (WindowManager.BadTokenException e) {

                    Log.e("WindowManagerBad ", e.toString());
                }
            }


        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        final ArrayAdapter<Hashtag> hashTagAdapter=new HashtagArrayAdapter<>(getApplicationContext());
        FirebaseDatabase.getInstance().getReference().child("HashTags").
                addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
for(DataSnapshot snapshot:dataSnapshot.getChildren())
{
    hashTagAdapter.add(new Hashtag(snapshot.getKey(),(int)snapshot.getChildrenCount
            ()));

}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
        description.setHashtagAdapter(hashTagAdapter);
    }

}
