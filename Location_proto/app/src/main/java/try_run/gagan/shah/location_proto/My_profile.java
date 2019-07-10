package try_run.gagan.shah.location_proto;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public class My_profile extends Fragment {
    private static final int REQUEST_CROP_ICON = 2;
    private static final Object RESULT_OK = 3;

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
                    .make(v.findViewById(R.id.btsave), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }
    private void checkConnection() {
        connectivityReceiver=new ConnectivityReceiver(activity);
        boolean isConnected = connectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    public interface Update_my_profile {
        void newdata();
    }

    interface Service {
        @Multipart
        @POST("updateProfilePic")
        Call<ResponseBody> upload(@Part("uid") RequestBody name,
                                  @Part MultipartBody.Part file);
    }

    Service service;
    Update_my_profile update_my_profile;
    String updatepic = "http://frendsdom.com/omapp_api/ws/";
    private TextView textViewmobile, textViewlocation;
    private EditText editTextname;
    private Marker m;
    private JSONObject locationObject;
    private String picurl;
    private Uri mImageCaptureUri;
    private ConnectivityReceiver connectivityReceiver;
    private LinearLayout.LayoutParams lp;
    private LatLng newlocaion;
    private boolean isConnected;
    GoogleMap Map;
    private ImageView mImageView;
    private  View v;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    private final static int RESULT_SELECT_IMAGE = 100;
    private Button button, buttonb;
    private  RelativeLayout linearLayout1;
    private TextView textViewname, textViewaddress;
    private ProgressBar progressBar, progressBar1;
    public static final String PREFS_NAME = "MyPrefsFile";
    private CircleView circleImageView;
    private static int RESULT_LOAD_IMG = 1;
    private String uname, umobile, locationid, latitudeid, logitudeid, status;
    private static int IMG_RESULT = 1;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    String ImageDecode, longi, lati;
    String id;
    private TextView textViewic;
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_my_profile, container, false);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar4);
        progressBar1 = (ProgressBar) v.findViewById(R.id.progressBar5);
        linearLayout1=(RelativeLayout)v.findViewById(R.id.addresslayout);
        progressBar.invalidate();
        progressBar.setVisibility(View.INVISIBLE);
        progressBar1.invalidate();
        progressBar1.setVisibility(View.INVISIBLE);
        textViewmobile = (TextView) v.findViewById(R.id.tvmobile);
        editTextname = (EditText) v.findViewById(R.id.etname);
        button = (Button) v.findViewById(R.id.btsave);
        textViewlocation = (TextView) v.findViewById(R.id.etlocation);
        textViewname = (TextView) v.findViewById(R.id.namemy);
        textViewaddress = (TextView) v.findViewById(R.id.address);
        circleImageView = (CircleView) v.findViewById(R.id.circleView);
        textViewic = (TextView) v.findViewById(R.id.textic);
        activity=getActivity();
        connectivityReceiver=new ConnectivityReceiver(activity);
        checkConnection();
        isConnected = connectivityReceiver.isConnected();
        textViewlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     final Dialog dialog = new Dialog(getActivity());


                //  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
