package try_run.gagan.shah.location_proto;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.nearby.messages.Distance;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import try_run.gagan.shah.location_proto.Helper.ConnectionHelper.SocketConnector;

import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.END;

public class testmap extends Fragment implements OnMapReadyCallback, BackgroundServies.Location {

//    public LocationReceiver location;
    @Override
    public void currentLoation(String lati, String longi) {
        System.out.println(longi+"  1  "+lati);
    }

    public interface Contacts{
        void mycontacts(String s);
    }

    public static Socket mSocket;
    {
        mSocket = SocketConnector.sharedInstance().getSocket();
    }

    private Contacts contacts;
    private Intent intent;
    private JSONArray jsonArrayid;
    private HashMap<Integer,Marker> hmap = new HashMap<Integer,Marker>();
    private ArrayList<Marker> my_marker=new ArrayList<Marker>();
    private ArrayList<MarkerOptions> my_markeropt=new ArrayList<MarkerOptions>();
    private ArrayList<String> my_id=new ArrayList<String>();
    private ArrayList<String> my_number=new ArrayList<String>();
    private ArrayList<String> my_name=new ArrayList<String>();
    private ArrayList<String> my_longi=new ArrayList<String>();
    private ArrayList<String> my_lati=new ArrayList<String>();
    private ArrayList<String> my_pic=new ArrayList<String>();
    private String allno;
    private boolean hash=false;
    private Bitmap bitmapf;
    private JSONArray jsonArray1;
    private ListView listView;
    private ImageView imageViewre;
    private ArrayList arlno = new ArrayList<String>();
    private HashMap<String,Integer> arllastseen = new HashMap<String,Integer>();
    private static final int REQUEST_READ_CONTACTS = 0;
    private String no="";
    private LinearLayout.LayoutParams lp,lp1;
    private CircleView cirpic,cirpicu;
    private Bitmap bitmapcu;
    private RelativeLayout linearLayout1;
    private LinearLayout linearLayout;
    private boolean scroll=true,ani=true;
    private CardView cardView;
    private ProgressBar progressBar,progressBarre;
    private View v;
    private DataListAdapter adapter;
    private SharedPreferences.Editor editor;
    private MapView mapView;
    private View ring;
    private Activity activity;
    private SwipeRefreshLayout swipeContainer;
    private String name,mobile,logitude,latitude,picurl;
    private String uname,umobile,ulogitude,ulatitude,uid,locationid,status,upic,idc,allcon;
     GoogleMap map;
    boolean fk=true,scroll1=true,on=true,dist=true;
     LatLng sydney;
    boolean marker_color_bool=true;
    MarkerOptions markero,markerOptions,markero1;
    Marker markers,m,marker1,marker;

    private  RelativeLayout relativeLayout1;
    String x;
    private  TextView textViewic,textViewicu;
    public static final String PREFS_NAME = "MyPrefsFile";
    View vmarker;
    View vumarker;
    LayoutInflater iconinflater;
    private CardView.LayoutParams lpc;
    private ImageView eye;
    private CircleView floatingActionButton;
    private SharedPreferences settings;
    private double logg, lt,nlong,nlati;
    ImageView imageView;
    private RelativeLayout relativeLayout;
    private static final int MY_ACCESS_FINE_LOCATION=101;
    private static final int MY_ACCESS_COARSE_LOCATION=102;
    private boolean permissionIsGranted=false,stop=false,tilt=true;
    private  ArrayList markerview = new ArrayList<View>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        my_longi.clear();
        my_lati.clear();
        my_number.clear();
        my_name.clear();
        my_id.clear();
        if(map!=null){ map.clear();}
        fk=true;
        activity=getActivity();
        mSocket.on("update", onNewMessage);
        mSocket.connect();

    }

    @Override
    public void onPause() {
        on=false;
        System.out.println("on pause ");
        super.onPause();
        stop=false;
        hash=false;
      //  BackgroundServies.timer=false;
    }

    @Override
    public void onStart() {
        super.onStart();
        stop=true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_test, container, false);
        iconinflater = inflater;
        imageView=(ImageView)v.findViewById(R.id.testi);
        mapView = (MapView) v.findViewById(R.id.mapview);
        imageViewre=(ImageView)v.findViewById(R.id.refrash);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        v.setSystemUiVisibility(uiOptions);
        listView=(ListView)v.findViewById(R.id.mylist);
        eye=(ImageView)v.findViewById(R.id.tilt);
        ring=(View)v.findViewById(R.id.ring);
        progressBar=(ProgressBar)v.findViewById(R.id.progressBar2);
        progressBarre=(ProgressBar)v.findViewById(R.id.progressBar6);
        relativeLayout1=(RelativeLayout)v. findViewById(R.id.myll);
        cardView=(CardView)v.findViewById(R.id.card);
        mapView.onCreate(savedInstanceState);
        floatingActionButton=(CircleView) v.findViewById(R.id.fab);
        Bundle args = getArguments();
        System.out.println("args = "+args);
        if(args!=null){
            umobile =args.getString("mobile");
            uname =args.getString("name");
            upic=args.getString("upic");
            ulogitude=args.getString("longi");
            ulatitude=args.getString("lati");
            uid=args.getString("id");
            allcon=args.getString("allcon");
            System.out.println("uidtmap = "+uid);
        }
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(map!=null){
                if(tilt){ CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(sydney)      // Sets the center of the map to Mountain View
                        .zoom(18)                   // Sets the zoom
                        // Sets the orientation of the camera to east
                        .tilt(90)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    tilt=false;
                }
                else{ CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(sydney)      // Sets the center of the map to Mountain View
                        .zoom(18)                   // Sets the zoom
                        // Sets the orientation of the camera to east
                        .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                tilt=true;
                }

            }}
        });


