package try_run.gagan.shah.location_proto;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by sumanthanda on 8/29/16.
 */
public class GPS implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {
    Update update;
    public interface Update {
        public void Updatelocation(GoogleApiClient mGoogleApiClient, LocationRequest mLocationRequest);
    }

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private Context mContext;

    public GPS(Context mContext) {
        this.mContext = mContext;

    }
    public void location(Update updatelocation){
        this.update=updatelocation;
        if (mGoogleApiClient == null) {
            System.out.println("mContext = "+mContext);
            if(mContext!=null){
                mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }
        }
        mGoogleApiClient.connect();
        System.out.println("connection status = "+mGoogleApiClient.isConnected());
    }
    protected void onStart() {
        System.out.println("==================connect=====================");
        mGoogleApiClient.connect();
      //  super.onStart();
    }

    protected void onStop() {
        System.out.println("==================disconnect===================");
        mGoogleApiClient.disconnect();
     //   super.onStop();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        update.Updatelocation(mGoogleApiClient,mLocationRequest);
        System.out.println("onConnected");
        System.out.println("connection status = "+mGoogleApiClient.isConnected());
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("onConnectionFailed");
    }


}