// Add the buttons
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int ids) {
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.dialog , null);
                builder.setView(view);
                builder.setMessage("Touch to reset location")
                        .setTitle("Change your location");
                AlertDialog dialog = builder.create();
                dialog.show();

                MapView mMapView;
                mMapView = (MapView) view.findViewById(R.id.mapi);
                mMapView.onCreate(dialog.onSaveInstanceState());
                mMapView.onResume();// needed to get the map to display immediately
                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        Map = googleMap;
                        Map.getUiSettings().setMyLocationButtonEnabled(false);
                        Map.getUiSettings().setMapToolbarEnabled(false);
                        MapsInitializer.initialize(getActivity());
                        if(latitudeid.equals(null)||logitudeid.equals(null)) {
                        }else{
                            double logg = Double.valueOf(logitudeid);
                            double lt = Double.valueOf(latitudeid);
                            LatLng sydney = new LatLng(lt, logg);
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(sydney)
                                    .title("Home location")
                                    .snippet(lati + "," + longi);
                            m = Map.addMarker(markerOptions);
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(sydney, 10);
                            Map.animateCamera(cameraUpdate);


                            Map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
                                    System.out.println("location" + latLng.latitude + "  " + latLng.longitude);
                                    m.remove();
                                    lati = String.valueOf(latLng.latitude);
                                    longi = String.valueOf(latLng.longitude);
                                    double logg = Double.valueOf(longi);
                                    double lt = Double.valueOf(lati);
                                    newlocaion = new LatLng(lt, logg);
                                    MarkerOptions markerOptions = new MarkerOptions()
                                            .position(newlocaion)
                                            .title("Your Current position")
                                            .snippet(lati + longi);
                                    m = Map.addMarker(markerOptions);
                                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(newlocaion, 10);
                                    Map.animateCamera(cameraUpdate);


                                }
                            });

                        }
                    }
                });
            }
        });
        textViewlocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                   //got the focus
                   //  final Dialog dialog = new Dialog(getActivity());

                 //   final Dialog dialog = new Dialog(getActivity(),R.style.AlertDialogCustom);
                    //  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
// Add the buttons
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                  //  LayoutInflater inflater = getActivity().getLayoutInflater();
                    View view = inflater.inflate(R.layout.mapdialog , null);
                 //  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //    dialog.setContentView(R.layout.dialog);


                 //   TextView ok=(TextView) view.findViewById(R.id.ok);
                 //   TextView cencel=(TextView)view.findViewById(R.id.cencel);
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    builder.setView(view);
                    builder.setMessage("Touch to reset location").setTitle("Change your location");
                  final AlertDialog dialog = builder.create();
                    dialog.show();
