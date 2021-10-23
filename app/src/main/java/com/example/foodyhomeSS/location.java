package com.example.foodyhomeSS;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class location extends AppCompatActivity {

    //    SupportMapFragment smf;
    TextView textLatlong;
    Button Delivery;
    ProgressBar progress;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

//    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        textLatlong = findViewById(R.id.textView5);
        Delivery = findViewById(R.id.button);
        progress = findViewById(R.id.progressBar2);
//        client = LocationServices.getFusedLocationProviderClient(this);
        //smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Google_map);
        Delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(location.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);

                } else {
                    getCurrentLocation();
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permisssion Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {

        progress.setVisibility(View.VISIBLE);
        LocationRequest locationrequest = new LocationRequest();
        locationrequest.setInterval(10000);
        locationrequest.setFastestInterval(3000);
        locationrequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
            LocationServices.getFusedLocationProviderClient(location.this)
                    .requestLocationUpdates(locationrequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(location.this)
                                    .removeLocationUpdates(this);
                            if (locationResult != null && locationResult.getLocations().size() > 0) {
                                int LatestLocationIndex = locationResult.getLocations().size() - 1;
                                double latitude = locationResult.getLocations().get(LatestLocationIndex).getLatitude();
                                double longitude = locationResult.getLocations().get(LatestLocationIndex).getLongitude();
                                textLatlong.setText(
                                        String.format(
                                                "Latitude: %s\nLongitude: %s",
                                                latitude,
                                                longitude
                                        )

                                );
                            }
                            progress.setVisibility(View.GONE);
                        }
                    }, Looper.getMainLooper());


        }
    }

//if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//        //    ActivityCompat#requestPermissions
//        // here to request the missing permissions, and then overriding
//        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//        //                                          int[] grantResults)
//        // to handle the case where the user grants the permission. See the documentation
//        // for ActivityCompat#requestPermissions for more details.
//
//        }

        //
//                Dexter.withContext(getApplicationContext())
//                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                        .withListener(new PermissionListener() {
//                            @Override
//                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                                getmylocation();
//                            }
//
//                            @Override
//                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//
//                            }
//
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                                permissionToken.continuePermissionRequest();
//                            }
//                        }).check();
//
//
//            }
//        });
//    }
//
//
//    public void getmylocation() {
//
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Task<Location> task = client.getLastLocation();
//        task.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(@NonNull Location location) {
////smf.getMapAsync(new OnMapReadyCallback() {
////    @Override
////    public void onMapReady(GoogleMap googleMap) {
//   LatLng latLng =new LatLng(location.getLatitude(),location.getLongitude());
////        MarkerOptions markerOptions = new MarkerOptions().position(latlng).title("You are here ....");
////        googleMap.addMarker(markerOptions);
////        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng ,15));
//
//    }
//});
//            }
////        });
//    }
//}