//        linearLayout=(LinearLayout)v.findViewById(R.id.myliner);
////        linearLayout1=(RelativeLayout)v.findViewById(R.id.myll);
        lp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp1 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        progressBar.invalidate();
        progressBar.setVisibility(View.VISIBLE);
        progressBarre.invalidate();
        progressBarre.setVisibility(View.INVISIBLE);
       // imageViewre.setVisibility(View.INVISIBLE);
//        imageViewre.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imageViewre.setVisibility(View.INVISIBLE);
//                progressBarre.setVisibility(View.VISIBLE);
//                lp.weight = 1;
//                linearLayout1.setLayoutParams(lp);
//                lp1.weight = (float) 1.55;
//                linearLayout.setLayoutParams(lp1);
//                scroll1=false;
//                scroll=true;
//                hash=false;
//                on=false;
//                map.clear();
//                arlno.clear();
//                my_id.clear();
//                my_name.clear();
//                my_number.clear();
//                my_lati.clear();
//                my_longi.clear();
//                my_pic.clear();
//                m=null;
//                dist=true;
//                if(adapter!=null){
//                adapter.notifyDataSetChanged();}
//                progressBar.invalidate();
//                progressBar.setVisibility(View.VISIBLE);
//                if(adapter!=null){
//                    GetMyContacts getMyContacts=new GetMyContacts(activity);
//                    getMyContacts.getContacts(new GetMyContacts.allmycontacts() {
//                        @Override
//                        public void actionMycontacts(String contacts) {
//                            allcon=contacts;
//                            SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
//                            SharedPreferences.Editor editor = settings.edit();
//                            editor.putString("contacts", contacts);
//                            editor.commit();
//                            System.out.println("new contacts"+contacts);
//                            addmarker(contacts);
//
//                        }
//                    });
//             }
//            }
//        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ani) {
                    LayoutTransition layoutTransition = cardView.getLayoutTransition();
                    layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
                    ring.setBackground(getResources().getDrawable(R.drawable.ring2));
                    lpc = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, 440, Gravity.CENTER|BOTTOM);
                    lpc.setMargins(20,0,20,170);
                    cardView.setLayoutParams(lpc);
                    cardView.setRadius(30);
                }else{
                    LayoutTransition layoutTransition=cardView.getLayoutTransition();
                    ring.setBackground(getResources().getDrawable(R.drawable.ring));
                    layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
                    lpc = new CardView.LayoutParams(10,10, Gravity.BOTTOM|END);
                    lpc.setMargins(0,0,62,62);
                    cardView.setLayoutParams(lpc);
                    cardView.setRadius(5);
                }
                ani=!ani;
