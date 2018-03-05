package com.parse.anywall;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Data model for a post.
 */
@ParseClassName("Posts")
public class AnywallPost extends ParseObject {
  public String getText() {
    return getString("text");
  }
  public String getPhone() {
    return getString("phone");
  }
  public String getadd() {
    return getString("address");
  }
  public String getdes() {
    return getString("description");
  }

  public void setText(String value) {
    put("text", value);
  }

  public void setName(String value) {
    put("name", value);
  }

  public void setPhone(String value) {
    put("phone", value);
  }

  public void setAdd(String value) {
    put("address", value);
  }

  public void setDes(String value) {
    put("description", value);
  }



  public ParseUser getUser() {
    return getParseUser("user");
  }

  public void setUser(ParseUser value) {
    put("user", value);
  }




  public ParseGeoPoint getLocation() {
    return getParseGeoPoint("location");
  }

  public void setLocation(ParseGeoPoint value) {
    put("location", value);
  }

  public static ParseQuery<AnywallPost> getQuery() {
    return ParseQuery.getQuery(AnywallPost.class);
  }
}
