package try_run.gagan.shah.location_proto;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.util.ArrayList;


public class Add_person extends Fragment {
    ListView listView;
    double longi=30.704746;
    double lati=76.689222;

    private ProgressBar progressBar;
    private DataListAdapter1 adapter;
    ArrayList arlname = new ArrayList<String>();
    ArrayList arlno = new ArrayList<String>();
    private static final int REQUEST_READ_CONTACTS = 0;
    String no="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_add_person, container, false);
        listView=(ListView)v.findViewById(R.id.list);
        progressBar=(ProgressBar)v.findViewById(R.id.progressBar);
        progressBar.invalidate();
       // progressBar.;
        arlname.clear();
        arlno.clear();
        start();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject locationObject = new JSONObject();
                try{
                    locationObject.put("lati", String.valueOf(longi));
                    locationObject.put("longi", String.valueOf(lati));
                }catch (Exception e){
                    System.out.println(e);
                }
                BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
                backgroundWorker.execute("insert",
                        arlname.get(position).toString(),
                        arlno.get(position).toString(),
                        locationObject.toString());
                backgroundWorker.data(getActivity(), new BackgroundWorker.Datasend() {
                    @Override
                    public void Getdata(String result) {

                        if(result.matches("")){

                        }else{
                            System.out.println("Data inserted");
                        }
                    }
                });
                System.out.println("adapter = "+arlname.get(position).toString());
                System.out.println("adapter = "+arlno.get(position).toString());
                System.out.println("adapter = "+String.valueOf(longi));
                System.out.println("adapter = "+String.valueOf(lati));
                longi=longi+0.001000;
                lati=lati+0.001000;
            }
        });


        return v;
    }
    private void start() {
        if (!mayRequestContacts()) {
            return;
        }
     GetContacts getContacts=new GetContacts();
        getContacts.execute();
    }

    class GetContacts extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.INVISIBLE);
            adapter = new DataListAdapter1((MainActivity) getActivity(), arlname, arlno);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ContentResolver cr = getActivity().getContentResolver();
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
                        arlname.add(name);
                        arlno.add(no);
                        pCur.close();
                    }
                }
            }
            return null;
        }
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (getActivity().checkSelfPermission( android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else{
            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
           // ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.READ_CONTACTS},
           //         REQUEST_READ_CONTACTS);
       //     ActivityCompat.requestPermissions(getActivity(),new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                start();
            }
        }
    }

}
