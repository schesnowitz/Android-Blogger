package com.chesnowitz.blogger.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.chesnowitz.blogger.Model.Blog;
import com.chesnowitz.blogger.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {
  private ImageButton postImage;
  private EditText postTitle;
  private EditText postDescription;
  private Button buttonSubmit;
  private StorageReference storage;
  private DatabaseReference databaseReference;
  private FirebaseDatabase firebaseDatabase;
  private FirebaseUser user;
  private FirebaseAuth auth;
  private ProgressDialog progress;
  private Uri imageUri;
  private static final int GALLERY_CODE = 1;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_post);

    progress = new ProgressDialog(this);

    auth = FirebaseAuth.getInstance();
    user = auth.getCurrentUser();
    storage = FirebaseStorage.getInstance().getReference(); // gets ref (storage url) to storage inside FB

    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReference = firebaseDatabase.getInstance().getReference().child("Blogger");
    databaseReference.keepSynced(true);

    postImage = (ImageButton) findViewById(R.id.imageButton);
    postTitle = (EditText) findViewById(R.id.etAddPostTitle);
    postDescription = (EditText) findViewById(R.id.etAddPostDescription);
    buttonSubmit = (Button) findViewById(R.id.bSubmitPost);


    // click listener to get image

    postImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_CODE); // galery code set as 1 above
      }
    });


    buttonSubmit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // send data to DB...

        startPosting();

      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
      imageUri = data.getData();
      postImage.setImageURI(imageUri);
    }

  }

  private void startPosting() {
    progress.setMessage("Working on things...");
    progress.show();

    final String titleValue = postTitle.getText().toString().trim();
    final String descriptionValue = postDescription.getText().toString().trim();

    if (!TextUtils.isEmpty(titleValue) && !TextUtils.isEmpty(descriptionValue)
            && imageUri != null) {
    // start uploading content...


//      storage reference file path

      StorageReference filepath =
              storage.child("blog_images").child(imageUri.getLastPathSegment());

      filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

          Uri downloadUrl = taskSnapshot.getDownloadUrl();

          DatabaseReference newPost = databaseReference.push();

          Map<String, String> dataToSave = new HashMap<>();
          dataToSave.put("title", titleValue);
          dataToSave.put("description", descriptionValue);
          dataToSave.put("image", downloadUrl.toString());
          dataToSave.put("timestamp", String.valueOf(java.lang.System.currentTimeMillis()));
          dataToSave.put("user_id", user.getUid());

          newPost.setValue(dataToSave);

          progress.dismiss();

          startActivity(new Intent(AddPostActivity.this, PostListActivity.class));
          finish();
          /*
          could also be done...
          newPost.child("title).setValue(titleValue);
           */


        }
      });









/*
-------->  code to write some sample data to DB

      Blog blog = new Blog("Title", "Description", "imageurl", "timestamp", "user_id");
      databaseReference.setValue(blog).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
          Toast.makeText(getApplicationContext(), "Item Added", Toast.LENGTH_LONG).show();

          progress.dismiss();
        }
      });
*/



    }
  }
}
