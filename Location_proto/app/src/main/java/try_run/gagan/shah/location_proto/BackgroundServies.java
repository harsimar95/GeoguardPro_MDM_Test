package try_run.gagan.shah.location_proto;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;

import try_run.gagan.shah.location_proto.Helper.ConnectionHelper.SocketConnector;

/**
 * Created by sumanthanda on 10/27/16.
 */
public class BackgroundServies extends IntentService {

    private Activity activity;
    private String result,uid,time;

    public static boolean timer;
    private JSONArray json;
   private Socket mSocket;
    {
        mSocket = SocketConnector.sharedInstance().getSocket();
    }

    public BackgroundServies() {
        super("Service");
    }
    public interface Location{
        public void currentLoation(String lati,String longi);
    }
    Location location;

    public BackgroundServies(Activity activity,Location location) {
        super("Service");
        this.activity = activity;
        this.location=location;

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle=intent.getExtras();
        timer=bundle.getBoolean("timer");
        uid=bundle.getString("id");
        try {
            json=new JSONArray(bundle.getString("jsonid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
  //      System.out.println("Background servies Start");
        GpsHandler gpsHandler=new GpsHandler(this);
        gpsHandler.GPSlocation(new GpsHandler.Update() {
            @Override
            public void Updatelocation(String longi, String lati) {
                if(timer){
                Date date=new Date();
                time= String.valueOf(new Timestamp(date.getTime()));
           //     System.out.println(time);
                }else{
                    time="0";
                }

                broadcastLocation(lati, longi);
        //        System.out.println(longi+"    "+lati);
                JSONObject locationObject = new JSONObject();
                JSONObject jsonObject=new JSONObject();
                try{
                    locationObject.put("lati",lati);
                    locationObject.put("longi",longi);
                    jsonObject.put("uid",uid);
                    jsonObject.put("time",time);
                    jsonObject.put("location",locationObject);
                    jsonObject.put("contacts",json);
                }catch (Exception e){System.out.println(e);}
                System.out.println("jsonobject = "+jsonObject);
                mSocket.emit("send", jsonObject.toString());

            }
        });



    }

    private void broadcastLocation(String latitude, String longitude){
        Intent localIntent =
                new Intent("com.cbb")
                        // Puts the status into the Intent
                        .putExtra("latitude", latitude)
                        .putExtra("longitude", longitude);
        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

    }
}
