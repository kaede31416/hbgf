package com.example.hbhri;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.qozix.tileview.TileView;
import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.FloatingActionButton;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;

public class MapActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private FloatingActionButton fat;
    private BottomSheetDialog bsd;

    private FrameLayout content;

    private double latitude  = 39.915 * 1E6;
    private double  longitude= 116.404 * 1E6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mToolbar = (Toolbar)findViewById(R.id.toolbar_map);
        content =(FrameLayout)findViewById(R.id.tile_content);
        fat = (FloatingActionButton)findViewById(R.id.bt_floating);

//        TileView tileView = new TileView(this);
//        tileView.setSize(4015, 4057);
//        tileView.addDetailLevel( 1.000f, "tiles/fantasy/1000/%d_%d.jpg");
//        tileView.addDetailLevel( 0.500f, "tiles/fantasy/500/%d_%d.jpg");
//        tileView.addDetailLevel( 0.250f, "tiles/fantasy/250/%d_%d.jpg");
//        tileView.addDetailLevel( 0.125f, "tiles/fantasy/125/%d_%d.jpg" );
//
//        tileView.setScaleLimits( 0, 2 );
//        content.addView(tileView);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            try {
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                longitude = lastKnownLocation.getLongitude();
                latitude = lastKnownLocation.getLatitude();
            }catch (SecurityException e){
                e.printStackTrace();
                Toast.makeText(getBaseContext(),"应用未获取GPS使用权限",Toast.LENGTH_SHORT).show();
            }
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if(location!=null){
                        latitude = location.getLatitude();
                        longitude=location.getLongitude();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }catch (SecurityException e){
            e.printStackTrace();
            Toast.makeText(getBaseContext(),"应用未获取GPS使用权限",Toast.LENGTH_SHORT).show();
        }
        MapView map = (MapView) findViewById(R.id.tdt_map);
        map.setBuiltInZoomControls(true);
        MapController controller = map.getController();
        GeoPoint point = new GeoPoint((int)latitude,(int)longitude);
        controller.setCenter(point);
        controller.setZoom(12);

        mToolbar.setTitle("浏览");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v instanceof FloatingActionButton){
                    ((FloatingActionButton) v).setLineMorphingState((((FloatingActionButton) v).getLineMorphingState()+1)%2,true);
                     bsd = new BottomSheetDialog(MapActivity.this,R.style.Material_App_BottomSheetDialog);
                    //这里进行dialog内容的初始化
                    View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.search_condition,null);
                    MaterialTextField mtf = (MaterialTextField) view.findViewById(R.id.mtf);
                    MaterialTextField mtf2 = (MaterialTextField) view.findViewById(R.id.mtf2);

                    Button cancel = (Button)view.findViewById(R.id.cancel);
                    Button confirm =(Button)view.findViewById(R.id.confirm);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bsd.dismiss();
                            fat.setLineMorphingState((fat.getLineMorphingState()+1)%2,true);
                            Toast.makeText(getBaseContext(),"canceled",Toast.LENGTH_LONG).show();
                        }
                    });

                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bsd.dismiss();
                            fat.setLineMorphingState((fat.getLineMorphingState()+1)%2,true);
                            Toast.makeText(getBaseContext(),"confrimed",Toast.LENGTH_LONG).show();
                        }
                    });
                    bsd.contentView(view).heightParam(ViewGroup.LayoutParams.MATCH_PARENT).cancelable(false).inDuration(500).show();
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_open,R.anim.activity_close);
    }


}