//                    ok.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.cancel();
//                        }
//                    });
//                    cencel.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.cancel();
//                        }
//                    });
                    MapView mMapView;
                    mMapView = (MapView) view.findViewById(R.id.mapi);
                    mMapView.onCreate(dialog.onSaveInstanceState());
                    mMapView.onResume();// needed to get the map to display immediately
                    mMapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            Map = googleMap;
                            Map.getUiSettings().setMyLocationButtonEnabled(false);
                            Map.getUiSettings().setMapToolbarEnabled(false);
                            MapsInitializer.initialize(getActivity());

                            if(latitudeid.equals(null)||logitudeid.equals(null)){
                            }else{
                                double logg=Double.valueOf(logitudeid);
                                double lt=Double.valueOf(latitudeid);
                                LatLng sydney = new LatLng( lt,logg);
                                MarkerOptions markerOptions=new MarkerOptions()
                                        .position(sydney)
                                        .title("Home location")
                                        .snippet(lati+","+longi)
                                        ;
                                m=Map.addMarker(markerOptions);
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(sydney, 14);
                                Map.animateCamera(cameraUpdate);

                                Map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(LatLng latLng) {
                                        System.out.println("location"+latLng.latitude+"  "+latLng.longitude);
                                        m.remove();
                                        lati=String.valueOf(latLng.latitude);
                                        longi=String.valueOf(latLng.longitude);
                                        double logg=Double.valueOf(longi);
                                        double lt=Double.valueOf(lati);
                                        newlocaion = new LatLng( lt,logg);
                                        MarkerOptions markerOptions=new MarkerOptions()
                                                .position(newlocaion)
                                                .title("Your Current position")
                                                .snippet(lati+longi)
                                                ;
                                        m=Map.addMarker(markerOptions);
                                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(newlocaion, 14);
                                        Map.animateCamera(cameraUpdate);


                                    }
                                });

                            }

                        }
                    });
                }
                else {
                   //focus lost
                }
            }
        });

        Bundle args = getArguments();
        if (args != null) {
            textViewmobile.setText("Phone :  " + args.getString("mobile"));
            editTextname.setText(args.getString("name"));
            textViewname.setText(args.getString("name"));
            latitudeid=args.getString("lati");
            logitudeid=args.getString("longi");

            System.out.println(args.getString("pic")+" pic value in my profile  ");
            String pic = args.getString("pic", "");

            if (pic.equals("null")){
                System.out.println("null pic");
                Picasso.with(getActivity())
                        .load(R.drawable.backon1)
                        .into(circleImageView);
                String n = args.getString("name");
                String nm = String.valueOf(n.charAt(0));
                textViewic.setText(nm);
            } else {
                System.out.println("pic avalible");
                Picasso.with(getActivity())
                        .load(args.getString("pic"))
                        .into(circleImageView);
                textViewic.setText("");
            }

            id = args.getString("id");

            getaddress();

        }
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               start();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isConnected = connectivityReceiver.isConnected();
                if (isConnected) {
                    progressBar.setVisibility(View.VISIBLE);
                    String name=editTextname.getText().toString();
                    locationObject = new JSONObject();
                    try{
                        locationObject.put("lati",lati);
                        locationObject.put("longi",longi);
                    }catch (Exception e){
                        System.out.println(e);
                    }
                    System.out.println("locationObject.toString() = "+locationObject.toString() );
                    BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
                    backgroundWorker.execute("update",
                            name,locationObject.toString(),id);
                    backgroundWorker.data(getActivity(), new BackgroundWorker.Datasend() {
                        @Override
                        public void Getdata(String result) {
                            System.out.println("result = "+result );
                            if(result!=null){
                            JSONArray jsonArray= null;
                            try {
                                JSONObject jsonObject1= new JSONObject(result);
                                status=jsonObject1.get("status").toString();
                                if(status=="1"){
                                    JSONObject jsonObject2=new JSONObject(jsonObject1.get("data").toString());
                                    id= jsonObject2.get("id").toString();
                                    uname=jsonObject2.get("name").toString();
                                    umobile=jsonObject2.get("number").toString();
                                    locationid=jsonObject2.get("location").toString();
                                    JSONObject jsonObjectlocation= new JSONObject(locationid);
                                    latitudeid=jsonObjectlocation.getString("lati");
                                    logitudeid=jsonObjectlocation.getString("longi");
                                    System.out.println("uid = "+id);
                                    System.out.println("unameid = "+uname);
                                    System.out.println("umobileid = "+umobile);
                                    System.out.println("ulatitudeid = "+latitudeid);
                                    System.out.println("ulogitudeid = "+logitudeid);
                                    picurl=jsonObject2.get("pic_url").toString();
                                    movetonext();
                                }else
                                {
                                    System.out.println(jsonObject1.get("message").toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                                System.out.println("result is null");
                                Toast.makeText(getActivity(),"Profile not updated",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);

                            }
                        }
                    });

                } else {
                   String message = "Sorry! Not connected to internet";
                   int color = Color.RED;
                    Snackbar snackbar = Snackbar
                            .make(v.findViewById(R.id.btsave), message, Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(color);
                    snackbar.show();
                }
            }
        });
        return v;
    }

    private void getaddress() {

            progressBar1.setVisibility(View.VISIBLE);
            lati=String.valueOf(latitudeid);
            longi=String.valueOf(logitudeid);

        BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
        backgroundWorker.execute("address",lati,longi);

        backgroundWorker.data(getActivity(), new BackgroundWorker.Datasend() {
            @Override
            public void Getdata(String result) {
                RelativeLayout linearLayout1=(RelativeLayout)v.findViewById(R.id.addresslayout);
                lp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                String address="";
                String[] sli=result.split("\n");

                if(sli.length==1){
                    address=address+sli[0];
                }else{
                    for(int i=0;i<sli.length;i++){
                        System.out.println(sli[i]);
                        address=address+sli[i]+",";
                    }}

                System.out.println("addre lines "+sli.length);
                if(sli.length>2){
                    System.out.println(">2 ");
                    lp.weight = 1.2f;
                    textViewlocation.setLines(4);
                    linearLayout1.setLayoutParams(lp);
                }else if (sli.length>1){
                    System.out.println(">1 ");
                    lp.weight = 1.5f;
                    textViewlocation.setLines(2);
                    linearLayout1.setLayoutParams(lp);
                }else if (sli.length>0){
                    System.out.println(">0 ");
                    lp.weight = 1.8f;
                    textViewlocation.setLines(1);
                    linearLayout1.setLayoutParams(lp);
                }else {
                    lp.weight = 1.8f;
                    linearLayout1.setLayoutParams(lp);
                }

                showSnack(isConnected);
                textViewaddress.setText(address.trim());
                textViewlocation.setText(result.toString().trim());
                progressBar1.setVisibility(View.INVISIBLE);
            }
        });


    }

    void movetonext(){
        if(picurl.matches("null")){
            String nm = String.valueOf(uname.charAt(0));
            textViewic.setText(nm);
        }
        else{ textViewic.setText("");
            Picasso.with(getActivity())
                    .load(picurl)
                    .placeholder(R.drawable.default_avatar)
                    .resize(100, 100)
                    .into(circleImageView);}
        textViewname.setText(uname);
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("id", id);
        editor.putString("name", uname);
        editor.putString("mobile", umobile);
        editor.putString("lati", latitudeid);
        editor.putString("longi", logitudeid);
        editor.putString("pic",picurl);
        editor.commit();
        progressBar.setVisibility(View.INVISIBLE);
        getaddress();
        Toast.makeText(getActivity(),"Profile Updated",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_FROM_CAMERA:
                doCrop();

                break;

            case PICK_FROM_FILE:
                mImageCaptureUri = data.getData();


                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP||Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP+1) {
                    Uri selectedImage = data.getData();

                    String[] filePathColumn = {MediaStore.Images.Media.DATA };
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    performCrop(picturePath);
                }else{
                    doCrop();
                }
                break;

            case CROP_FROM_CAMERA:

                Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");

                    File dir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    Bitmap b= extras.getParcelable("data");
                    Bitmap out = Bitmap.createScaledBitmap(b, 200, 200, false);
                    File file = new File(dir, "resize.jpeg");
                    FileOutputStream fOut;
                    try {
                        fOut = new FileOutputStream(file);
                        out.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                        b.recycle();
                        out.recycle();
                    } catch (Exception e) {}

                    System.out.println("now upload ");
                    final String[] result = new String[1];
                    final JSONObject[] jsonObjectnew = {new JSONObject()};
                    String image = file.getPath();
                    String uid = id;
                    System.out.println("image = "+image);
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
                    client.retryOnConnectionFailure();
                    service = new Retrofit.Builder().baseUrl(updatepic).client(client.newBuilder().connectTimeout(5, TimeUnit.MINUTES).build()).build().create(Service.class);
                    File file1 = new File(image);
                    RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("user_pic", file1.getName(), reqFile);
                    RequestBody name = RequestBody.create(MediaType.parse("text/plain"), uid);

                    retrofit2.Call<okhttp3.ResponseBody> req = service.upload(name,body);
                    req.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {

                                result[0] =response.body().string();
                                JSONObject jsonObject=new JSONObject(result[0]);
                                System.out.println("resghffghjult = "+ jsonObject.toString() );

                                System.out.println("result pu = "+jsonObject.toString() );
                                try {
                                    JSONObject jsonObject1= new JSONObject(jsonObject.toString());
                                    status=jsonObject1.get("status").toString();
                                    if(status=="1"){
                                        JSONObject jsonObject2=new JSONObject(jsonObject1.get("data").toString());
                                        id= jsonObject2.get("id").toString();
                                        uname=jsonObject2.get("name").toString();
                                        umobile=jsonObject2.get("number").toString();
                                        locationid=jsonObject2.get("location").toString();
                                        JSONObject jsonObjectlocation= new JSONObject(locationid);
                                        latitudeid=jsonObjectlocation.getString("lati");
                                        logitudeid=jsonObjectlocation.getString("longi");
                                        System.out.println("uid = "+id);
                                        System.out.println("unameid = "+uname);
                                        System.out.println("umobileid = "+umobile);
                                        System.out.println("ulatitudeid = "+latitudeid);
                                        System.out.println("ulogitudeid = "+logitudeid);
                                        picurl=jsonObject2.get("pic_url").toString();
                                        movetonext();
                                    }else
                                    {
                                        System.out.println(jsonObject1.get("message").toString());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            } catch (Exception e) {
                                Log.d("errorvalue","is=>"+e);
                            }

                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                            System.out.println("t = "+t);
                            showSnack(isConnected);
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    });

                }

                File f = new File(mImageCaptureUri.getPath());

                if (f.exists()) f.delete();

                break;

        }


