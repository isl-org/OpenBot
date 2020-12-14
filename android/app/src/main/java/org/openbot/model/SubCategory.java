package org.openbot.model;

public class SubCategory {

  public SubCategory(String title, int image) {
    this.title = title;
    this.image = image;
  }

  private String title;
  private int image;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getImage() {
    return image;
  }

  public void setImage(int image) {
    this.image = image;
  }
}
