package try_run.gagan.shah.location_proto;

import android.app.AlertDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ProgrammingKnowledge on 1/5/2016.
 */
public class BackgroundWorker extends AsyncTask<String,Void,String> {
    final String[] stringValue = new String[1];



    public interface Datasend{
        public void Getdata(String result);
    }
    public final class Constants {
        public static final int SUCCESS_RESULT = 0;
        public static final int FAILURE_RESULT = 1;
        public static final String PACKAGE_NAME =
                "com.google.android.gms.location.sample.locationaddress";
        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
        public static final String RESULT_DATA_KEY = PACKAGE_NAME +
                ".RESULT_DATA_KEY";
        public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
                ".LOCATION_DATA_EXTRA";
    }
    private Datasend datasend;
    Context context;
    String type;
    AlertDialog alertDialog;
    BackgroundWorker(Context ctx) {
        context = ctx;
    }
    void data(Context ctx,Datasend datasend) {
        context = ctx;
        this.datasend=datasend;
    }
    @Override
    protected String doInBackground(String... params) {
        type = params[0];
        String login_url = "http://frendsdom.com/omapp_api/ws/register";
        String dataout = "http://frendsdom.com/omapp_api/ws/listall";
        String query = "http://frendsdom.com/omapp_api/ws/update";
        String settinguri = "http://frendsdom.com/omapp_api/ws/updatePrivacy";

        if(type.equals("insert")) {
            try {
                String name = params[1];
                String number = params[2];
                String location = params[3];
                System.out.println(name+"  "+location+"  "+number);
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("no","UTF-8")+"="+URLEncoder.encode(number,"UTF-8")+"&"
                        +URLEncoder.encode("location","UTF-8")+"="+URLEncoder.encode(location,"UTF-8")+"&"
                        +URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{  if(type.equals("get")) {
            try {

                String uid = params[1];
                String allno = params[2];
                System.out.println("uid = "+uid);
                URL url = new URL(dataout);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("uid","UTF-8")+"="+URLEncoder.encode(uid,"UTF-8")+ "&"
                        + URLEncoder.encode("contacts", "UTF-8") + "=" + URLEncoder.encode(allno, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{  if(type.equals("setting")) {
            try {
                String blockid = null;
                String post_data = null;
                String uid = params[1];
                String privacy = params[2];
                if(params.length==3){
                    post_data = URLEncoder.encode("uid","UTF-8")+"="+URLEncoder.encode(uid,"UTF-8")+"&"
                            +URLEncoder.encode("privacy","UTF-8")+"="+URLEncoder.encode(privacy,"UTF-8");

                }else{
                    blockid = params[3];
                    post_data = URLEncoder.encode("uid","UTF-8")+"="+URLEncoder.encode(uid,"UTF-8")+"&"
                            +URLEncoder.encode("privacy","UTF-8")+"="+URLEncoder.encode(privacy,"UTF-8")+"&"
                            +URLEncoder.encode("blocked","UTF-8")+"="+URLEncoder.encode(blockid,"UTF-8");
                }
                URL url = new URL(settinguri);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else {
            if (type.equals("update")) {
                try {

                    String name = params[1];
                    String location = params[2];
                    String uid = params[3];
                    System.out.println("uid = " + uid);
                    URL url = new URL(query);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&"
                            + URLEncoder.encode("location", "UTF-8") + "=" + URLEncoder.encode(location, "UTF-8") + "&"
                            + URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(uid, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    System.out.println("result in update = " + result);
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }  else {
                if (type.equals("address")) {

                    String errorMessage = "";
                    StringBuilder result = new StringBuilder();
                    double latitude =Double.valueOf(params[1]) ;
                    double longitude = Double.valueOf(params[2]);
                    List<Address> addresses = null;
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(
                                latitude,
                                longitude,
                                // In this sample, get just a single address.
                                1);
                    } catch (IOException ioException) {
                        // Catch network or other I/O problems.

                        Log.e(" I/O problems. ", errorMessage, ioException);
                    } catch (IllegalArgumentException illegalArgumentException) {
                        // Catch invalid latitude or longitude values.
                        Log.e(" invalid latitude or longitude ", errorMessage + ". " +
                                "Latitude = " + latitude +
                                ", Longitude = " +
                                longitude, illegalArgumentException);
                    }

                    // Handle case where no address was found.
                    if (addresses == null || addresses.size()  == 0) {

                       return "No Address found";
                    } else {
                        Address address = addresses.get(0);
                        ArrayList<String> addressFragments = new ArrayList<String>();

                        // Fetch the address lines using getAddressLine,
                        // join them, and send them to the thread.
                        for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            addressFragments.add(address.getAddressLine(i));
                            result.append(address.getAddressLine(i)).append("\n");
                        }

                        return  result.toString();
                    }
//                    StringBuilder result = new StringBuilder();
//                    try {
//                        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//                        if (addresses.size() > 0) {
//                            Address address = addresses.get(0);
//                            result.append(address.getLocality()).append("\n");
//                            result.append(address.getCountryName());
//                        }
//                    } catch (IOException e) {
//                        Log.e("tag", e.getMessage());
//                    }
//
//
//                    return result.toString();
                }
            }
        }
        }
        }
        return null;
    }



    @Override
    protected void onPreExecute() {
//        alertDialog = new AlertDialog.Builder(context).create();
//        alertDialog.setTitle("Insert Status");
    }

    @Override
    protected void onPostExecute(String result) {
        if(type=="get"){
            datasend.Getdata(result);
        }
        else if(type=="insert"){
//            alertDialog.setMessage(result);
//            System.out.println("datasend = "+ datasend );System.out.println("result = "+ result );
            datasend.Getdata(result);

           // alertDialog.show();
        }
        else if(type=="update"){
            datasend.Getdata(result);
            // alertDialog.show();
        }else if(type=="address"){
            datasend.Getdata(result);
            // alertDialog.show();
        }else if(type=="setting"){
            datasend.Getdata(result);
            // alertDialog.show();
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}