//                ObjectAnimat
            }
        });
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(adapter!=null){
                    hash=false;
                    on=false;
                    map.clear();
                    arlno.clear();
                    my_id.clear();
                    my_name.clear();
                    my_number.clear();
                    my_lati.clear();
                    my_longi.clear();
                    my_pic.clear();
                    m=null;
                    dist=true;
                    swipeContainer.setRefreshing(false);
                    if(adapter!=null){
                        adapter.notifyDataSetChanged();}
                    progressBar.invalidate();
                    progressBar.setVisibility(View.VISIBLE);
                    if(adapter!=null){
                        GetMyContacts getMyContacts=new GetMyContacts(activity);
                        getMyContacts.getContacts(new GetMyContacts.allmycontacts() {
                            @Override
                            public void actionMycontacts(String contacts) {
                                allcon=contacts;
                                SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("contacts", contacts);
                                editor.commit();
                                System.out.println("new contacts"+contacts);
                                addmarker(contacts);

                            }
                        });
                    }

               }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        gotoloaction(Double.valueOf(my_longi.get(position)),Double.valueOf(my_lati.get(position)));
                        lpc = new CardView.LayoutParams(10,10, Gravity.BOTTOM|END);
                        lpc.setMargins(0,0,62,62);
                        ring.setBackground(getResources().getDrawable(R.drawable.ring));
                        cardView.setLayoutParams(lpc);
                        cardView.setRadius(5);
                        ani=!ani;
                    }
                });
//        linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(scroll1) {
//                    lp.weight = 1;
//                    linearLayout1.setLayoutParams(lp);
//                    lp1.weight= (float) 1.55;
//                    linearLayout.setLayoutParams(lp1);
//                    scroll1=false;
//                }else{
//                    lp.weight = 8;
//                    linearLayout1.setLayoutParams(lp);
//                    lp1.weight= 7;
//                    linearLayout.setLayoutParams(lp1);
//                    scroll1=true;
//                }
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        gotoloaction(Double.valueOf(my_longi.get(position)),Double.valueOf(my_lati.get(position)));
//                        lp.weight = 8;
//                        linearLayout1.setLayoutParams(lp);
//                        lp1.weight= 7;
//                        linearLayout.setLayoutParams(lp1);
//                        scroll=false;
//                    }
//                });
//
//                linearLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(scroll==true){
//                            lp.weight = 8;
//                            linearLayout1.setLayoutParams(lp);
//                            lp1.weight= 7;
//                            linearLayout.setLayoutParams(lp1);
//                            scroll=false;
//                        }else{
//
//                            lp.weight = 1;
//                            linearLayout1.setLayoutParams(lp);
//                            lp1.weight= (float) 1.55;
//                            linearLayout.setLayoutParams(lp1);
//                            scroll=true;
//                        }
//
//                    }
//                });
//
//            }
//        });
        mapView.getMapAsync(this);
        return v;
    }


    @Override
    public void onResume() {
        dist=true;
        on=true;
        hash=true;
        v.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                            v.setSystemUiVisibility(uiOptions);
                        } else {
                            // TODO: The system bars are NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                        }
                    }
                });
        mapView.onResume();
        View decorView = getActivity().getWindow().getDecorView();
        System.out.println("on resume ");

        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
//        mSocket.disconnect();
//        mSocket.off("update", onNewMessage);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        settings = getActivity().getSharedPreferences(PREFS_NAME, 0);


        map=googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.setPadding(1000,1000,1000,1000);


      //  map.setMyLocationEnabled(true);
        Bundle args = getArguments();
        System.out.println("on create "+args);
        if(args.getDouble("longi")!=0&&args.getDouble("lati")!=0){
            umobile =args.getString("mobile");
            uname =args.getString("name");
            upic=args.getString("upic");
            uid=args.getString("id");
            allcon=args.getString("allcon");
            System.out.println("run gotoloaction ");
            gotoloaction(args.getDouble("longi"), args.getDouble("lati"));
        }
        MapsInitializer.initialize(this.getActivity());
     //   getContacts();
        addmarker(allcon);
