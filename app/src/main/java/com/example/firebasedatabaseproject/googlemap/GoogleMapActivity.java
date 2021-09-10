package com.example.firebasedatabaseproject.googlemap;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.commanclasses.PrograssBar;
import com.example.firebasedatabaseproject.databinding.ActivityGoogleMapBinding;
import com.example.firebasedatabaseproject.googlemap.model.DataParser;
import com.example.firebasedatabaseproject.googlemap.model.PolyLineModel;
import com.example.firebasedatabaseproject.googlemap.service.HttpConnection;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private final String TAG = GoogleMapActivity.class.getSimpleName();
    private ActivityGoogleMapBinding binding;
    private Context context;
    private GoogleMap mMap;
    private MarkerOptions source, destination;
    private PrograssBar prograssBar;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private SupportMapFragment supportMapFragment;

    private LocationRequest mLocationRequest;
    Location mLastLocation;
    private PolylineOptions lineOptions;

    private ArrayList<Polyline> polylineList;
    private String dist = "";
    private String time = "";
    private final ArrayList<PolyLineModel> lstPolyLineModel = new ArrayList<>();

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                Log.e(TAG, "locationCallback:- " + null);
                return;
            }
            for (Location location : locationResult.getLocations()) {
                mLastLocation = location;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoogleMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeMap();
        initialize();
    }


    private void initializeMap() {
        // Initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(1000);// 1 Sec Update Latlng
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    private void checkSettingsAndStartLocationUpdates() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);

        locationSettingsResponseTask.addOnSuccessListener(locationSettingsResponse -> startLocationUpdate());

        locationSettingsResponseTask.addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                ResolvableApiException apiException = (ResolvableApiException) e;
                try {
                    apiException.startResolutionForResult(GoogleMapActivity.this, 1001);
                } catch (IntentSender.SendIntentException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.getMainLooper());
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(this::onSuccess);
        }

    }

    private void callGoogleAPI(MarkerOptions markerOptions) {
        String url = getMapsApiDirectionsUrl(markerOptions.getPosition(), destination.getPosition(), "DRIVING");
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url, "DRIVING");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
                checkSettingsAndStartLocationUpdates();
            }
        }
    }

    private void onSuccess(Location location) {
        if (location != null) {
            supportMapFragment.getMapAsync(googleMap -> {
                lineOptions = new PolylineOptions();
                polylineList = new ArrayList<>();

                mMap = googleMap;
                // mMap.getUiSettings().setZoomControlsEnabled(true);
                Geocoder geocoder = new Geocoder(GoogleMapActivity.this, Locale.getDefault());
                // Initialize Address list
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    source = new MarkerOptions().position(latLng).title(addresses.get(0).getAddressLine(0));
                    // Zoom Map
                    if (ActivityCompat.checkSelfPermission(GoogleMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GoogleMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    } else {
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setCompassEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        mMap.getUiSettings().setRotateGesturesEnabled(true);
                    }
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                    binding.ivDirectionBtn.setOnClickListener(this::onClick);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void onClick(View v) {
        checkSettingsAndStartLocationUpdates();
        callGoogleAPI(source);
        // Drawing polyline in the Google Map for the i-th route
        mMap.setOnMapClickListener(latLng1 -> {
            for (Polyline polyline : polylineList) {
                for (LatLng polyCords : polyline.getPoints()) {
                    float[] results = new float[1];
                    Location.distanceBetween(latLng1.latitude, latLng1.longitude, polyCords.latitude, polyCords.longitude, results);

                    if (results[0] < 100) {
                        mMap.clear();
                        for (Polyline pl : polylineList) {
                            if (pl != polyline) {
                                mMap.addPolyline(new PolylineOptions().width(10).color(Color.DKGRAY).addAll(pl.getPoints()));
                            } else {
                                mMap.addPolyline(new PolylineOptions().width(10).color(Color.BLUE).addAll(pl.getPoints()));
                                mMap.addMarker(destination);

                                for (int i = 0; i < lstPolyLineModel.size(); i++) {
                                    if (pl.getId().equals(lstPolyLineModel.get(i).getPolyline().get(i).getId())) {
                                        binding.txvDistance.setText(String.format("Distance  %s", lstPolyLineModel.get(i).getDistance()));
                                        binding.txvDuration.setText(MessageFormat.format("Time  {0}", lstPolyLineModel.get(i).getDuration()));
                                    } else {
                                        Log.e(TAG, "ElseCondition " + lstPolyLineModel.get(i).getPolyline().get(i).getId());
                                    }
                                }
                                Log.e(TAG, "results:- " + pl.getId());
                            }
                        }

                    }

                }
            }
            Log.e("PolyLine:- ", polylineList.toString());

        });
        binding.txvDistance.setVisibility(View.VISIBLE);
        binding.txvDuration.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onLocationChanged(Location location) {

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
            ParserTask parserTask = new ParserTask(directionMode);
            parserTask.execute(result);
            try {
                dismissProgressHud();
                JSONObject jsonRootObject = new JSONObject(result);
                JSONArray routeArray = jsonRootObject.getJSONArray("routes");
                lstPolyLineModel.clear();
                Log.e("ROUTES ", routeArray.toString());
                for (int i = routeArray.length() - 1; i >= 0; i--) {
                    lineOptions = new PolylineOptions();
                    JSONObject routes = routeArray.getJSONObject(i);
                    JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
                    JSONArray legs = routes.getJSONArray("legs");

                    for (int j = 0; j < legs.length(); j++) {
                        JSONObject distObj = legs.getJSONObject(j);
                        JSONObject distance = distObj.getJSONObject("distance");
                        dist = distance.getString("text");
                        JSONObject duration = distObj.getJSONObject("duration");
                        time = duration.getString("text");

                        binding.txvDistance.setText(String.format("Distance  %s", dist));
                        binding.txvDuration.setText(MessageFormat.format("Time  {0}", time));

                    }
                    String encodedString = overviewPolylines.getString("points");
                    List<LatLng> list = decodePoly(encodedString);

                    /*Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String ABC = gson.toJson(lstPolyLineModel);*/

                    if (i == 0) {
                        Polyline line = mMap.addPolyline(new PolylineOptions().addAll(list).width(10).color(Color.BLUE).geodesic(true));
                        polylineList.add(line);
                        //  polyline_path = mMap.addPolyline(new PolylineOptions().addAll(list).width(10).color(Color.BLUE).geodesic(true));
                        lstPolyLineModel.add(new PolyLineModel(dist, time, polylineList));
                        Log.e(TAG, "newLsPolylineList:- " + lstPolyLineModel.get(i).getPolyline());
                        mMap.addMarker(destination);
                    } else {
                        Polyline line = mMap.addPolyline(new PolylineOptions().addAll(list).width(10).color(Color.DKGRAY).geodesic(true));
                        polylineList.add(line);
                        lstPolyLineModel.add(new PolyLineModel(dist, time, polylineList));
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }


    public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        private String directionMode = "DRIVING";

        public ParserTask(String directionMode) {
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
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;

            if (result.isEmpty()) {
                dismissProgressHud();
                Toast.makeText(context, "Data Not Found.....", Toast.LENGTH_SHORT).show();
            } else {
                dismissProgressHud();
                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<>();
                    lineOptions = new PolylineOptions();
                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);
                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(Objects.requireNonNull(point.get("lat")));
                        double lng = Double.parseDouble(Objects.requireNonNull(point.get("lng")));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }
                }
                // Adding all the points in the route to LineOptions
                assert points != null;
                lineOptions.addAll(points);
                lineOptions.width(10);
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
        // Sensor enabled
        String sensor = "sensor=false&alternatives=true&units=metric";
        // Building the parameters to the web service
        String parameters = strSource + "&" + strDestination + "&" + "&" + mode + "&" + sensor;
        // Key Or map
        String parKey = "&key=" + getString(R.string.googleMapAPIkey);
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + parameters + parKey;
        Log.e(TAG, "getMapsApiDirectionsUrl: " + url);
        return url;
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