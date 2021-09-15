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
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.commanclasses.Constants;
import com.example.firebasedatabaseproject.databinding.ActivityMapBinding;
import com.example.firebasedatabaseproject.googlemap.service.TaskReloadedCallback;
import com.example.firebasedatabaseproject.newmap.comman.BaseUrlClass;
import com.example.firebasedatabaseproject.newmap.retrofit.IGoogleApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final String TAG = MapsActivity.class.getSimpleName();
    private ActivityMapBinding binding;
    private Context context;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SupportMapFragment mapFragment;
    private LatLng currentLocation;
    private LatLng newLocation;
    private List<LatLng> polylineList;
    private Marker marker;
    private float v;
    private double lat, lng;
    private Handler handler;
    private LatLng startPosition, endPosition;
    private int index, next;
    MarkerOptions source;
    private String destination;
    private PolylineOptions grayPolylineOptions, bluePolylineOptions;
    private Polyline bluePolyline, greyPolyline;
    private String dist = "";
    private String time = "";
    private String endLocationLAT = "";
    private String endLocationLNG = "";

    private IGoogleApi mService;
    private LocationRequest mLocationRequest;

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                Log.e(TAG, "locationCallback:- " + null);
                return;
            }
            for (Location location : locationResult.getLocations()) {
                getDurationUpdate();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        initializeMap();
        initialize();
    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    private void initializeMap() {
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
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void initialize() {
        mService = BaseUrlClass.getGooglApi();
        polylineList = new ArrayList<>();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(30000);// 30 sec Update Latlng
        mLocationRequest.setFastestInterval(30000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        binding.ivDirectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destination = binding.edtPlace.getText().toString();
                destination = destination.replace("", "+"); //Replace space to + to make url
                mapFragment.getMapAsync(MapsActivity.this);
                binding.crdTimeDuration.setVisibility(View.VISIBLE);
            }
        });

    }

    private void getDurationUpdate(){
        String requestUrl = Constants.BASEURL + Constants.MODE + Constants.TRANSITROUTE
                + Constants.SOURCE + newLocation.latitude + Constants.OR
                + newLocation.longitude + Constants.AND /*+ waypoints + Constants.AND*/
                + Constants.DESTINATION + destination + Constants.SENSOR + Constants.AND + Constants.KEY + getString(R.string.googleMapAPIkey);

        mService.getDataFromGoogleApi(requestUrl).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    JSONArray routeArray = jsonObject.getJSONArray(Constants.ROUTES);

                    for (int i = 0; i < routeArray.length(); i++) {
                        JSONObject route = routeArray.getJSONObject(i);
                        JSONArray legs = route.getJSONArray(Constants.LEGS);
                        for (int j = 0; j < legs.length(); j++) {
                            JSONObject distObj = legs.getJSONObject(j);
                            JSONObject distance = distObj.getJSONObject(Constants.DISTANCE);
                            dist = distance.getString(Constants.TEXT);
                            JSONObject duration = distObj.getJSONObject(Constants.DURATION);
                            time = duration.getString(Constants.TEXT);

                            Toast.makeText(context, "Call Method in 1 min....", Toast.LENGTH_SHORT).show();

                            binding.txvDuration.setText(MessageFormat.format("Time  {0}", time));
                            binding.txvDistance.setText(String.format("Distance  %s", dist));
                        }

                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e(TAG, "Exception:- " + e);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MapsActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure_Exception:- " + t.toString());
            }
        });
    }

    private int getNodeIndex(NodeList nl, String nodename) {
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeName().equals(nodename))
                return i;
        }
        return -1;
    }

    private void getInstructions(String requestUrl) {
        Log.e(TAG,"getInstructions");
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            builder = builderFactory.newDocumentBuilder();
            Document document = null;
            String xml = requestUrl.replaceAll(">\\s+<", "><").trim();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            document = builder.parse(is);

            ArrayList<String> instructions = new ArrayList<String>();
            NodeList stepNodes = document.getElementsByTagName("steps");

            if (stepNodes.getLength() > 0) {
                for (int x = 0; x < stepNodes.getLength(); x++) {
                    Node currentStepNode = stepNodes.item(x);
                    NodeList currentStepNodeChildren = currentStepNode.getChildNodes();
                    Node currentStepInstruction = currentStepNodeChildren.item(getNodeIndex(currentStepNodeChildren, "html_instructions"));
                    instructions.add(currentStepInstruction.getTextContent());
                }
            }

            Log.e(TAG, "instructions:-" + instructions);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(googleMap.getCameraPosition().target).zoom(17).bearing(30).tilt(45).build()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
        String requestUrl = null;
        try {
            String waypoints = "waypoints=optimize:true|";
            requestUrl = Constants.BASEURL + Constants.MODE + Constants.TRANSITROUTE
                    + Constants.SOURCE + currentLocation.latitude + Constants.OR
                    + currentLocation.longitude + Constants.AND /*+ waypoints + Constants.AND*/
                    + Constants.DESTINATION + destination + Constants.SENSOR + Constants.AND + Constants.KEY + getString(R.string.googleMapAPIkey);

         //   getInstructions(requestUrl);
            Log.e(TAG, "URL:- " + requestUrl);
            mService.getDataFromGoogleApi(requestUrl).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        Log.e(TAG,"ResponseBody:- "+response.body().toString());

                        JSONArray routeArray = jsonObject.getJSONArray(Constants.ROUTES);
                        for (int i = 0; i < routeArray.length(); i++) {
                            JSONObject route = routeArray.getJSONObject(i);
                            JSONArray legs = route.getJSONArray(Constants.LEGS);
                            for (int j = 0; j < legs.length(); j++) {
                                JSONObject distObj = legs.getJSONObject(j);
                                JSONArray jSteps = distObj.getJSONArray("steps");
                                JSONObject distance = distObj.getJSONObject(Constants.DISTANCE);
                                dist = distance.getString(Constants.TEXT);
                                JSONObject duration = distObj.getJSONObject(Constants.DURATION);
                                time = duration.getString(Constants.TEXT);

                                JSONObject endLocation = distObj.getJSONObject("end_location");
                                JSONObject startlocation = distObj.getJSONObject("start_location");
                                double startLat = Double.parseDouble(startlocation.getString("lat"));
                                double startLng = Double.parseDouble(startlocation.getString("lng"));

                                endLocationLAT = (endLocation.getString("lat"));
                                endLocationLNG = (endLocation.getString("lng"));

                                double endtLat = Double.parseDouble(endLocation.getString("lat"));
                                double endtLng = Double.parseDouble(endLocation.getString("lng"));

                                binding.txvDistance.setText(String.format("Distance  %s", dist));
                                binding.txvDuration.setText(MessageFormat.format("Time  {0}", time));

                                getDistanceFromLatLong(startLat, startLng, endtLat, endtLng);

                                for (int k=0; k<jSteps.length(); k++){
                                    JSONObject jstepsObjct = jSteps.getJSONObject(k);
                                    /*String turn = jstepsObjct.getString("maneuver");*/
                                   // Toast.makeText(context, "Direction Turn:- "+jstepsObjct, Toast.LENGTH_SHORT).show();
                                }
                            }
                            JSONObject poly = route.getJSONObject(Constants.OVERVIEWPOLYLINE);
                            String polyline = poly.getString(Constants.POINTS);
                            polylineList = decodePoly(polyline);

                           /* if(i==0) {
                                Polyline line = mMap.addPolyline(new PolylineOptions().addAll(polylineList).width(10).color(Color.BLUE).geodesic(true));
                                newPolylinelst.add(line);
                                lstPolyLineModel.add(new PolyLineModel(dist,time,newPolylinelst));
                                mMap.addMarker(destination);
                            }
                            else{
                                Polyline line = mMap.addPolyline(new PolylineOptions().addAll(polylineList).width(10).color(Color.DKGRAY).geodesic(true));
                                newPolylinelst.add(line);
                                lstPolyLineModel.add(new PolyLineModel(dist,time,newPolylinelst));
                            }*/

                        }

                        //Adjusting bounds
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (LatLng latLng : polylineList)
                            builder.include(latLng);

                        LatLngBounds bounds = builder.build();
                        CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
                        mMap.animateCamera(mCameraUpdate);
                        grayPolylineOptions = new PolylineOptions();
                        grayPolylineOptions.color(Color.GRAY);
                        grayPolylineOptions.width(5);
                        grayPolylineOptions.startCap(new SquareCap());
                        grayPolylineOptions.endCap(new SquareCap());
                        grayPolylineOptions.jointType(JointType.ROUND);
                        grayPolylineOptions.addAll(polylineList);
                        greyPolyline = mMap.addPolyline(grayPolylineOptions);

                        bluePolylineOptions = new PolylineOptions();
                        bluePolylineOptions.color(Color.BLUE);
                        bluePolylineOptions.width(5);
                        bluePolylineOptions.startCap(new SquareCap());
                        bluePolylineOptions.endCap(new SquareCap());
                        bluePolylineOptions.jointType(JointType.ROUND);
                        bluePolylineOptions.addAll(polylineList);
                        bluePolyline = mMap.addPolyline(bluePolylineOptions);

                        mMap.addMarker(new MarkerOptions().position(polylineList.get(polylineList.size() - 1)));

                        //Animator
                        ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
                        polylineAnimator.setDuration(2000);
                        polylineAnimator.setInterpolator(new LinearInterpolator());
                        polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                List<LatLng> points = greyPolyline.getPoints();
                                int percentValue = (int) valueAnimator.getAnimatedValue();
                                int size = points.size();
                                int newPoints = (int) (size * (percentValue / 100.0f));
                                List<LatLng> p = points.subList(0, newPoints);
                                bluePolyline.setPoints(p);
                            }
                        });
                        polylineAnimator.start();
                        //Add bike marker
                        marker = mMap.addMarker(new MarkerOptions().position(currentLocation).flat(true).icon(bitmapDescriptorFromVector(context, R.drawable.fooddeliveryimage)));
                        //Bike Moving
                        handler = new Handler();
                        index = -1;
                        next = 1;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (index < polylineList.size() - 1) {
                                    index++;
                                    next = index + 1;
                                }
                                if (index < polylineList.size() - 1) {
                                    startPosition = polylineList.get(index);
                                    endPosition = polylineList.get(next);
                                }

                                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                                valueAnimator.setDuration(3000);
                                valueAnimator.setStartDelay(3000);
                                valueAnimator.setInterpolator(new LinearInterpolator());
                                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        v = valueAnimator.getAnimatedFraction();
                                        lng = v * endPosition.longitude + (1 - v) * startPosition.longitude;
                                        lat = v * endPosition.latitude + (1 - v) * startPosition.latitude;
                                        LatLng newPositon = new LatLng(lat, lng);
                                        //  getCurrentLocation();
                                        Location location1 = new Location("");
                                        location1.setLatitude(Double.parseDouble(endLocationLAT));
                                        location1.setLongitude(Double.parseDouble(endLocationLNG));

                                        newLocation = new LatLng(startPosition.latitude, startPosition.longitude);
                                        startLocationUpdate();

                                        Location location2 = new Location("");
                                        location2.setLatitude(startPosition.latitude);
                                        location2.setLongitude(startPosition.longitude);

                                        float distanceInMeters = location1.distanceTo(location2);
                                        float newDistance = Float.parseFloat(String.format(Locale.US, "%.2f", distanceInMeters / 1000));

                                        binding.txvGetNewDistance.setText(String.format("Distance  %s", newDistance + " Km"));
                                        binding.txvGetNewDuration.setText("Duration " + getTimeTaken(location2, location1));


                                        Log.e(TAG, "distanceInMeters:- " + distanceInMeters);

                                        // marker.setPosition(currentLocation);
                                        marker.setPosition(newPositon);
                                        //   marker.setAnchor(0.5f,1f);
                                        //  marker.setRotation(getBearing(startPosition, currentLocation));
                                        marker.setRotation(getBearing(startPosition, newPositon));
                                    }
                                });
                                valueAnimator.start();
                                handler.postDelayed(this, 3000);

                            }
                        }, 3000);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "Exception:- " + e);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(MapsActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onFailure_Exception:- " + t.toString());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float getBearing(LatLng startPosition, LatLng newPositon) {
        double lat = Math.abs(startPosition.latitude - newPositon.latitude);
        double lng = Math.abs(startPosition.longitude - newPositon.longitude);

        if (startPosition.latitude < newPositon.latitude && startPosition.longitude < newPositon.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (startPosition.latitude >= newPositon.latitude && startPosition.longitude < newPositon.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (startPosition.latitude >= newPositon.latitude && startPosition.longitude >= newPositon.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (startPosition.latitude < newPositon.latitude && startPosition.longitude >= newPositon.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
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

    public static String getDistanceFromLatLong(Double lat1, Double lng1, Double lat2, Double lng2) {
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);
        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);
        float distanceInMeters = loc1.distanceTo(loc2);
        float newDistance = distanceInMeters / 1000;
        Log.e("TAG", "newDistance: " + String.format(Locale.US, "%.2f", newDistance));

        return String.format(Locale.US, "%.2f", distanceInMeters / 1000);
    }

    public String getTimeTaken(Location source, Location dest) {


        double meter = source.distanceTo(dest);

        double kms = meter / 1000;

        double kms_per_min = 0.5;

        double mins_taken = kms / kms_per_min;

        int totalMinutes = (int) mins_taken;

        Log.d("ResponseT","meter :"+meter+ " kms : "+kms+" mins :"+mins_taken);

        if (totalMinutes<60)
        {
            Log.e(TAG,"totalMinutes:- "+totalMinutes+" mins");
            return ""+totalMinutes+" mins";
        }else {
            String minutes = Integer.toString(totalMinutes % 60);
            minutes = minutes.length() == 1 ? "0" + minutes : minutes;
            Log.e(TAG,"ElsetotalMinutes:- "+(totalMinutes / 60) + " hour " + minutes +"mins");
            return (totalMinutes / 60) + " hour " + minutes +"mins";

        }
    }

}