//        LatLng person1 = new LatLng( 30.718738,76.679678);
//        map.addMarker(new MarkerOptions()
//                .position(person1)
//                .title("person1")
//                .snippet("Population: 4,137,400")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.char4)));
//
//        LatLng person2 = new LatLng( 30.720032,76.689892);
//        map.addMarker(new MarkerOptions()
//                .position(person2)
//                .title("person2")
//                .snippet("Population: 4,137,400")
//
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.char4)));
//
//        LatLng person3 = new LatLng( 30.715384,76.692402);
//        markero1 = new MarkerOptions();
//        markero1.position(person3).title("person3")
//                .snippet("Population: 4,137,400")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.char4));
//        if(marker1!=null){
//            marker1.remove();
//        }
//        marker1= map.addMarker(markero1);
//        Runn runn1=new Runn();
//        runn1.marker_animation(marker1,markero1);
//        stop=true;
//
//        LatLng person4 = new LatLng( 30.708762,76.696179);
//        markero = new MarkerOptions();
//        markero.position(person4).title("person4")
//                .snippet("Population: 4,137,400")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.char4));
//        if(marker!=null){
//            marker.remove();
//        }
//        marker= map.addMarker(markero);
//        Runn runn=new Runn();
//        runn.marker_animation(marker,markero);
//        stop=true;
   //     lp.weight = 8;
   //     linearLayout1.setLayoutParams(lp);
   //     lp1.weight= 7;
  //      linearLayout.setLayoutParams(lp1);

    }

    private void addmarker(String s) {
        ConnectivityReceiver connectivityReceiver=new ConnectivityReceiver(activity);
        boolean isConnected = connectivityReceiver.isConnected();
        if (isConnected) {
            Toast toast = Toast.makeText(activity,"  Please wait   ",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();

            BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
            backgroundWorker.execute("get",uid,s);
            backgroundWorker.data(getActivity(), new BackgroundWorker.Datasend() {
                @Override
                public void Getdata(String result) {
                    System.out.println("result testmap = " + result);
                    if (result != null){
                        JSONArray jsonArray = null;
                        try {
                            JSONObject jsonObject1 = new JSONObject(result);
                            status = jsonObject1.get("status").toString();
                            System.out.println("status = " + status);
                            if (status.matches("1")) {
                                jsonArray = jsonObject1.getJSONArray("data");
                                System.out.println("jsonArray.length() = " + jsonArray.length());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    idc = jsonObject2.getString("id").toString();
                                    name = jsonObject2.getString("name").toString();
                                    mobile = jsonObject2.getString("number").toString();
                                    locationid = jsonObject2.get("location").toString();
                                    JSONObject jsonObjectlocation = new JSONObject(locationid);
                                    latitude = jsonObjectlocation.getString("lati").toString();
                                    logitude = jsonObjectlocation.getString("longi").toString();
                                    picurl = jsonObject2.get("pic_url").toString();
                                    System.out.println("name = " + name);
                                    System.out.println("mobile = " + mobile);
                                    System.out.println("latitude = " + latitude);
                                    System.out.println("logitude = " + logitude);
                                    System.out.println("picurl = " + picurl);
                                    nlong = Double.valueOf(logitude);
                                    nlati = Double.valueOf(latitude);
                                    my_id.add(idc);
                                    my_name.add(name);
                                    my_number.add(mobile);
                                    my_lati.add(latitude);
                                    my_longi.add(logitude);
                                    my_pic.add(picurl);
                                    arllastseen.put(name,0);
                                }
                                addPins();


                                System.out.println("my_name   = " + my_name);
//                                adapter=new DataListAdapter((MainActivity) getActivity(),my_name,my_number,my_pic);
//                                listView.setAdapter(adapter);
//                                adapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.INVISIBLE);
                                progressBarre.setVisibility(View.INVISIBLE);
//                                imageViewre.setVisibility(View.VISIBLE);
                                addPins();


                            } else if(status.matches("0")) {
                                System.out.println("No data found");
                                my_name.add("nulldata");
                                adapter=new DataListAdapter((MainActivity) getActivity(),my_name);
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.INVISIBLE);
                                progressBarre.setVisibility(View.INVISIBLE);
                                imageViewre.setVisibility(View.VISIBLE);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Run getlocation");
                        getlocation();
                        on=true;

                    }
                }
            });

        } else {
            String message = "Sorry! Not connected to internet";
            int color = Color.RED;
            Snackbar snackbar = Snackbar
                    .make(v.findViewById(R.id.circle), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
            progressBar.setVisibility(View.INVISIBLE);
            progressBarre.setVisibility(View.INVISIBLE);
            imageViewre.setVisibility(View.VISIBLE);
        }

    }

    private void addPins() {
        my_markeropt.clear();

        hash=false;
        if(map!=null){ map.clear();
        m=null;}
        for (int i = 0; i < my_name.size(); i++) {
            vmarker = iconinflater.inflate(R.layout.custom_marker_layout, null);
            cirpic = (CircleView) vmarker.findViewById(R.id.iconpic);
            textViewic = (TextView) vmarker.findViewById(R.id.textmar);
            if (my_pic.get(i)!="null"){
           //     System.out.println("picurl in add marker  = " + my_pic.get(i));
                Picasso.with(activity)
                        .load(my_pic.get(i))
                        .placeholder(R.drawable.default_avatar)
                        .resize(150,150)
                        .into(cirpic);
                textViewic.setText("");
            }else{
                String nm= String.valueOf(my_name.get(i).charAt(0));
           //     System.out.println("char in add marker  = " + nm);
                textViewic.setText(nm);
            }

            DisplayMetrics displayMetrics = new DisplayMetrics();
            (activity).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            vmarker.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            vmarker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
            vmarker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
            vmarker.buildDrawingCache();
            bitmapf = Bitmap.createBitmap(vmarker.getMeasuredWidth(), vmarker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmapf);
            vmarker.draw(canvas);

            LatLng person1 = new LatLng(Double.valueOf(my_lati.get(i)), Double.valueOf(my_longi.get(i)));
            my_markeropt.add(new MarkerOptions()
                    .position(person1)
                    .title(my_name.get(i))
                    .snippet(my_number.get(i))
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmapf)));
            hmap.put(i,map.addMarker(my_markeropt.get(i)));
            //my_marker.add();
        }
  //      System.out.println(hmap);
      //  map.clear();
        hash=true;

    }

    private void getlocation() {

        GPS gps = new GPS(getActivity());
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
                if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION},MY_ACCESS_FINE_LOCATION);
                    }else {
                        permissionIsGranted=true;
                    }
                    return ;
                }
          //      JSONObject finaljson=new JSONObject();
            //    JSONObject locationObject = new JSONObject();
                jsonArrayid=new JSONArray();
                for (int i=0; i < my_id.size(); i++) {
                    jsonArrayid.put(Integer.valueOf(my_id.get(i)));
                }
