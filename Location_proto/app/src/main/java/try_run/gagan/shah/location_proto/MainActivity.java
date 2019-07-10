package try_run.gagan.shah.location_proto;

import android.*;
import android.Manifest;
import android.animation.LayoutTransition;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static android.view.Gravity.END;

public class MainActivity extends AppCompatActivity implements My_Contacts.MyContactAction, My_profile.Update_my_profile, ConnectivityReceiver.ConnectivityReceiverListener {
    private ExpandableListView mDrawerList;
    private ExpListAdapter mAdapter;
    ArrayList<String> listData;
    private Common common;
    private String name, mobile, longi, lati, ids, pic;
    private testmap t;
    private View decorView;
    private String no = "";
    private String mycont;
    private ArrayList arlno = new ArrayList<String>();
    private static final int REQUEST_READ_CONTACTS = 0;
    private ConnectivityReceiver connectivityReceiver;
    public static boolean isConnected;
    private ContactusFragment contactusFragment;
    private My_profile my_profile;
    private Settings settingFrag;
    private aboutus aboutus;
    private My_Contacts my_contactsfrag;
    private Add_person add_person;
    FragmentManager fragmentManager;
    private Window window;
    HashMap<String, ArrayList> listDataChild;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private Toolbar toolbar;

    TextView textViewname, textViewmobile, textView;
    private CircleView imageView;
    private static String[] osArray = {"My Profile", "Settings", "About", "Contact Us"};
    public static final String PREFS_NAME = "MyPrefsFile";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Explode explode= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            explode = new Explode();
            getWindow().setExitTransition(explode);
            getWindow().setEnterTransition(explode);
        }
        decorView = this.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                            decorView.setSystemUiVisibility(uiOptions);
                        } else {
                            // TODO: The system bars are NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                        }
                    }
                });

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        mDrawerList = (ExpandableListView) findViewById(R.id.lvExp);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imageView = (CircleView) findViewById(R.id.circleViewm);

        textView = (TextView) findViewById(R.id.mainpictxt);
        textViewname = (TextView) findViewById(R.id.name);
        textViewmobile = (TextView) findViewById(R.id.mobile);
        ids = settings.getString("id", null);
        name = settings.getString("name", null);
        mobile = settings.getString("mobile", null);
        longi = settings.getString("longi", null);
        lati = settings.getString("lati", null);
        pic = settings.getString("pic", null);
        mycont = settings.getString("contacts", null);
        System.out.println(settings.getString("pic", null) + " pic value in main activity  ");
        if (settings.getString("pic", null).matches("null")) {
            Picasso.with(this)
                    .load(R.drawable.backon1)
                    .into(imageView);
            String nm = String.valueOf(name.charAt(0));
            textView.setText(nm);
        } else {

            Picasso.with(this)
                    .load(pic)
                    .into(imageView);
            textView.setText("");

        }
        textViewname.setText(settings.getString("name", null));
        textViewmobile.setText(mobile);
        System.out.println(settings.getString("id", null) + "  "
                + settings.getString("name", null) + "  "
                + settings.getString("mobile", null) + "  "
                + settings.getString("longi", null) + "  "
                + settings.getString("lati", null) + "  ");


        setSupportActionBar(toolbar);
        startScreen();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        createData();

        connectivityReceiver = new ConnectivityReceiver(MainActivity.this);
        isConnected = connectivityReceiver.isConnected();

        window = getWindow();
        mDrawerList.setGroupIndicator(null);
        mAdapter = new ExpListAdapter(this, listData, listDataChild);
        mDrawerList.setAdapter(mAdapter);
        common = new Common();
        my_profile = new My_profile();
        add_person = new Add_person();
        aboutus=new aboutus();
        settingFrag = new Settings();
        contactusFragment=new ContactusFragment();
        t = new testmap();
        my_contactsfrag = new My_Contacts();
        fragmentManager = getSupportFragmentManager();
        getContacts();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(fragmentManager.findFragmentById(R.id.fm) == my_profile)) {
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle args = new Bundle();
                    args.putString("name", settings.getString("name", null));
                    args.putString("id", ids);
                    args.putString("pic", settings.getString("pic", null));
                    args.putString("mobile", mobile);
                    args.putString("longi", settings.getString("longi", null));
                    args.putString("lati", settings.getString("lati", null));
                    my_profile.setArguments(args);
                    fragmentTransaction.replace(R.id.fm, my_profile);
                    fragmentTransaction.commit();
                }
                mDrawerLayout.closeDrawers();
            }
        });
        mDrawerList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            public void onGroupExpand(int groupPosition) {
                int len = mAdapter.getGroupCount();
                for (int i = 0; i < len; i++) {
                    if (i != groupPosition) {
                        mDrawerList.collapseGroup(i);
                    }
                }
            }
        });
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        mDrawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                switch (groupPosition) {
                    case 0: {
                        break;
                    }
                    case 1: {
                        break;
                    }
                    case 2: {
                        break;
                    }
                    case 3: {
                        break;
                    }
                    case 4: {
                        break;
                    }
                }
                return true;
            }
        });
        mDrawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                switch (groupPosition) {
                    case 0: {
                        mDrawerLayout.closeDrawers();
                        if (!(fragmentManager.findFragmentById(R.id.fm) == my_profile)) {
                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Bundle args = new Bundle();
                            args.putString("name", settings.getString("name", null));
                            args.putString("id", ids);
                            args.putString("pic", settings.getString("pic", null));
                            args.putString("mobile", mobile);
                            args.putString("longi", settings.getString("longi", null));
                            args.putString("lati", settings.getString("lati", null));
                            my_profile.setArguments(args);
                            fragmentTransaction.replace(R.id.fm, my_profile);
                            fragmentTransaction.commit();
                        }
                        break;
                    }
                    case 1: {
                        mDrawerLayout.closeDrawers();
                        if (!(fragmentManager.findFragmentById(R.id.fm) == settingFrag) && mycont != null) {
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Bundle args = new Bundle();
                            args.putString("name", name);
                            args.putString("id", ids);
                            args.putString("mobile", mobile);
                            args.putString("con", mycont);
                            args.putString("longi", longi);
                            args.putString("lati", lati);
                            settingFrag.setArguments(args);
                            fragmentTransaction.replace(R.id.fm, settingFrag);
                            fragmentTransaction.commit();
                        }
                        break;
                    }
                    case 2: {
                        mDrawerLayout.closeDrawers();
                        if (!(fragmentManager.findFragmentById(R.id.fm) == aboutus)) {
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fm, aboutus);
                            fragmentTransaction.commit();
                        }
                        break;
                    }
                    case 3: {
                        mDrawerLayout.closeDrawers();
                        if (!(fragmentManager.findFragmentById(R.id.fm) == contactusFragment)) {
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fm, contactusFragment);
                            fragmentTransaction.commit();
                        }
                        break;
                    }

                }
                return false;
            }
        });
