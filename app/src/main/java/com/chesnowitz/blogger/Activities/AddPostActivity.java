package com.chesnowitz.blogger.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.chesnowitz.blogger.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPostActivity extends AppCompatActivity {
  private ImageButton postImage;
  private EditText postTitle;
  private EditText postDescription;
  private Button buttonSubmit;

  private DatabaseReference databaseReference;
  private FirebaseDatabase firebaseDatabase;
  private FirebaseUser user;
  private FirebaseAuth auth;
  private ProgressDialog progress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_post);

    progress = new ProgressDialog(this);

    auth = FirebaseAuth.getInstance();
    user = auth.getCurrentUser();
    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReference = firebaseDatabase.getInstance().getReference().child("Blogger");
    databaseReference.keepSynced(true);

    postImage = (ImageButton) findViewById(R.id.imageButton);
    postTitle = (EditText) findViewById(R.id.etAddPostTitle);
    postDescription = (EditText) findViewById(R.id.etAddPostDescription);
    buttonSubmit = (Button) findViewById(R.id.bSubmitPost);

    buttonSubmit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // send data to DB...

        startPosting();

      }
    });
  }

  private void startPosting() {
    progress.setMessage("Working on things...");
    progress.show();

    String titleValue = postTitle.getText().toString().trim();
    String descriptionValue = postDescription.getText().toString().trim();

    if (!TextUtils.isEmpty(titleValue) && !TextUtils.isEmpty(descriptionValue)) {
    // start uploading content...
    }
  }
}
