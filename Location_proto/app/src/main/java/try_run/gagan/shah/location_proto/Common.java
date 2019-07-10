package try_run.gagan.shah.location_proto;

import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class Common extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap,mMap2;
    private LatLng sydney;
    private ImageView imageView;
    Marker markers;
    private MapView mMapView;
    MarkerOptions markero,markerOptions;
    Marker marker,m,marker1,marker2,marker3;
    String x;
    private double logg, lt;
    private static final int MY_ACCESS_FINE_LOCATION=101;
    private static final int MY_ACCESS_COARSE_LOCATION=102;
    private boolean permissionIsGranted=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mapFragment = (com.google.android.gms.maps.MapFragment)
//                getChildFragmentManager() .findFragmentById(R.id.map_fragment_job_single);
//        mapFragment.getMapAsync(this);

        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
                .compassEnabled(false)
                .rotateGesturesEnabled(false)
                .tiltGesturesEnabled(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_common, container, false);
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(this);

        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MapsInitializer.initialize(getContext());
        mMapView.postInvalidate();
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        GPS gps = new GPS(getActivity());
        gps.location(new GPS.Update() {
            @Override
            public void Updatelocation(GoogleApiClient mGoogleApiClient, LocationRequest mLocationRequest) {
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION},MY_ACCESS_FINE_LOCATION);
                    }else {
                        permissionIsGranted=true;
                    }
                    return;
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        String lati = String.valueOf(location.getLatitude());
                        String longi = String.valueOf(location.getLongitude());
                        logg=Double.valueOf(longi);
                        lt=Double.valueOf(lati);
                        sydney = new LatLng( lt,logg);
                        if(m!=null){
                            m.remove();
                        }
                        markerOptions=new MarkerOptions()
                                .position(sydney)
                                .title("Melbourne")
                                .snippet("Population: 4,137,400")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.char4));
                       m=mMap.addMarker(markerOptions);
                        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(sydney, 14.0f) );

                    }
                });
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                        .addLocationRequest(mLocationRequest);
                PendingResult<LocationSettingsResult> result =
                        LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
                result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        final LocationSettingsStates state = result.getLocationSettingsStates();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                // All location settings are satisfied. The client can initialize location
                                // requests here.
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the user
                                // a dialog.
                                try {
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    status.startResolutionForResult(
                                            getActivity(), 1000);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                break;
                        }
                    }
                });

            }
        });

//
        //  mMap.addMarker(new MarkerOptions().position(sydney).title(x));

        //       MarkerOptions marker = new MarkerOptions().position(sydney).title("Hello Maps");
        //      marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.char));
//                        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
//                        Bitmap bmp = Bitmap.createBitmap(80, 80, conf);
//                        Canvas canvas1 = new Canvas(bmp);
//
//// paint defines the text color, stroke width and size
//                        Paint color = new Paint();
//// modify canvas
//                        canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
//                                R.drawable.char0), 0,0, color);


        LatLng person1 = new LatLng( 30.718738,76.679678);
        mMap.addMarker(new MarkerOptions()
                .position(person1)
                .title("person1")
                .snippet("Population: 4,137,400")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.char4)));

        LatLng person2 = new LatLng( 30.720032,76.689892);
        mMap.addMarker(new MarkerOptions()
                .position(person2)
                .title("person2")
                .snippet("Population: 4,137,400")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.char4)));
        LatLng person3 = new LatLng( 30.715384,76.692402);
        mMap.addMarker(new MarkerOptions()
                .position(person3)
                .title("person3")
                .snippet("Population: 4,137,400")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.char4)));
        LatLng person4 = new LatLng( 30.708762,76.696179);

        markero = new MarkerOptions();
        if(marker!=null){
            marker.remove();
        }
        markero.position(person4).title("person4")
                .snippet("Population: 4,137,400")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.char4));
       marker= mMap.addMarker(markero);
        marker_animation(marker,markero);
    }
boolean marker_color_bool=true;
    Handler handler=new Handler();

    private void marker_animation(final Marker marker,final MarkerOptions markero) {
        markers=marker;
        handler.postDelayed(new Runnable() {
            public void run() {


                Log.e("running",""+marker_color_bool);

                if(marker_color_bool == true)
                {
                    if(markers!=null){
                        markers.remove();
                        Log.e("marker",""+markers);
                    }
                    mMapView.postInvalidate();
                    markero.icon(BitmapDescriptorFactory.fromResource(R.drawable.pic2));
                    markers=mMap.addMarker(markero);
                    marker_color_bool = false;
                }
                else
                {
                    if(markers!=null){
                        markers.remove();
                        Log.e("marker",""+markers);
                    }
                    markero.icon(BitmapDescriptorFactory.fromResource(R.drawable.pic6));
                    markers=mMap.addMarker(markero);
                    marker_color_bool = true;
                }

                handler.postDelayed(this, 500);
            }
        }, 500);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_ACCESS_COARSE_LOCATION:{
                break;
            }

            case MY_ACCESS_FINE_LOCATION:{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    permissionIsGranted=true;
                }
                else{

                    permissionIsGranted=false;
                }
                break;
            }
        }
    }
}
