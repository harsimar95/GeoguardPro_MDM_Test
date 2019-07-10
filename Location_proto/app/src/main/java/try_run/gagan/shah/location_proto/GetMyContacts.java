package try_run.gagan.shah.location_proto;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by root on 5/11/16.
 */

public class GetMyContacts {

    private  String no="";
    private ArrayList arlno = new ArrayList<String>();
    public static String contacts;
    private Context getActivity;
    allmycontacts allmycontact;
    public GetMyContacts(Context context){
        this.getActivity=context;
    }
    public interface allmycontacts{
        public void actionMycontacts(String contacts);
    }
    public void getContacts(allmycontacts allmycontact){
        this.allmycontact=allmycontact;
        GetContacts getContacts=new GetContacts();
        getContacts.execute();
    }

    class GetContacts extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
         //   System.out.println("arlno = "+ arlno );
            JSONArray jsonArray1=new JSONArray();
            for (int i=0; i < arlno.size(); i++) {
                jsonArray1.put(arlno.get(i));
            }
            contacts= jsonArray1.toString();
            allmycontact.actionMycontacts(contacts);
        }

        @Override
        protected Void doInBackground(Void... params) {
            ContentResolver cr = getActivity.getContentResolver();
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
                        String newno="";
                        no=no.trim();
                        String[] noa=no.split("-");
                        for(int i=0;i<noa.length;i++){newno=newno+noa[i];}
                        no=newno;



                        newno="";
                        String[] noaa=no.split(" ");
                        for(int i=0;i<noaa.length;i++){newno=newno+noaa[i];}
                        no=newno;


                        if(no.charAt(0)=='+'){
                            no=no.substring(1);
                        }

                        if(no.length()>10){
                            if(no.charAt(0)=='0'){
                                no=no.substring(1);
                            }
                        }


                        if(no.length()>10){
                            if(no.charAt(0)=='9'){
                                no=no.substring(1);
                            }
                        }

                        if(no.length()>10){
                            if(no.charAt(0)=='1'){
                                no=no.substring(1);
                            }
                        }

                           System.out.println("no n = "+ no );

                        arlno.add(no);
                        pCur.close();
                    }
                }
            }
            return null;
        }
    }


}
