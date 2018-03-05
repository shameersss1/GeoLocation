package com.parse.anywall;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.logging.Handler;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.anywall.utils.Utils;

public class RegisterActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    Handler mHandler;
    private Timer autoUpdate;

    /*
     * Define a request code to send to Google Play services This code is returned in
     * Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    /*
     * Constants for location update parameters
     */
    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordAgainEditText;

    // The update interval
    private static final int UPDATE_INTERVAL_IN_SECONDS = 5;

    // A fast interval ceiling
    private static final int FAST_CEILING_IN_SECONDS = 1;

    // Update interval in milliseconds
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
            * UPDATE_INTERVAL_IN_SECONDS;

    // A fast ceiling of update intervals, used when the app is visible
    private static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
            * FAST_CEILING_IN_SECONDS;

    /*
     * Constants for handling location results
     */
    // Conversion from feet to meters
    private static final float METERS_PER_FEET = 0.3048f;

    // Conversion from kilometers to meters
    private static final int METERS_PER_KILOMETER = 1000;

    // Initial offset for calculating the map bounds
    private static final double OFFSET_CALCULATION_INIT_DIFF = 1.0;

    // Accuracy for calculating the map bounds
    private static final float OFFSET_CALCULATION_ACCURACY = 0.01f;

    // Maximum results returned from a Parse query
    private static final int MAX_POST_SEARCH_RESULTS = 20;

    // Maximum post search radius for map in kilometers
    private static final int MAX_POST_SEARCH_DISTANCE = 100;

    /*
     * Other class member variables
     */
    // Map fragment
    private SupportMapFragment mapFragment;

    // Represents the circle around a map
    private Circle mapCircle;

    // Fields for the map radius in feet
    private float radius;
    private float lastRadius;

    // Fields for helping process map and location changes
    private final Map<String, Marker> mapMarkers = new HashMap<String, Marker>();
    private int mostRecentMapUpdate;
    private boolean hasSetUpInitialLocation;
    private String selectedPostObjectId;
    private String selectedPostObjectName;
    private Location lastLocation;
    private Location currentLocation;

    // A request to connect to Location Services
    private LocationRequest locationRequest;

    // Stores the current instantiation of the location client in this object
    private GoogleApiClient locationClient;
    private ParseGeoPoint geoPoint;

    /** The username EditText. */
    private EditText user;

    Button b1;

    /** The password EditText. */
    private EditText pwd;

    /** The email EditText. */
    private EditText name,phone,add;

    String a;

    // Adapter for the Parse query
    private ParseQueryAdapter<AnywallPost> postsQueryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);




        // Create a new location client, using the enclosing class to handle callbacks.
        locationClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();



        user = (EditText) findViewById(R.id.user);
        pwd = (EditText) findViewById(R.id.pwd);


        b1 = (Button) findViewById(R.id.btnReg);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });


    }
    private void signup() {
        lastLocation = LocationServices.FusedLocationApi
                .getLastLocation(locationClient);

        geoPoint = new ParseGeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude());


        String u = user.getText().toString();
        String p = pwd.getText().toString();
        if (u.length() == 0 || p.length() == 0)
        {
            Utils.showDialog(this, R.string.err_fields_empty);
            return;
        }
        final ProgressDialog dia = ProgressDialog.show(this, null,
                getString(R.string.alert_wait));

        final ParseUser pu = new ParseUser();
        pu.setPassword(p);
        pu.put("location",geoPoint);
        pu.setUsername(u);
        pu.signUpInBackground(new SignUpCallback() {

            @Override
            public void done(ParseException e) {
                dia.dismiss();

                if (e == null) {
                    ParseUser user = pu;

Intent i =new Intent(RegisterActivity.this,PostActivity.class);
                    i.putExtra(Application.INTENT_EXTRA_LOCATION, lastLocation);
                    finish();
                    startActivity(i);
                } else {
                    Utils.showDialog(
                            RegisterActivity.this,
                            getString(R.string.err_singup) + " "
                                    + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (locationClient != null) {
            locationClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        //displayLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        locationClient.connect();
    }




}