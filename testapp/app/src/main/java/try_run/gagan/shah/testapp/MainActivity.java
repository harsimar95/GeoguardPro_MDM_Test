package try_run.gagan.shah.testapp;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    double longi=30.704746;
    double lati=76.689222;
    private DataListAdapter adapter;
    ArrayList arlname = new ArrayList<String>();
    ArrayList arlno = new ArrayList<String>();
    private static final int REQUEST_READ_CONTACTS = 0;
String no="";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView)findViewById(R.id.list);
        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlankFragment3 blankFragment=new BlankFragment3();
                FragmentManager fragmentManager;
                fragmentManager=getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fm,blankFragment);
                fragmentTransaction.commit();
            }
        });
      //  start();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BackgroundWorker backgroundWorker = new BackgroundWorker(MainActivity.this);
                backgroundWorker.execute("insert",
                        arlname.get(position).toString(),
                        arlno.get(position).toString(),
                        String.valueOf(longi),
                        String.valueOf(lati));
                System.out.println("adapter = "+arlname.get(position).toString());
                System.out.println("adapter = "+arlno.get(position).toString());
                System.out.println("adapter = "+String.valueOf(longi));
                System.out.println("adapter = "+String.valueOf(lati));
                longi=longi+0.001000;
                lati=lati+0.001000;
            }
        });



    }

    private void start() {
        if (!mayRequestContacts()) {
            return;
        }
        removecontect();
    }

    private void removecontect() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    pCur.moveToNext();
                    no = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
                    arlname.add(name);
                    arlno.add(no);
                    pCur.close();
                }

            }
        }
        adapter=new DataListAdapter(this,arlname,arlno);
//        System.out.println("adapter = "+adapter);
//        System.out.println("listView = "+listView);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            System.out.println("1 = "+String.valueOf(new String[]{android.Manifest.permission.READ_CONTACTS}));
            System.out.println("2 = "+REQUEST_READ_CONTACTS);
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        } else {
            System.out.println("1 = "+String.valueOf(new String[]{android.Manifest.permission.READ_CONTACTS}));
            System.out.println("2 = "+REQUEST_READ_CONTACTS);
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
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
