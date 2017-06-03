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

import com.chesnowitz.blogger.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CreateAccountActivity extends AppCompatActivity {

  private EditText firstName;
  private EditText lastName;
  private EditText email;
  private EditText password;
  private ImageButton profileImage;
  private Uri resultUri = null;
  private static final int GALLERY_CODE = 123;
  private Button bCreateAccount;

  private DatabaseReference databaseReference;
  private FirebaseDatabase firebaseDatabase;
  private FirebaseAuth firebaseAuth;
  private ProgressDialog progressDialog;
  private StorageReference firebaseStorage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_account);

    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReference = firebaseDatabase.getReference().child("User");
    firebaseAuth = FirebaseAuth.getInstance();
    firebaseStorage = FirebaseStorage.getInstance().getReference().child("Profile_Image");

    progressDialog = new ProgressDialog(this);

    firstName = (EditText) findViewById(R.id.caFirstName);
    lastName = (EditText) findViewById(R.id.caLastName);
    email = (EditText) findViewById(R.id.caEmail);
    password = (EditText) findViewById(R.id.caPassword);
    profileImage = (ImageButton) findViewById(R.id.profileImage);
    bCreateAccount = (Button) findViewById(R.id.caButton);


    profileImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_CODE);
      }
    });

    bCreateAccount.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        createAccount();
      }
    });

  }

  private void createAccount() {
    final String userFirstName = firstName.getText().toString().trim();
    final String userLastName = lastName.getText().toString().trim();
    final String userEmail = email.getText().toString().trim();
    final String userPassword = password.getText().toString().trim();

    if (!TextUtils.isEmpty(userFirstName) && !TextUtils.isEmpty(userLastName)
            && !TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPassword)) {
      progressDialog.setMessage("Creating Account...");
      progressDialog.show();

      firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
              .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                  if (authResult != null) {
                      StorageReference imagePath = firebaseStorage.child("Profile_Image")
                              .child(resultUri.getLastPathSegment());

                    imagePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                      @Override
                      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String user_id = firebaseAuth.getCurrentUser().getUid();
                        DatabaseReference currentUserReference = databaseReference.child(user_id);
                        currentUserReference.child("first_name").setValue(userFirstName);
                        currentUserReference.child("last_name").setValue(userLastName);
                        currentUserReference.child("email").setValue(userEmail);
                        currentUserReference.child("paswword").setValue(userPassword);
                        currentUserReference.child("image").setValue(resultUri.toString());

                        progressDialog.dismiss();

                        // redirect the user

                        Intent intent = new Intent(CreateAccountActivity.this, PostListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                      }
                    });

//                    String user_id = firebaseAuth.getCurrentUser().getUid();
//                    DatabaseReference currentUserReference = databaseReference.child(user_id);
//                    currentUserReference.child("first_name").setValue(userFirstName);
//                    currentUserReference.child("last_name").setValue(userLastName);
//                    currentUserReference.child("email").setValue(userEmail);
//                    currentUserReference.child("paswword").setValue(userPassword);
//                    currentUserReference.child("image").setValue("nada");
//
//                    progressDialog.dismiss();
//
//                    // redirect the user
//
//                    Intent intent = new Intent(CreateAccountActivity.this, PostListActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
                  }
                }
              });
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {

      Uri imageUri = data.getData();

      CropImage.activity(imageUri)
              .setAspectRatio(1, 1)
              .setGuidelines(CropImageView.Guidelines.ON)
              .start(this);
    }
    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
      CropImage.ActivityResult result = CropImage.getActivityResult(data);
      if (resultCode == RESULT_OK) {
        resultUri = result.getUri();

        profileImage.setImageURI(resultUri);

      } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
        Exception error = result.getError();
      }
    }
  }
}
