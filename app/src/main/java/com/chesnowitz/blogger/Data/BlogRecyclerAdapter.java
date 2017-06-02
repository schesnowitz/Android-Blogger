package com.chesnowitz.blogger.Data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chesnowitz.blogger.Model.Blog;
import com.chesnowitz.blogger.R;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

/**
 * Created by steve on 6/1/2017.
 */

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

  private Context context;
  private List<Blog> blogList;

  public BlogRecyclerAdapter(Context context, List<Blog> blogList) {
    this.context = context;
    this.blogList = blogList;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.post_row, parent, false);

    return new ViewHolder(view, context);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    // bind views and data
    Blog blog = blogList.get(position);
    String imageUrl = null;

    // set up 'view widgets'

    holder.title.setText(blog.getTitle());
    holder.description.setText(blog.getDescription());


    java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
    String formattedDate = dateFormat.format
            (new Date(Long.valueOf(blog.getTimestamp())).getTime());

    holder.timestamp.setText(formattedDate);

    imageUrl = blog.getImage();
    // TODO ---->  use picasso to load image
  }

  @Override
  public int getItemCount() {
    return blogList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView description;
    public TextView timestamp;
    public ImageView image;
    String user_id;

    public ViewHolder(View view, Context ctx) {
      super(view);
      context = ctx;

      title = (TextView) view.findViewById(R.id.postTitleList);
      description = (TextView) view.findViewById(R.id.postTextList);
      timestamp =(TextView) view.findViewById(R.id.timestampList);
      image =(ImageView) view.findViewById(R.id.postImageList);

      user_id = null;

      view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          // go to next Activity...
        }
      });
    }
  }
}
