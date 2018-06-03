package com.example.myapplication;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

public class ShopDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private TextView brand_txview,atmospherev,average_pricev,classv,hoursv,phonev,tagsv,websitev;
    private  Bundle extras;
    private String name,image,address,atmosphere,average_price,classs,hours,menus,phone,tags,website,lat,longitude;
    GoogleMap mGoogleMap;
    private ImageView imageView,menuv;
    MapFragment mapFrag;
    GoogleApiClient mGoogleApiClient;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        extras=getIntent().getExtras();
        name=extras.getString("name");
        image=extras.getString("image");
        address=extras.getString("address");
        atmosphere=extras.getString("atmosphere");
        average_price=extras.getString("average_price");
        classs=extras.getString("class");
        hours=extras.getString("hours");
        menus=extras.getString("menu");
        longitude=extras.getString("lon");
        lat=extras.getString("lat");
        phone=extras.getString("phone");
        tags=extras.getString("tags");
        website=extras.getString("website");

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        AppBarLayout appBarLayout =findViewById(R.id.app_bar);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(name);
        init();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init(){

        brand_txview = findViewById(R.id.brand_txview);
        atmospherev = findViewById(R.id.atmospherev);
        average_pricev = findViewById(R.id.average_pricev);
        classv = findViewById(R.id.classv);
        hoursv = findViewById(R.id.hoursv);
        menuv = findViewById(R.id.menuv);
        phonev = findViewById(R.id.phonev);
        tagsv = findViewById(R.id.tagsv);
        websitev = findViewById(R.id.websitev);
        imageView = findViewById(R.id.ImageView);

        try{
            Picasso.with(getApplicationContext()).load(image).into(imageView);
        }
        catch (Exception e){
            
        }


        websitev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(website));
                startActivity(intent);
            }
        });

        brand_txview.setText(name);
        atmospherev.setText(atmosphere);
        average_pricev.setText(average_price);
        classv.setText(classs);
        hoursv.setText(hours);
        Picasso.with(getApplicationContext()).load(menus).into(menuv);
        phonev.setText(phone);
        tagsv.setText(tags);
        websitev.setText(website);

        mapFrag = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.
                add(R.id.map, mapFrag);
        fragmentTransaction.commit();
        mapFrag.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Double.valueOf(lat), Double.valueOf(longitude))).zoom(17).build();

        googleMap.addMarker(new MarkerOptions().position(
                new LatLng(Double.valueOf(lat), Double.valueOf(longitude))).title(name).snippet(address).icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {

                        }
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
