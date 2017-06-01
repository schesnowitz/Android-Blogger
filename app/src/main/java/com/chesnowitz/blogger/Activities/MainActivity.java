package com.chesnowitz.blogger.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chesnowitz.blogger.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

  private FirebaseAuth mAuth;
  private FirebaseAuth.AuthStateListener mAuthListener;
  private FirebaseUser user;
  private Button loginButton;
  private Button registerButton;
  private EditText emailField;
  private EditText passwordField;



  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mAuth = FirebaseAuth.getInstance();
    loginButton = (Button) findViewById(R.id.bLogin);
    registerButton = (Button) findViewById(R.id.bRegister);
    emailField = (EditText) findViewById(R.id.emailField);
    passwordField = (EditText) findViewById(R.id.passwordField);

    mAuthListener = new FirebaseAuth.AuthStateListener() {
      @Override
      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
      user = firebaseAuth.getCurrentUser();

        if (user != null) {
          Toast.makeText(MainActivity.this, "You are signed in.", Toast.LENGTH_LONG).show();
          startActivity(new Intent(MainActivity.this, PostListActivity.class));
          finish(); // removes old activities if user goes back
        } else {
          Toast.makeText(MainActivity.this, "You are signed out.", Toast.LENGTH_LONG).show();
        }
      }
    };
    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!TextUtils.isEmpty(emailField.getText().toString())
        && !TextUtils.isEmpty(passwordField.getText().toString())) {

          String email = emailField.getText().toString();
          String password = passwordField.getText().toString();
          
          login(email, password);

        } else {

        }
      }
    });
  }

  private void login(String email, String password) {
    mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful()) {
                 Toast.makeText(MainActivity.this, "Signed In!", Toast.LENGTH_SHORT).show();

                 startActivity(new Intent(MainActivity.this, PostListActivity.class));
                 finish(); // removes old activities if user goes back

               } else {
                 // Not signed in...
               }
              }
            });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_signout) {
      mAuth.signOut();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  protected void onStart() {
    super.onStart();
    mAuth.addAuthStateListener(mAuthListener);
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (mAuthListener != null) {
      mAuth.removeAuthStateListener(mAuthListener);
    }
  }
}
