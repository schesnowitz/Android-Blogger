package com.chesnowitz.blogger.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.chesnowitz.blogger.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostListActivity extends AppCompatActivity {

  private DatabaseReference databaseReference;
  private FirebaseDatabase firebaseDatabase;
  private FirebaseUser user;
  private FirebaseAuth auth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_post_list);

    auth = FirebaseAuth.getInstance();
    user = auth.getCurrentUser();
    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReference = firebaseDatabase.getReference().child("Blogger");
    databaseReference.keepSynced(true);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
  switch (item.getItemId()) {
    case R.id.action_add:
      if (user != null && auth != null) {
        startActivity(new Intent(PostListActivity.this, AddPostActivity.class));
        finish(); // removes old activities if user goes back
      }
      break;
    case R.id.action_signout:
      if (user != null && auth != null) {
        auth.signOut();
        startActivity(new Intent(PostListActivity.this, MainActivity.class));
        finish(); // removes old activities if user goes back
      }
  }
    return super.onOptionsItemSelected(item);
  }
}
