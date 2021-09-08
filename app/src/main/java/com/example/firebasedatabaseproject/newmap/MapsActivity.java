package com.example.firebasedatabaseproject.newmap;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.databinding.ActivityMapsBinding;
import com.example.firebasedatabaseproject.googlemap.GoogleMapActivity;
import com.example.firebasedatabaseproject.newmap.comman.Comman;
import com.example.firebasedatabaseproject.newmap.service.IGoogleApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final String TAG = MapsActivity.class.getSimpleName();
    private ActivityMapsBinding binding;
    private Context context;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SupportMapFragment mapFragment;
    private LatLng currentLocation;
    private List<LatLng> polylineList;
    private Marker marker;
    private float v;
    private double lat,lng;
    private Handler handler;
    private LatLng startPosition,endPosition;
    private int index,next;
    private MarkerOptions source;
    private String destination;
    private PolylineOptions polylineOptions,blackPolylineOptions;
    private Polyline blackPolyline,greyPolyline;


    IGoogleApi mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        initializeMap();
        initialize();
    }

    private void initializeMap(){
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

    private void onSuccess(Location location) {
        if (location != null) {
            mapFragment.getMapAsync(googleMap -> {
                mMap = googleMap;
                // mMap.getUiSettings().setZoomControlsEnabled(true);
                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                // Initialize Address list
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    source = new MarkerOptions().position(currentLocation).title(addresses.get(0).getAddressLine(0));
                    // Zoom Map
                    if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    } else {
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setCompassEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        mMap.getUiSettings().setRotateGesturesEnabled(true);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void initialize(){
        polylineList = new ArrayList<>();
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destination = binding.edtPlace.getText().toString();
                destination = destination.replace("","+"); //Replace space to + to make url
                mapFragment.getMapAsync(MapsActivity.this);
            }
        });
        mService = Comman.getGooglApi();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);

      //  mMap.addMarker(new MarkerOptions().position(currentLocation).title(source.getTitle()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(googleMap.getCameraPosition().target).zoom(17).bearing(30).tilt(45).build()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
        String requestUrl = null;
        try {
            requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" + "transit_routing_preference=less_driving&"
                    + "origin=" + currentLocation.latitude + "," + currentLocation.longitude + "&"
                    + "destination=" + destination + "&" + "key=" + getString(R.string.googleMapAPIkey);

            Log.e(TAG,"URL:- "+requestUrl);
            mService.getDataFromGoogleApi(requestUrl).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("routes");
                        for (int i=0; i<jsonArray.length(); i++){
                           JSONObject route = jsonArray.getJSONObject(i);
                           JSONObject poly = route.getJSONObject("overview_polyline");
                           String polyline = poly.getString("points");
                           polylineList = decodePoly(polyline);
                        }

                        //Adjusting bounds
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (LatLng latLng:polylineList)
                            builder.include(latLng);

                        LatLngBounds bounds = builder.build();
                        CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,2);
                        mMap.animateCamera(mCameraUpdate);
                        polylineOptions = new PolylineOptions();
                        polylineOptions.color(Color.GRAY);
                        polylineOptions.width(5);
                        polylineOptions.startCap(new SquareCap());
                        polylineOptions.endCap(new SquareCap());
                        polylineOptions.jointType(JointType.ROUND);
                        polylineOptions.addAll(polylineList);
                        greyPolyline = mMap.addPolyline(polylineOptions);

                        blackPolylineOptions = new PolylineOptions();
                        blackPolylineOptions.color(Color.BLACK);
                        blackPolylineOptions.width(5);
                        blackPolylineOptions.startCap(new SquareCap());
                        blackPolylineOptions.endCap(new SquareCap());
                        blackPolylineOptions.jointType(JointType.ROUND);
                        blackPolylineOptions.addAll(polylineList);
                        blackPolyline = mMap.addPolyline(blackPolylineOptions);

                        mMap.addMarker(new MarkerOptions().position(polylineList.get(polylineList.size() -1)));

                        //Animator
                        ValueAnimator polylineAnimator = ValueAnimator.ofInt(0,100);
                        polylineAnimator.setDuration(2000);
                        polylineAnimator.setInterpolator(new LinearInterpolator());
                        polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                List<LatLng> points = greyPolyline.getPoints();
                                int percentValue = (int) valueAnimator.getAnimatedValue();
                                int size = points.size();
                                int newPoints = (int) (size * (percentValue / 100.0f));
                                List<LatLng> p = points.subList(0,newPoints);
                                blackPolyline.setPoints(p);
                            }
                        });
                        polylineAnimator.start();
                        //Add bike marker
                        marker = mMap.addMarker(new MarkerOptions().position(currentLocation).flat(true).icon(bitmapDescriptorFromVector(context,R.drawable.fooddeliveryimage)));
                        //Bike Moving
                        handler = new Handler();
                        index = -1;
                        next = 1;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (index < polylineList.size()-1){
                                    index++;
                                    next = index+1;
                                }
                                if (index < polylineList.size() -1){
                                    startPosition = polylineList.get(index);
                                    endPosition = polylineList.get(next);
                                }

                                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1);
                                valueAnimator.setDuration(3000);
                                valueAnimator.setInterpolator(new LinearInterpolator());
                                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                       v = valueAnimator.getAnimatedFraction();
                                       lng = v*endPosition.longitude+(1-v)*startPosition.longitude;
                                       lat = v*endPosition.latitude+(1-v)*startPosition.latitude;
                                       LatLng newPositon = new LatLng(lat,lng);
                                       getCurrentLocation();

                                     //   marker.setPosition(currentLocation);
                                      //  marker.setPosition(newPositon);
                                    //   marker.setAnchor(0.5f,1f);
                                       marker.setRotation(getBearing(startPosition, currentLocation));
                                      // marker.setRotation(getBearing(startPosition, newPositon));
                                    }
                                });
                                valueAnimator.start();
                                handler.postDelayed(this,3000);

                            }
                        },3000);

                    }catch (Exception e){
                        e.printStackTrace();
                        Log.e(TAG,"Exception:- "+e);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(MapsActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"onFailure_Exception:- "+t.toString());
                }
            });
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private float getBearing(LatLng startPosition, LatLng newPositon) {
        double lat = Math.abs(startPosition.latitude - newPositon.latitude);
        double lng = Math.abs(startPosition.longitude - newPositon.longitude);

        if (startPosition.latitude < newPositon.latitude && startPosition.longitude < newPositon.longitude)
            return (float)(Math.toDegrees(Math.atan(lng/lat)));
        else if (startPosition.latitude >= newPositon.latitude && startPosition.longitude < newPositon.longitude)
            return (float)((90-Math.toDegrees(Math.atan(lng/lat)))+90);
        else if (startPosition.latitude >= newPositon.latitude && startPosition.longitude >= newPositon.longitude)
            return (float)(Math.toDegrees(Math.atan(lng/lat))+180);
        else if (startPosition.latitude < newPositon.latitude && startPosition.longitude >= newPositon.longitude)
            return (float)((90-Math.toDegrees(Math.atan(lng/lat)))+270);
        return -1;
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

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}