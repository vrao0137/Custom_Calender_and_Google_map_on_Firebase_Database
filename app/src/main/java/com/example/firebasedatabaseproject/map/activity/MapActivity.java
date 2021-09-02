package com.example.firebasedatabaseproject.map.activity;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.databinding.ActivityMapBinding;
import com.example.firebasedatabaseproject.map.activity.models.Route;
import com.example.firebasedatabaseproject.map.activity.service.DataParser;
import com.example.firebasedatabaseproject.map.activity.service.FetchURL;
import com.example.firebasedatabaseproject.map.activity.service.TaskLoadedCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private final String TAG = MapActivity.class.getSimpleName();
    private ActivityMapBinding binding;
    private Context context;

    private GoogleMap mMap;
    private MarkerOptions source, dastination;
    private Polyline currentPolyline;
    private ArrayList<Route> lstOfDuration = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialize();
    }

    /*public void parseData(JSONObject jDistance, JSONObject jDuration){
        binding.txvDistance.setText(jDistance.toString());
        binding.txvDuration.setText(jDuration.toString());

        Log.e(TAG,"TAG_jDistance"+jDistance);
        Log.e(TAG,"TAG_jDuration"+jDuration);
    }*/

    private void initialize(){
        context = this;

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        source = new MarkerOptions().position(new LatLng(22.765210366707027, 75.88521583570233)).title("ICAI Indore Branch");
        dastination = new MarkerOptions().position(new LatLng(22.69235335427362, 75.86755672786444)).title("Bhawarkua Squar");


        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* String url = getMapsApiDirectionsUrl(source.getPosition(), dastination.getPosition(), "driving");
                ReadTask downloadTask = new ReadTask();
                downloadTask.execute(url, "driving");*/
                new FetchURL(context).execute(getMapsApiDirectionsUrl(source.getPosition(), dastination.getPosition(), "DRIVING"), "DRIVING");

                DataParser.DataDistance(binding.txvDistance);
                DataParser.DataDuration(binding.txvDuration);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(source);
        mMap.addMarker(dastination);
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

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

}