//        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(listDataChild.get(listData.get(position)).get(position)==)
//                switch (){
//                    case 0:{FragmentManager fragmentManager=getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//                        Bundle args = new Bundle();
//                        Fragment fragment=new About();
//                        args.putString("name",osArray[0]);
//                        fragment.setArguments(args);
//                        fragmentTransaction.replace(R.id.fm,fragment);
//                        fragmentTransaction.commit();}
//
//                }
//
//
//            }
//        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitleTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitleTextView.setText("OMAPP");

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /**
             * Called when a drawer has settled in a completely open state.
             */

            public void onDrawerOpened(View drawerView) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                System.out.println(settings.getString("pic", null) + " pic value in main activity onDrawerOpen  ");
                textViewname.setText(settings.getString("name", null));
                if (settings.getString("pic", null).matches("null")) {
                    Picasso.with(MainActivity.this)
                            .load(R.drawable.backon1)
                            .resize(100, 100)
                            .into(imageView);
                    String nm = String.valueOf(settings.getString("name", null).charAt(0));
                    textView.setText(nm);
                } else {
                    pic = settings.getString("pic", null);
                    System.out.println("pic =" + pic);
                    textView.setText("");
                    Picasso.with(MainActivity.this)
                            .load(pic)
                            .into(imageView);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(getResources().getColor(R.color.blue));
                }
                invalidateOptionsMenu();
            }

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                //   getSupportActionBar().setTitle(mActivityTitle);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);

            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void startScreen() {

    }

    private void createData() {

        listData = new ArrayList<String>();
        listDataChild = new HashMap<String, ArrayList>();
        Collections.addAll(listData, osArray);
        ArrayList<String> myprofile = new ArrayList<String>();
        ArrayList<String> setting = new ArrayList<String>();
        ArrayList<String> help = new ArrayList<String>();
        ArrayList<String> contact = new ArrayList<String>();
        listDataChild.put(listData.get(0), myprofile);
        listDataChild.put(listData.get(1), setting);
        listDataChild.put(listData.get(2), help);
        listDataChild.put(listData.get(3), contact);

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Add) {
            if (!(fragmentManager.findFragmentById(R.id.fm) == t) && mycont != null) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("name", name);
                args.putString("id", ids);
                args.putString("allcon", settings.getString("contacts", null));
                args.putString("upic", settings.getString("pic", null));
                args.putString("mobile", mobile);
                //       args.putDouble("longi",Double.valueOf(longi));
                //       args.putDouble("lati",Double.valueOf(lati));
                t.setArguments(args);
                fragmentTransaction.replace(R.id.fm, t);
                fragmentTransaction.commit();
            } else {
                if (t.map != null && t.sydney != null) {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(t.sydney, 18);
                    t.map.animateCamera(cameraUpdate);
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void getlocation() {
        GPS gps = new GPS(this);
        gps.location(new GPS.Update() {
            @Override
            public void Updatelocation(GoogleApiClient mGoogleApiClient, LocationRequest mLocationRequest) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        String lati = String.valueOf(location.getLatitude());
                        String longi = String.valueOf(location.getLongitude());
                        LatLng sydney = new LatLng(Double.valueOf(lati), Double.valueOf(longi));

                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        mDrawerLayout.closeDrawers();
        if (!(fragmentManager.findFragmentById(R.id.fm) == t)) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle args = new Bundle();
            args.putString("name", settings.getString("name", null));
            args.putString("id", ids);
            args.putString("allcon", settings.getString("contacts", null));
            args.putString("upic", settings.getString("pic", null));
            args.putString("mobile", mobile);
            //       args.putDouble("longi",Double.valueOf(settings.getString("longi",null)));
            //       args.putDouble("lati",Double.valueOf(settings.getString("lati",null)));
            t.setArguments(args);
            fragmentTransaction.replace(R.id.fm, t);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void getposition(double longi, double lati) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("id", ids);
        args.putString("upic", pic);
        args.putString("allcon", mycont);
        args.putString("mobile", mobile);
        args.putDouble("longi", longi);
        args.putDouble("lati", lati);
        t.setArguments(args);
        fragmentTransaction.replace(R.id.fm, t);
        fragmentTransaction.commit();

    }

    @Override
    public void newdata() {
        if (!(fragmentManager.findFragmentById(R.id.fm) == t) && mycont != null) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle args = new Bundle();
            args.putString("name", name);
            args.putString("id", ids);
            args.putString("allcon", settings.getString("contacts", null));
            args.putString("upic", settings.getString("pic", null));
            args.putString("mobile", mobile);
            //       args.putDouble("longi",Double.valueOf(longi));
            //       args.putDouble("lati",Double.valueOf(lati));
            t.setArguments(args);
            fragmentTransaction.replace(R.id.fm, t);
            fragmentTransaction.commit();
        }

    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
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
        System.out.println("Main activity" + isConnected);
        showSnack(isConnected);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (this.checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
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
        addfragment(mycont);
        //    GetContacts getContacts=new GetContacts();
        Toast toast = Toast.makeText(this, "Loading Contacts", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
        //     getContacts.execute();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //   System.out.println("arlno = "+ arlno );

            JSONArray jsonArray1 = new JSONArray();
            for (int i = 0; i < arlno.size(); i++) {
                jsonArray1.put(arlno.get(i));
            }


            //    jsonArray1.put("9999999999");
            //    jsonArray1.put("7404124033");
            //     jsonArray1.put("7404424033");


            mycont = jsonArray1.toString();
            addfragment(jsonArray1.toString());
        }

        @Override
        protected Void doInBackground(Void... params) {
            ContentResolver cr = MainActivity.this.getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        pCur.moveToNext();
                        no = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
                        //     System.out.println("no r = "+ no );
                        String newno = "";
                        no = no.trim();
                        String[] noa = no.split("-");
                        for (int i = 0; i < noa.length; i++) {
                            newno = newno + noa[i];
                        }
                        no = newno;


                        newno = "";
                        String[] noaa = no.split(" ");
                        for (int i = 0; i < noaa.length; i++) {
                            newno = newno + noaa[i];
                        }
                        no = newno;


                        if (no.charAt(0) == '+') {
                            no = no.substring(1);
                        }

                        if (no.length() > 10) {
                            if (no.charAt(0) == '0') {
                                no = no.substring(1);
                            }
                        }


                        if (no.length() > 10) {
                            if (no.charAt(0) == '9') {
                                no = no.substring(1);
                            }
                        }

                        if (no.length() > 10) {
                            if (no.charAt(0) == '1') {
                                no = no.substring(1);
                            }
                        }

                        //   System.out.println("no n = "+ no );

                        arlno.add(no);
                        pCur.close();
                    }
                }
            }
            return null;
        }
    }

    private void addfragment(String s) {
        if (!(fragmentManager.findFragmentById(R.id.fm) == t)) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle args = new Bundle();
            args.putString("name", name);
            args.putString("id", ids);
            args.putString("allcon", settings.getString("contacts", null));
            args.putString("upic", settings.getString("pic", null));
            args.putString("mobile", mobile);
            //       args.putDouble("longi",Double.valueOf(settings.getString("longi",null)));
            //       args.putDouble("lati",Double.valueOf(settings.getString("lati",null)));
            t.setArguments(args);
            fragmentTransaction.replace(R.id.fm, t);
            fragmentTransaction.commit();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        BackgroundServies.timer=true;
    }

    @Override
    public void onPause() {
        super.onPause();
        BackgroundServies.timer=false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                start();
            }
        }
    }
}
