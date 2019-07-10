package try_run.gagan.shah.location_proto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginPage extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    public static final String PREFS_NAME = "MyPrefsFile";
    private FloatingActionButton button;
    private ConnectivityReceiver connectivityReceiver;
    private String name;
    private static final int REQUEST_READ_CONTACTS = 0;
    private  String message;
    int color;
    private ProgressBar progressBar;
    private String mobile;
    private EditText editText1, editText2;
    public static Activity fa;
    String longi;
    String status,id;
    int ids;
    String nameid;
    boolean runfree=true;
    String  mobileid;
    String latitudeid,locationid;
    String logitudeid;
    AlertDialog alertDialog;
    SharedPreferences settings;
    String lati;
    int i=0;

    private static final int MY_ACCESS_FINE_LOCATION = 101;
    private static final int MY_ACCESS_COARSE_LOCATION = 102;
    private boolean permissionIsGranted = false,stop=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        fa = this;
        editText1 = (EditText) findViewById(R.id.user);
        editText2 = (EditText) findViewById(R.id.mobile);
        editText1.setTypeface(Typeface.SANS_SERIF);
        editText2.setTypeface(Typeface.SANS_SERIF);
        button = (FloatingActionButton) findViewById(R.id.button);
        progressBar=(ProgressBar)findViewById(R.id.progressBar3);
        settings = getSharedPreferences(PREFS_NAME, 0);
        progressBar.invalidate();
        progressBar.setVisibility(View.INVISIBLE);
//Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

        if (hasLoggedIn) {
            Intent intent = new Intent(LoginPage.this, MainActivity.class);
         //   intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            fa.setVisible(false);
            fa.finish();
        }
        checkConnection();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (runfree){
                    runfree=!runfree;
                    connectivityReceiver = new ConnectivityReceiver(LoginPage.this);
                boolean isConnected = connectivityReceiver.isConnected();

                name = editText1.getText().toString();
                mobile = editText2.getText().toString();
                if (isConnected) {
                    if (name.matches("")) {
                        alertDialog = new AlertDialog.Builder(LoginPage.this).create();
                        alertDialog.setTitle("Warning");
                        alertDialog.setMessage("Please enter your Name. ");
                        alertDialog.show();
                    } else if (mobile.matches("")) {
                        alertDialog = new AlertDialog.Builder(LoginPage.this).create();
                        alertDialog.setTitle("Warning");
                        alertDialog.setMessage("Please enter your Mobile Number. ");
                        alertDialog.show();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        getlocation();
                    }

                } else {
                    message = "Sorry! Not connected to internet";
                    color = Color.RED;
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.button), message, Snackbar.LENGTH_LONG);

                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(color);
                    snackbar.show();
                }


            }
            }
        });
    }
    private void sendatatoserver(){
        System.out.println("name = " + name);
        System.out.println("mobile = " + mobile);
        System.out.println("adapter = " + String.valueOf(longi));
        System.out.println("adapter = " + String.valueOf(lati));
        JSONObject locationObject = new JSONObject();
        try{
        locationObject.put("lati", String.valueOf(lati));
        locationObject.put("longi", String.valueOf(longi));
        }catch (Exception e){
            System.out.println(e);
        }
        System.out.println("location = "+locationObject.toString());
        BackgroundWorker backgroundWorker = new BackgroundWorker(LoginPage.this);
        backgroundWorker.execute("insert",
                name,
                mobile,
                locationObject.toString());
        backgroundWorker.data(LoginPage.this, new BackgroundWorker.Datasend() {
            @Override
            public void Getdata(String result) {
                System.out.println("result = "+result );
                if(result!=null) {
                    JSONArray jsonArray = null;
                    try {
                        JSONObject jsonObject1 = new JSONObject(result);
                        status = jsonObject1.get("status").toString();
                        if (status == "1") {
                            JSONObject jsonObject2 = new JSONObject(jsonObject1.get("data").toString());
                            id = jsonObject2.get("id").toString();
                            nameid = jsonObject2.get("name").toString();
                            mobileid = jsonObject2.get("number").toString();
                            locationid = jsonObject2.get("location").toString();
                            JSONObject jsonObjectlocation = new JSONObject(locationid);
                            latitudeid = jsonObjectlocation.getString("lati");
                            logitudeid = jsonObjectlocation.getString("longi");
                            System.out.println("id = " + id);
                            System.out.println("nameid = " + nameid);
                            System.out.println("mobileid = " + mobileid);
                            System.out.println("latitudeid = " + latitudeid);
                            System.out.println("logitudeid = " + logitudeid);
                            getContacts();
                        } else {
                            runfree=!runfree;
                            System.out.println("Registration unsuccessful");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else{
                    runfree=!runfree;
                    System.out.println("result is null");
                    Toast.makeText(LoginPage.this,"Registration unsuccessful",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    void movetonext(String contacts){

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("id", id);
        editor.putBoolean("hasLoggedIn", true);
        editor.putString("name", nameid);
        editor.putString("pic", "null");
        editor.putString("mobile", mobileid);
        editor.putString("contacts", contacts);
        editor.putString("lati", latitudeid);
        editor.putString("longi", logitudeid);
        editor.commit();
        runfree=!runfree;
        progressBar.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(LoginPage.this, MainActivity.class);
        startActivity(intent);
        fa.setVisible(false);
        fa.finish();
    }
    private void getlocation() {
        GPS gps = new GPS(LoginPage.this);
        gps.location(new GPS.Update() {
            @Override
            public void Updatelocation(GoogleApiClient mGoogleApiClient, LocationRequest mLocationRequest) {
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
                                            LoginPage.this, 1000);
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
                if (ActivityCompat.checkSelfPermission(LoginPage.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoginPage.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(LoginPage.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoginPage.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_ACCESS_FINE_LOCATION);
                        } else {
                            permissionIsGranted = true;
                        }
                    }
                    return ;
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        lati = String.valueOf(location.getLatitude());
                        longi = String.valueOf(location.getLongitude());
                        if(stop){
                            sendatatoserver();
                            stop=false;
                        }
                    }
                });
            }
        });
        System.out.println("done ");

    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (this.checkSelfPermission( android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else{
            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
            // ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.READ_CONTACTS},
            //         REQUEST_READ_CONTACTS);
            //     ActivityCompat.requestPermissions(getActivity(),new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }
    private void getContacts() {
        start();
    }
    private void start() {
        if (!mayRequestContacts()) {
            return;
        }
        GetMyContacts getMyContacts=new GetMyContacts(LoginPage.this);
        getMyContacts.getContacts(new GetMyContacts.allmycontacts() {
            @Override
            public void actionMycontacts(String contacts) {
                System.out.println("new contacts"+contacts);
                movetonext(contacts);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                start();
            }
        }
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
        getlocation();
    }

    private void checkConnection() {
       connectivityReceiver=new ConnectivityReceiver(LoginPage.this);
        boolean isConnected = connectivityReceiver.isConnected();
        showSnack(isConnected);
    }
    private void showSnack(boolean isConnected) {
        String message="";
        int color;
        if (isConnected) {

        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.button), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
