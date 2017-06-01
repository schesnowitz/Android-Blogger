package com.chesnowitz.blogger.Model;

/**
 * Created by steve on 6/1/2017.
 */

public class Blog {
  public String title;
  public String decription;
  public String image;
  public String timestamp;
  public String user_id;

  public Blog() {
  }

  public Blog(String title, String decription, String image, String timestamp, String user_id) {
    this.title = title;
    this.decription = decription;
    this.image = image;
    this.timestamp = timestamp;
    this.user_id = user_id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDecription() {
    return decription;
  }

  public void setDecription(String decription) {
    this.decription = decription;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getUser_id() {
    return user_id;
  }

  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }
}
