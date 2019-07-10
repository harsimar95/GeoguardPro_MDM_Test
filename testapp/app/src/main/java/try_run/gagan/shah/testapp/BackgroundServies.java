package try_run.gagan.shah.testapp;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;

/**
 * Created by sumanthanda on 10/27/16.
 */
public class BackgroundServies extends IntentService  {

    private static final int MY_ACCESS_FINE_LOCATION = 101;
    private static final int MY_ACCESS_COARSE_LOCATION = 102;
    private Activity activity;
    private boolean permissionIsGranted = false;
    public BackgroundServies() {

        super("Service");
    }

    public BackgroundServies(Main5Activity main5Activity) {
        super("Service");
        this.activity = main5Activity;

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("Background servies Start");
        System.out.println(activity);
        GpsHandler gpsHandler=new GpsHandler(this);
        gpsHandler.GPSlocation(new GpsHandler.Update() {
            @Override
            public void Updatelocation(String longi, String lati) {
                System.out.println(longi+"    "+lati);
            }
        });

    }

}
