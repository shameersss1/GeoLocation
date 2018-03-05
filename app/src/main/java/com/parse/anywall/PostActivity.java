package com.parse.anywall;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Activity which displays a login screen to the user, offering registration as well.
 */
public class PostActivity extends Activity {

  private Button postButton;

  EditText et1,et2,et3,et4;

  private static final float METERS_PER_FEET = 0.3048f;

  // Conversion from kilometers to meters
  private static final int METERS_PER_KILOMETER = 1000;
  private float radius;

  private ParseGeoPoint geoPoint;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    radius = Application.getSearchDistance();

    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_post);

    Intent intent = getIntent();
    Location location = intent.getParcelableExtra(Application.INTENT_EXTRA_LOCATION);
    geoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());


    et1 = (EditText) findViewById(R.id.name);
    et2 =(EditText) findViewById(R.id.phone);
    et3 = (EditText) findViewById(R.id.address);
    et4 = (EditText) findViewById(R.id.des);




    postButton = (Button) findViewById(R.id.post_button);
    postButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        post();
      }
    });


  }

  private void post () {
    String name = et1.getText().toString();
    String phone = et2.getText().toString();
    String add = et3.getText().toString();
    String des = et4.getText().toString();
    // Set up a progress dialog
    final ProgressDialog dialog = new ProgressDialog(PostActivity.this);
    dialog.setMessage(getString(R.string.progress_post));
    dialog.show();
    ParseUser currentUser = ParseUser.getCurrentUser();
    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
    installation.put("location",geoPoint);
      installation.put("user",currentUser.getUsername().toString());
    installation.saveInBackground();


    AnywallPost post = new AnywallPost();

    // Set the location to the current user's location
    post.setLocation(geoPoint);
    post.setAdd(add);
    post.setText(name);
    post.setPhone(phone);
    post.setDes(des);
    post.setUser(ParseUser.getCurrentUser());
    ParseACL acl = new ParseACL();

    // Give public read access
    acl.setPublicReadAccess(true);
    post.setACL(acl);



//    ParseQuery pushQuery = ParseInstallation.getQuery();
//    pushQuery.whereWithinKilometers("location", geoPoint, radius
//            * METERS_PER_FEET / METERS_PER_KILOMETER);
//
//
//    pushQuery.whereNotEqualTo("user",currentUser.getUsername().toString());
//
//
//    ParsePush push = new ParsePush();
//    push.setQuery(pushQuery);
//    push.setMessage(des);
//    push.sendInBackground();
    // Save the post
    post.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        dialog.dismiss();
        Intent i =new Intent(PostActivity.this,WelcomeActivity.class);
        Toast.makeText(getApplicationContext(),"Registration Success", Toast.LENGTH_LONG).show();
        finish();
        startActivity(i);

      }
    });
  }


}
