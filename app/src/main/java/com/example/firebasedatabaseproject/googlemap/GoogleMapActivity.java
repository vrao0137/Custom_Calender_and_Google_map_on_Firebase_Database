package com.example.firebasedatabaseproject.googlemap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.commanclasses.PrograssBar;
import com.example.firebasedatabaseproject.databinding.ActivityGoogleMapBinding;
import com.example.firebasedatabaseproject.googlemap.service.HttpConnection;
import com.example.firebasedatabaseproject.googlemap.service.TaskLoadedCallback;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class GoogleMapActivity extends AppCompatActivity implements TaskLoadedCallback {
    private final String TAG = GoogleMapActivity.class.getSimpleName();
    private ActivityGoogleMapBinding binding;
    private Context context;
    private GoogleMap mMap;
    private MarkerOptions destination;
    private Polyline currentPolyline;
    private PrograssBar prograssBar;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private SupportMapFragment supportMapFragment;

    int LOCATION_REQUEST_CODE = 10001;
    LocationRequest locationRequest;

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                Log.e(TAG, "locationCallback:- " + locationResult);
                return;
            }
            for (Location location : locationResult.getLocations()) {
                Log.e(TAG, "OnLocationResult:- " + location.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoogleMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mapInitialize();
        initialize();
    }

    private void mapInitialize() {
        // Initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(50000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void checkSettingsAndStartLocationUpdates() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);

        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdate();
            }
        });

        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(GoogleMapActivity.this, 1001);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLoactionUpdate();
    }

    private void stopLoactionUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void initialize() {
        context = this;

        destination = new MarkerOptions().position(new LatLng(22.69235335427362, 75.86755672786444)).title("Bhawarkua Squar");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // When permisison granted
            getCurrentLocation();
        } else {
            // When permission denid
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull GoogleMap googleMap) {
                                mMap = googleMap;
                                Geocoder geocoder = new Geocoder(GoogleMapActivity.this, Locale.getDefault());
                                // Initialize Address list
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(addresses.get(0).getAddressLine(0));
                                    // Zoom Map
                                    if (ActivityCompat.checkSelfPermission(GoogleMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GoogleMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        return;
                                    }else {
                                        googleMap.setMyLocationEnabled(true);
                                    }

                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));

                                   // googleMap.addMarker(markerOptions);

                                    binding.ivDirectionBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            checkSettingsAndStartLocationUpdates();
                                            /*source = new MarkerOptions().position(new LatLng(22.765210366707027, 75.88521583570233)).title("ICAI Indore Branch");*/
                                            String url = getMapsApiDirectionsUrl(markerOptions.getPosition(), destination.getPosition(), "DRIVING");
                                            ReadTask downloadTask = new ReadTask();
                                            downloadTask.execute(url, "DRIVING");
                                            binding.txvDurationName.setVisibility(View.VISIBLE);
                                            binding.txvDuration.setVisibility(View.VISIBLE);
                                            binding.txvDistanceName.setVisibility(View.VISIBLE);
                                            binding.txvDistance.setVisibility(View.VISIBLE);
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
                checkSettingsAndStartLocationUpdates();
            }
        }
    }

    class ReadTask extends AsyncTask<String, Void, String> {
        private String directionMode = "DRIVING";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startProgressHud();
        }

        @Override
        protected String doInBackground(String... url) {
            String data = "";
            directionMode = url[1];
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask(context,directionMode);
            parserTask.execute(result);
        }
    }

    public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>>{
        private TaskLoadedCallback taskCallback;
        private String directionMode = "DRIVING";

        public ParserTask(Context mContext, String directionMode) {
            this.taskCallback = (TaskLoadedCallback) mContext;
            this.directionMode = directionMode;
        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DataParser parser = new DataParser();
                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                Log.d("mylog", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            /*Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String ABC = gson.toJson(result);
            Log.e(TAG,"Response123result:- "+ABC);*/

            if (result.isEmpty()) {
                dismissProgressHud();
                Toast.makeText(context, "Data Not Found.....", Toast.LENGTH_SHORT).show();
            } else {
                dismissProgressHud();
                DataParser.DataDistance(binding.txvDistance);
                DataParser.DataDuration(binding.txvDuration);

                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<>();
                    lineOptions = new PolylineOptions();
                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);
                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }
                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    if (directionMode.equalsIgnoreCase("walking")) {
                        lineOptions.width(10);
                        lineOptions.color(Color.MAGENTA);
                    } else {
                        lineOptions.width(20);
                        lineOptions.color(Color.BLUE);
                    }
                }
                // Drawing polyline in the Google Map for the i-th route
                if (lineOptions != null) {
                    //mMap.addPolyline(lineOptions);
                    taskCallback.onTaskDone(lineOptions);
                } else {
                    Log.d("mylog", "without Polylines drawn");
                }
            }

        }
    }

    private String getMapsApiDirectionsUrl(LatLng sourc, LatLng dest, String directionMode) {
        // Output format
        String output = "json?";
        // Origin of route
        String strSource = "origin=" + sourc.latitude + "," + sourc.longitude;
        // Destination of route
        String strDestination = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = strSource + "&" + strDestination + "&" + mode;
        // Key Or map
        String parKey = "&key=" + getString(R.string.googleMapAPIkey);
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + parameters + parKey;
        Log.e(TAG, "getMapsApiDirectionsUrl: " + url);
        return url;
    }


   /* @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(source);
        mMap.addMarker(dastination);
        dismissProgressHud();
    }*/

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null){
            currentPolyline.remove();
        }else {
            dismissProgressHud();
            mMap.addMarker(destination);
            currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
        }
    }

    public void startProgressHud() {
        if (prograssBar == null)
            prograssBar = PrograssBar.show(this, true, false, null);
        else if (!prograssBar.isShowing())
            prograssBar = PrograssBar.show(this, true, false, null);
    }

    public void dismissProgressHud() {
        if (prograssBar != null)
            prograssBar.dismiss();
    }
}