//        try {
//            if (requestCode == IMG_RESULT
//                    && null != data) {
//
//
//
////                try {
////                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), URI);
////
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
//            //    circleImageView.setImageBitmap(bitmap);
////                ByteArrayOutputStream baos = new ByteArrayOutputStream();
////                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
////                byte[] imageBytes = baos.toByteArray();
////                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//
//
//
//            }
//        } catch (Exception e) {
//            Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG)
//                    .show();
//        }

    }

    private void performCrop(String picUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_FROM_CAMERA);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void start() {
        if (!mayRequestContacts()) {
            return;
        }
        changeimage();

    }

    private void changeimage() {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//        // Start the Intent
//        startActivityForResult(galleryIntent, IMG_RESULT);
        final String [] items			= new String [] {"Take from camera", "Select from gallery"};
        ArrayAdapter<String> adapter	= new ArrayAdapter<String> (getActivity(), android.R.layout.select_dialog_item,items);
        AlertDialog.Builder builder		= new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Image");
        builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int item ) { //pick from camera
                if (item == 0) {
                    Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                            "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                    try {
                        intent.putExtra("return-data", true);

                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else { //pick from file
                    System.out.println("SDK = "+Build.VERSION.SDK_INT+"  "+Build.VERSION_CODES.LOLLIPOP+"  "+Build.VERSION_CODES.M+"  "+Build.VERSION_CODES.KITKAT);
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP||Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP+1) {
                        System.out.println("Lollipop devies detact ");
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, PICK_FROM_FILE);
                    }else{
                        System.out.println("Marshmallo devies detact ");
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                    }

//                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(i, RESULT_SELECT_IMAGE);
                }
            }
        } );
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (getActivity().checkSelfPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else{
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
            // ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.READ_CONTACTS},
            //         REQUEST_READ_CONTACTS);
            //     ActivityCompat.requestPermissions(getActivity(),new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                start();
            }
        }
    }
    private void doCrop() {
        progressBar.setVisibility(View.VISIBLE);
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities( intent, 0 );

        int size = list.size();

        if (size == 0) {
            Toast.makeText(getActivity(), "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {
            intent.setData(mImageCaptureUri);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            if (size == 1) {
                Intent i= new Intent(intent);
                ResolveInfo res	= list.get(0);

                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title 	= getActivity().getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon		= getActivity().getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent= new Intent(intent);

                    co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(getContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
                builder.setTitle("Choose Crop App");
                builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int item ) {
                        startActivityForResult( cropOptions.get(item).appIntent, CROP_FROM_CAMERA);

                    }
                });

                builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel( DialogInterface dialog ) {

                        if (mImageCaptureUri != null ) {
                            getActivity().getContentResolver().delete(mImageCaptureUri, null, null );
                            mImageCaptureUri = null;
                        }
                    }
                } );

                AlertDialog alert = builder.create();

                alert.show();
            }
        }
    }

}
