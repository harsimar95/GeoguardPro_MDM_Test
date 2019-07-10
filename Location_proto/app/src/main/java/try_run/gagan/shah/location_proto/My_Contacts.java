package try_run.gagan.shah.location_proto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class My_Contacts extends Fragment {
    public interface MyContactAction{
        public void getposition(double longi,double lati);
    }
MyContactAction myContactAction;
    private ArrayList<String> my_number=new ArrayList<String>();
    private ArrayList<String> my_name=new ArrayList<String>();
    private ArrayList<String> my_longi=new ArrayList<String>();
    private ArrayList<String> my_lati=new ArrayList<String>();
    private ArrayList<String> my_pic=new ArrayList<String>();
    private String uname,umobile,ulogitude,ulatitude,uid,status,locationid,ucon;
    private ListView listView;
    private ProgressBar progressBar;
    private double logg, lt;
    private DataListAdapter adapter;
    private LatLng sydney;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args!=null){
            uname =args.getString("mobile");
            umobile =args.getString("name");
            ulogitude=args.getString("longi");
            ulatitude=args.getString("lati");
            ucon=args.getString("con");
            uid=args.getString("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_my__contacts, container, false);
        listView=(ListView)v.findViewById(R.id.mylist);
        progressBar=(ProgressBar)v.findViewById(R.id.progressBar2);
        progressBar.invalidate();
        my_longi.clear();
        my_lati.clear();
        my_number.clear();
        my_name.clear();
        GetMyContacts getMyContacts=new GetMyContacts(getActivity());
        getMyContacts.getContacts(new GetMyContacts.allmycontacts() {
            @Override
            public void actionMycontacts(String contacts) {
                System.out.println("new contacts"+contacts);
                getname(contacts);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                logg=Double.valueOf(my_lati.get(position));
                lt=Double.valueOf(my_longi.get(position));
                myContactAction=(MyContactAction)getActivity();
                System.out.println(lt+"   "+logg);
                myContactAction.getposition(logg,lt);

            }
        });


        return v;
    }
    private void getname(String contacts) {
        BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
        backgroundWorker.execute("get",uid,contacts);
        backgroundWorker.data(getActivity(), new BackgroundWorker.Datasend() {
            @Override
            public void Getdata(String result) {
                String data=result;
                System.out.println("new data"+data);
                if(!data.equals(null)){
                JSONArray jsonArray= null;
                try {
                    JSONObject jsonObject1= new JSONObject(result);
                    status=jsonObject1.get("status").toString();
                    if(status=="1"){
                        jsonArray=jsonObject1.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject2=jsonArray.getJSONObject(i);
                        my_name.add(jsonObject2.getString("name").toString());
                        my_number.add(jsonObject2.getString("number").toString());
                        locationid=jsonObject2.getString("location").toString();
                        JSONObject jsonObjectlocation= new JSONObject(locationid);
                        my_lati.add(jsonObjectlocation.getString("lati").toString());
                        my_longi.add(jsonObjectlocation.getString("longi").toString());
                        my_pic.add(jsonObject2.get("pic_url").toString());
                        }
                    }else if(status.matches("0")) {
                        progressBar.setVisibility(View.INVISIBLE);
                        System.out.println("No data found");
                        my_name.add("nulldata");
                        adapter=new DataListAdapter((MainActivity) getActivity(),my_name);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.INVISIBLE);
                adapter=new DataListAdapter((MainActivity) getActivity(),my_name,my_number,my_pic);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    System.out.println("No data found");
                    my_name.add("nulldata");
                    adapter=new DataListAdapter((MainActivity) getActivity(),my_name);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();}
            }
        });

    }

}