//                try{
//                    locationObject.put("lati",lati);
//                    locationObject.put("longi",longi);
//                    finaljson.put("uid",uid);
//                    finaljson.put("location",locationObject);
//                    finaljson.put("contacts",jsonArrayid);
//                }catch (Exception e){System.out.println(e);}
                System.out.println("id = "+jsonArrayid.toString());
      //          Toast.makeText(getActivity(),locationObject.toString(),Toast.LENGTH_SHORT).show();
                //BackgroundServies backgroundServies=new BackgroundServies(getActivity(),testmap.this);
                intent=new Intent(activity,BackgroundServies.class);
                intent.putExtra("id",uid);
                intent.putExtra("timer",true);
                intent.putExtra("jsonid",jsonArrayid.toString());
                activity.startService(intent);
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        if(on){
                        String lati = String.valueOf(location.getLatitude());
                        String longi = String.valueOf(location.getLongitude());
                        logg=Double.valueOf(longi);
                        lt=Double.valueOf(lati);
                        sydney = new LatLng( lt,logg);
                        System.out.println("dist = "+dist);
                        if(dist){
                        adapter=new DataListAdapter((MainActivity) activity,my_name,my_number,my_pic,my_longi,my_lati,arllastseen,logg,lt);
                        listView.setAdapter(adapter);
                            progressBar.setVisibility(View.INVISIBLE);
                            progressBarre.setVisibility(View.INVISIBLE);
//                            imageViewre.setVisibility(View.VISIBLE);
                        dist=false;
                        }
                  //       System.out.println("my lati  "+my_lati);
                  //       System.out.println("my longi  "+my_longi);
                        adapter.notifyDataSetChanged();

                        vumarker = iconinflater.inflate(R.layout.custom_marker_layout, null);
                        textViewicu = (TextView) vumarker.findViewById(R.id.textmar);
                        cirpicu = (CircleView) vumarker.findViewById(R.id.iconpic);
                      //      System.out.println( upic +" pic value in testmap  ");
                        if(upic.matches("null")){
                            String nm= String.valueOf(uname.charAt(0));
                            textViewicu.setText(nm);
                            }
                        else{
                            Picasso.with(getActivity())
                                    .load(upic)
                                    .placeholder(R.drawable.default_avatar)
                                    .resize(55, 55)
                                    .into(cirpicu);
                            textViewicu.setText("");

                        }

                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        (activity).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        vumarker.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        vumarker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
                        vumarker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
                        vumarker.buildDrawingCache();
                        bitmapcu = Bitmap.createBitmap(vumarker.getMeasuredWidth(), vumarker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmapcu);
                        vumarker.draw(canvas);
                      //  System.out.println("testmap");


                        if(fk){
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(sydney)      // Sets the center of the map to Mountain View
                                    .zoom(18)                   // Sets the zoom
                                                 // Sets the orientation of the camera to east
                                    .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                                    .build();                   // Creates a CameraPosition from the builder
                            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(sydney, 18);
//                            map.animateCamera(cameraUpdate);
                            fk=false;
                        }

                        markerOptions=new MarkerOptions()
                                .position(sydney)
                                .title("Your Current position")
                                .snippet(lati+longi)
                                .icon(BitmapDescriptorFactory.fromBitmap(bitmapcu));

                            if(m!=null){
                            //    System.out.println("set marker");
                                m.setPosition(sydney);
                                try {
                                    m.setIcon(BitmapDescriptorFactory.fromBitmap(bitmapcu));
                                }catch(Exception e){
                                    System.out.println("e = "+e);
                                }
                            }else{
                          //      System.out.println("add marker");
                                addPins();
                                m=map.addMarker(markerOptions);
                            }
                    }}
                });
            }
        });


    }

    public void gotoloaction(double longi, double lati) {
        System.out.println("gotoloaction"+lati+" "+longi);
        LatLng sydney = new LatLng( lati,longi);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(sydney, 18);
        map.animateCamera(cameraUpdate);
    }
    public class Runn{
        Handler handler = new Handler();
        boolean marker_color_bool=true;
        private void marker_animation(final Marker marker, final MarkerOptions markero) {
        markers = marker;
        handler.postDelayed(new Runnable() {
            public void run() {
                if(stop) {
                    if (marker_color_bool == true) {
                        if (markers != null) {
                            markers.remove();
                        }
                        markero.icon(BitmapDescriptorFactory.fromResource(R.drawable.pic2));
                        markers = map.addMarker(markero);
                        marker_color_bool = false;
                    } else {
                        if (markers != null) {
                            markers.remove();
                        }
                        markero.icon(BitmapDescriptorFactory.fromResource(R.drawable.pic6));
                        markers = map.addMarker(markero);
                        marker_color_bool = true;
                    }

                    handler.postDelayed(this, 500);
                }
            }}, 100);
    }
    }
    public class ThreadGetMoreData extends Thread {
        @Override
        public void run() {
            //Delay time
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
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
        getlocation();
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {


            final String data=(String)args[0];

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(hash){ processCall(data);    }

                }
            });
        }
    };

    private void processCall(String args){
    //    System.out.println("listener work");
        JSONArray jsonArray=null;
        String id = null,last_seen;
        String current_location,longi = null,lati=null;
   //     System.out.println("listener data = "+ args);

        try {
            jsonArray= new JSONArray((String) args);
            for(int i=0;i< jsonArray.length();i++){
                JSONObject data = jsonArray.getJSONObject(i);
        //        System.out.println(data.toString());
                id = data.getString("id");
                current_location = data.getString("current_location");
                last_seen= data.getString("last_seen");
         //       System.out.println("current_location"+current_location);
                if(!current_location.matches("")) {
                    JSONObject jsonObject = new JSONObject(current_location);
                    lati = jsonObject.getString("lati");
                    longi = jsonObject.getString("longi");
                }
       //         System.out.println("user data  "+id+"   "+lati+"  "+longi+"   "+last_seen);
                // add the message to view


                addMessage(id,lati, longi,last_seen);
            }
        } catch (JSONException e) {
            System.out.println(e);
            return;
        }

    }

    private void addMessage(final String id, final String lati, final String longi,String last_seen) {
            if(lati!=null&&longi!=null) {
         //       System.out.println(my_id);
                int i = my_id.indexOf(id);
                if(my_id.size()>0){
       //         System.out.println("index = " + i);
                my_lati.set(i, lati);
                my_longi.set(i, longi);
                adapter.notifyDataSetChanged();
                if(last_seen.equals("0000-00-00 00:00:00")){
                    arllastseen.put(my_name.get(i).toString(),0);
                    adapter.notifyDataSetChanged();
                }else{
                    arllastseen.put(my_name.get(i).toString(),1);
                    adapter.notifyDataSetChanged();
                }
     //           System.out.println(hmap.get(i));
                LatLng person1 = new LatLng(Double.valueOf(lati), Double.valueOf(longi));
                hmap.get(i).setPosition(person1);}
        }
    }

    private class LocationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(!intent.getAction().equals("com.cbb")){
                return;
            }


        }
    }




}
