package com.chesnowitz.blogger.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.chesnowitz.blogger.Data.BlogRecyclerAdapter;
import com.chesnowitz.blogger.Model.Blog;
import com.chesnowitz.blogger.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PostListActivity extends AppCompatActivity {

  private DatabaseReference databaseReference;
  private FirebaseDatabase firebaseDatabase;
  private FirebaseUser user;
  private FirebaseAuth auth;
  private RecyclerView recyclerView;
  private BlogRecyclerAdapter blogRecyclerAdapter;
  private List<Blog> blogList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_post_list);

    auth = FirebaseAuth.getInstance();
    user = auth.getCurrentUser();
    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReference = firebaseDatabase.getReference().child("Blogger");
    databaseReference.keepSynced(true);

    blogList = new ArrayList<>();
    recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
  @Override
  protected void onStart() {
    super.onStart();

    databaseReference.addChildEventListener(new ChildEventListener() {
      @Override
      public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        Blog blog = dataSnapshot.getValue(Blog.class);

        blogList.add(blog);

        blogRecyclerAdapter = new BlogRecyclerAdapter(PostListActivity.this, blogList);
        recyclerView.setAdapter(blogRecyclerAdapter);
        blogRecyclerAdapter.notifyDataSetChanged();
      }

      @Override
      public void onChildChanged(DataSnapshot dataSnapshot, String s) {

      }

      @Override
      public void onChildRemoved(DataSnapshot dataSnapshot) {

      }

      @Override
      public void onChildMoved(DataSnapshot dataSnapshot, String s) {

      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }
}

