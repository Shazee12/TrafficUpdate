package shahzaib.com.trafficupdate.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.PolyUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mancj.slideup.SlideUp;

import org.xml.sax.InputSource;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import cz.msebera.android.httpclient.Header;
import shahzaib.com.trafficupdate.Model.Postitem;
import shahzaib.com.trafficupdate.Model.RessultModel;
import shahzaib.com.trafficupdate.Model.latlng;
import shahzaib.com.trafficupdate.R;

/**
 * Created by shahzaib on 7/25/2017.
 */

public class MapViewActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMapReadyCallback {
    private Double mLatitude, mLongitude;

    GoogleMap mgoogleMap;
    //MapView mapView;
    GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    ArrayList<Marker> markers = new ArrayList<>();
    private boolean isCameraSet;
    public ArrayList<latlng> v = new ArrayList<>();
    Set<String> ps = new HashSet<>();
    Polyline polylin;
    private String a, ba;
    private Bundle bundle;
     LatLng[] latLngs = new LatLng[2];
    public Button show_status;

    ArrayList<String> ss = new ArrayList<>();
    private AutoCompleteTextView mAutocompleteTextView, getmAutocompleteTextView;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private final int postActivityCode = 200;
    private FloatingActionButton fab;
    private static CharSequence from, to;
    private Location mLocation;
    private boolean sCameraMoved;
    DrawerLayout drawer;
    NavigationView navigationView;
    FloatingSearchView searchView;
    ImageView s2;
    private int navItemIndex;
    private MapView mapView;
    private FirebaseAuth auth;
    private LocationRequest request;
    private Location mLastLocation;
    private String TAG;
    private boolean mRequestingLocationUpdates;
    private final static int REQUEST_CHECK_SETTINGS = 1000;
    private AutoCompleteTextView getLocationTextView, locationTextView;
    SlideUp slideUp;



    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_map);


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        getLocationTextView = (AutoCompleteTextView) findViewById(R.id.location_text1);
        locationTextView = (AutoCompleteTextView) findViewById(R.id.location_text);
        show_status = (Button) findViewById(R.id.show_status);

        CheckMapPermission();
buildGoogleAPiClient();




/*

        getSupportFragmentManager().beginTransaction().replace(R.id.posFragmentConainer,new PostStatusfragment()).commit();

        final SlideUp slideUp = new SlideUpBuilder(slidingView)
                .withStartState(SlideUp.State.HIDDEN)
                .withStartGravity(Gravity.BOTTOM)

                .withGesturesEnabled(true)
                .withHideSoftInputWhenDisplayed(true)
                //.withInterpolator()
                //.withAutoSlideDuration()
                //.withLoggingEnabled()
                //.withTouchableAreaPx()
                //.withTouchableAreaDp()
                //.withListeners()
                //.withSavedState()
                .build();

        findViewById(R.id.postNewBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideUp.show();
            }
        });
*/

    }


   /* private void zoom() {
        displayLocationSettingsRequest(MapViewActivity.this);
        if (ActivityCompat.checkSelfPermission(MapViewActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapViewActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mgoogleMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);


        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();


            LatLng coordinate = new LatLng(latitude, longitude);
            zoomToLocation(coordinate);
        }

    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MapViewActivity.this, postActivityCode);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    private void ShowDirection() {

        if (from != null && to != null) {
            decodeAddress((String) from, new SimpleAddressDecoderCallback() {
                @Override
                public void onDecode(double lat, double lng) {
                    latLngs[0] = new LatLng(lat, lng);
                    decodeAddress((String) to, new SimpleAddressDecoderCallback() {
                        @Override
                        public void onDecode(final double lat, final double lng) {
                            latLngs[1] = new LatLng(lat, lng);
                            showDirection(latLngs[0], latLngs[1]);
                            addMarkers();


                        }

                    });
                }
            });

        } else {
            Toast.makeText(this, "Give your locations", Toast.LENGTH_SHORT).show();
        }
    }
*/

    protected synchronized void buildGoogleAPiClient() {
        if (mGoogleApiClient == null) {

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
            //mGoogleApiClient = new GoogleApiClient.Builder(this);
        }
        mGoogleApiClient.connect();

    }


  /*  public boolean googleserviceAvailablity() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvalable = api.isGooglePlayServicesAvailable(this);
        if (isAvalable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvalable)) {
            Dialog dialog = api.getErrorDialog(this, isAvalable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cant connect to google play", Toast.LENGTH_LONG).show();
        }
        return false;


    }
*/

    public void SearchStatus() {

        show_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v = GetPos();
             ps = GetMarkerNames();

                bundle = new Bundle();
               bundle.putParcelableArrayList("latlngs",  v);
                bundle.putSerializable("names", (Serializable) ps);

                //bundle.putParcelableArrayList("pos", (ArrayList<? extends Parcelable>) ps);
                Intent i = new Intent(getApplication(), ShowstatusActivity.class);

                i.putExtras(bundle);

                startActivity(i);
                //v.clear();
                v.clear();
                finish();
            }


        });
    }

    private ArrayList<latlng> GetPos(){
        ArrayList<latlng> pos = new ArrayList<>();
        HashSet<latlng> hashSet = new HashSet<latlng>();
       // ArrayList<Marker> marker = markers;
        latlng ln = new latlng();
            //Log.d("e", String.valueOf((l)));
            for (Marker marker : markers) {
                LatLng l = marker.getPosition();
                    if (PolyUtil.isLocationOnPath(l, Arrays.asList(latLngs), false, 800)) {

                        ln = new latlng(l.longitude, l.latitude);
                        Log.d("ltln", String.valueOf((ln)));
                        pos.add(ln);


                    }



            }




        Log.d("eee", String.valueOf((pos)));

        return  pos;
    }
    private Set<String> GetMarkerNames() {
        Set<String> bb = new HashSet<>();
        Set<String> idd = new HashSet<>();

        for (Marker marker : markers) {


            String names,id;
            if (PolyUtil.isLocationOnPath(marker.getPosition(), Arrays.asList(latLngs), true, 800)) {

                names = marker.getTitle();
               id =  marker.getId();

               idd.add(id);
                bb.add(names);

            }


        }
        Toast.makeText(getApplicationContext(), "m" +  bb, Toast.LENGTH_SHORT).show();

        return bb;


    }
    private ArrayList<latlng> GETPS() {

        ArrayList<latlng> l = new ArrayList<>();

        for (Marker marker : markers) {

            latlng lz = new latlng();
            LatLng latLn;
            if (PolyUtil.isLocationOnPath(marker.getPosition(), Arrays.asList(latLngs), true, 500)) {

                latLn = marker.getPosition();
                lz = new latlng(latLn);

                l.add(lz);

            }

        }
        Toast.makeText(getApplicationContext(), "latlngs" +  l, Toast.LENGTH_SHORT).show();
        //STOPS NOW
        return l;


    }

    private void addMarkers() {
        FirebaseDatabase.getInstance().getReference().child("Posts").orderByChild("time").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                RessultModel postItem = dataSnapshot.getValue(RessultModel.class);

                if (postItem != null && postItem.getName() != null) {
                  /*  if (!isCameraSet) {
                        isCameraSet = true;
                        latlng l = postItem.getLatLng();


                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(l, 15);

                        mgoogleMap.setTrafficEnabled(true);
                    }*/


                    //Location location;
                    Marker marker = addMarker(postItem.getLatLng(), postItem.getName(), BitmapDescriptorFactory.fromBitmap(resizeMapIcons("pin", 150, 175)));
                    markers.add(marker);


                }
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

   /* public void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds currentLatLongBounds =
                googleMap.getProjection().getVisibleRegion().latLngBounds;
        boolean updateBounds = false;

        for (LatLng latLng : lstLatLngRoute) {
            if (!currentLatLongBounds.contains(latLng)) {
                updateBounds = true;
            }
        }

        if (updateBounds) {

            CameraUpdate cameraUpdate;

            if (lstLatLngRoute.size() == 1) {

                LatLng latLng = lstLatLngRoute.iterator().next();
                cameraUpdate = CameraUpdateFactory.newLatLng(latLng);

            } else {

                LatLngBounds.Builder builder = LatLngBounds.builder();
                for (LatLng latLng : lstLatLngRoute) {
                    builder.include(latLng);
                }
                LatLngBounds latLongBounds = builder.build();

                cameraUpdate =
                        CameraUpdateFactory.newLatLngBounds(latLongBounds, 90);

            }

            try {
                googleMap.animateCamera(cameraUpdate, 500,
                        new GoogleMap.CancelableCallback() {
                            @Override
                            public void onFinish() {
                            }

                            @Override
                            public void onCancel() {
                            }
                        });
            } catch (IllegalStateException ex) {
                // Ignore it. We're just being a bit lazy, as this exception only happens if
                // we try to animate the camera before the map has a size
            }
        }
    }*/

    private Marker addMarker(latlng location, String title, BitmapDescriptor image) {

        return mgoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLongitite(), location.getLatitude()))
                .draggable(false)
                .title(title)
                .icon(image)


        );
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", this.getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

  /*  private void zoomToLocation(LatLng location) {

        CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(15).build();
        mgoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }*/

    private void showDirection(final LatLng me, final LatLng dest) {
        final GMapV2Direction md = new GMapV2Direction();
        md.getDocument(me, dest, GMapV2Direction.MODE_DRIVING, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                //pd.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    mgoogleMap.clear();
                    isCameraSet = false;
                    addMarkers();

                    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    InputSource is = new InputSource();
                    is.setCharacterStream(new StringReader(new String(responseBody)));

                    ArrayList<LatLng> directionPoint = md.getDirection(db.parse(is));
                    PolylineOptions rectLine = new PolylineOptions().width(12).color(
                            getResources().getColor(android.R.color.black));

                    for (int i = 0; i < directionPoint.size(); i++) {

                        rectLine.add(directionPoint.get(i));
                    }
                    if (polylin != null) {
                        polylin.remove();
                    }
                    polylin = mgoogleMap.addPolyline(rectLine);
                    polylin.setZIndex(100);
                    //zoomRoute(mgoogleMap, directionPoint);
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(me);
                    builder.include(dest);
                    LatLngBounds bounds = builder.build();

                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
                    mgoogleMap.animateCamera(cu, new GoogleMap.CancelableCallback() {
                        public void onCancel() {
                        }

                        public void onFinish() {
                            CameraUpdate zout = CameraUpdateFactory.zoomBy((float) -0.1);
                            mgoogleMap.animateCamera(zout);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public void onFinish() {
                //pd.dismiss();
            }
        });
    }

/*
    private void decodeAddress(String address, final SimpleAddressDecoderCallback callback) {
        try {
            //address += ", Pakistan";
            final ProgressDialog apd = new ProgressDialog(this);
            apd.setMessage("Getting Address Location...");
            apd.setCancelable(false);
            final String decoderUrl = "http://maps.googleapis.com/maps/api/geocode/json?address=" + URLEncoder.encode(address, "UTF-8") + "&sensor=false";
            final String finalAddress = address;
            (new AsyncHttpClient()).get(decoderUrl, new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                    apd.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONObject geo = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                        if (geo != null) {
                            Log.e("Decoded Cords", "Address : " + finalAddress + " Cords : " + geo.getDouble("lat") + "," + geo.getDouble("lng"));
                            callback.onDecode(geo.getDouble("lat"), geo.getDouble("lng"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Toast.makeText(MapViewActivity.this, "Failed To get address location!", LENGTH_SHORT).show();
                }

                @Override
                public void onFinish() {
                    apd.dismiss();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
*/

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {

        } else {
            mGoogleApiClient.disconnect();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            googleApiAvailability.getErrorDialog(this, resultCode, 1).show();
        } else {

            mGoogleApiClient.connect();
            createLocationRequest();
        }


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgoogleMap = googleMap;
        addMarkers();
    }

    public void createLocationRequest() {

        request = new LocationRequest();
        request.setSmallestDisplacement(10);
        request.setFastestInterval(50000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(3);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(request);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());


        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates states = result.getLocationSettingsStates();

                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:
                        setInitialLocation();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    MapViewActivity.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.

                        break;

                }


            }
        });


    }


    private void CheckMapPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

            if (ActivityCompat.checkSelfPermission(MapViewActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(MapViewActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapViewActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1002);
            } else {

                buildGoogleAPiClient();
            }
        } else {
            buildGoogleAPiClient();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1002: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        buildGoogleAPiClient();

                    }
                } else {

                    Toast.makeText(MapViewActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    //finish();
                }
            }
            break;
        }
    }

    private void setInitialLocation() {


        if (ActivityCompat.checkSelfPermission(MapViewActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapViewActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


                mLastLocation = location;

                try {
                    LatLng positionUpdate = new LatLng(location.getLatitude(), location.getLongitude());
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(positionUpdate, 15);
                    mgoogleMap.animateCamera(update);
                    GooglePlacesAutocompleteAdapter adapter = new GooglePlacesAutocompleteAdapter(MapViewActivity.this, R.layout.autocompletelistitem);
                    locationTextView.setAdapter(adapter);
                    getLocationTextView.setAdapter(adapter);

                    locationTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                            String str = (String) adapterView.getItemAtPosition(i);
                            String[] places = str.split("@");
                            String place_id = places[1];

                            locationTextView.setText("");
                            locationTextView.setHint(places[0]);
                            //getLatLng Method is not built-in method, find this method below

                            getLatLang(place_id);
                        }
                    });

                    getLocationTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                            String str = (String) adapterView.getItemAtPosition(i);
                            String[] places = str.split("@");
                            String place_id = places[1];

                            getLocationTextView.setText("");
                            getLocationTextView.setHint(places[0]);
                            //getLatLng Method is not built-in method, find this method below
                          getLatLang1(place_id);
                        }

                    });


                } catch (Exception ex) {

                    ex.printStackTrace();
                    Log.e("MapException", ex.getMessage());

                }

            }

        });
    }

    public void getLatLang1(String placeId) {
        Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess() && places.getCount() > 0) {
                            final Place place = places.get(0);

                            latLngs[0] = place.getLatLng();

                            try {
                                if (locationTextView != null) {
                                    showDirection(latLngs[0], latLngs[1]);




                                    show_status.setVisibility(View.VISIBLE);

                                   SearchStatus();
                                }
                            } catch (Exception ex) {

                                ex.printStackTrace();
                                Log.e("MapException", ex.getMessage());

                            }
                            Log.i("place", "Place found: " + place.getLatLng());
                        } else {
                            Log.e("place", "Place not found");
                        }
                        places.release();
                    }
                });
    }

    public void getLatLang(String placeId) {
        Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess() && places.getCount() > 0) {
                            final Place place = places.get(0);
                            latLngs[1] = place.getLatLng();
                            try {
                                if (getLocationTextView != null) {

                                    showDirection(latLngs[0], latLngs[1]);
                                    show_status.setVisibility(View.VISIBLE);
                                   SearchStatus();
                                }
                            } catch (Exception ex) {

                                ex.printStackTrace();
                                Log.e("MapException", ex.getMessage());

                            }

                            Log.i("place", "Place found: " + place.getLatLng());
                        } else {
                            Log.e("place", "Place not found");
                        }
                        places.release();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult()", Integer.toString(resultCode));

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK: {

                        setInitialLocation();

                        Toast.makeText(MapViewActivity.this, "Location enabled by user!", Toast.LENGTH_LONG).show();
                        mRequestingLocationUpdates = true;
                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(MapViewActivity.this, "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                        mRequestingLocationUpdates = false;
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }

  /*  private void switchFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frag, fragment);
        ft.commit();
    }*/


}
