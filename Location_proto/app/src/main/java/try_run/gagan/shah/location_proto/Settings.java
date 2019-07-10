package try_run.gagan.shah.location_proto;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Settings extends Fragment implements  DataListAdapter2.Listitem{

    private ListView listView;
    private ProgressBar progressBar,progressBar9;
    private DataListAdapter2 adapter;
    private View v;
    private LinearLayout linearLayout;
    private TextView textView;
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String MY_CONTACTS = "protected";
    public static final String NO_ONE = "private";
    public static final String EVERY_ONE= "public";
    private RadioGroup radioGroup;
    private int checkid;
    private  JSONArray jsonArrayid;
    private RadioButton radioButton,radioButton1,radioButton2;
    private String uname,umobile,ulogitude,ulatitude,uid,status,locationid,ucon,blocid;
    private Button button;
    private JSONArray jsonArray;
    private ArrayList<String> my_number=new ArrayList<String>();
    private ArrayList<Integer> blockid=new ArrayList<Integer>();
    private ArrayList<String> my_name=new ArrayList<String>();
    private ArrayList<String> my_longi=new ArrayList<String>();
    private SharedPreferences settings;
    private Activity activity;
    private ArrayList<String> my_lati=new ArrayList<String>();
    private ArrayList<String> my_pic=new ArrayList<String>();
    private ArrayList<String> my_id=new ArrayList<String>();
    private HashMap<String,Integer> hmap = new HashMap<String,Integer>();
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
        activity=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.fragment_settings, container, false);
        textView=(TextView)v.findViewById(R.id.selectall);
        radioGroup=(RadioGroup)v.findViewById(R.id.rg);
        radioButton=(RadioButton)v.findViewById(R.id.radioButton);
        radioButton1=(RadioButton)v.findViewById(R.id.radioButton1);
        radioButton2=(RadioButton)v.findViewById(R.id.radioButton2);
        button=(Button)v.findViewById(R.id.btsave);
        listView=(ListView)v.findViewById(R.id.listViewsetting);
        progressBar=(ProgressBar)v.findViewById(R.id.progressBar8);
        progressBar.invalidate();
        linearLayout=(LinearLayout)v.findViewById(R.id.settinglist);
        progressBar9=(ProgressBar)v.findViewById(R.id.progressBar9);
        progressBar9.invalidate();
        textView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar9.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);
        settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        checkid=settings.getInt("checkid",0);
        try {
           blocid=settings.getString("block",null);
            System.out.println("blockid = "+blocid);
            if(blocid!=null){
            jsonArrayid= new JSONArray(blocid);
            System.out.println("jsonArray = "+jsonArrayid);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dotask();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkid=checkedId;
                dotask();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar9.setVisibility(View.VISIBLE);
                if (checkid==R.id.radioButton){
                    System.out.println("Action for "+EVERY_ONE);
                    BackgroundWorker backgroundWorker=new BackgroundWorker(activity);
                    backgroundWorker.execute("setting",uid,EVERY_ONE);
                    backgroundWorker.data(activity, new BackgroundWorker.Datasend() {
                        @Override
                        public void Getdata(String result) {
                            System.out.println("result = "+result);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putInt("checkid", checkid);
                            editor.putString("block",null);
                            editor.commit();
                            progressBar9.setVisibility(View.INVISIBLE);
                        }
                    });

                }else
                if (checkid==R.id.radioButton1){
                    System.out.println("Action for "+NO_ONE);
                    BackgroundWorker backgroundWorker=new BackgroundWorker(activity);
                    backgroundWorker.execute("setting",uid,NO_ONE);
                    backgroundWorker.data(activity, new BackgroundWorker.Datasend() {
                        @Override
                        public void Getdata(String result) {
                            System.out.println("result = "+result);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putInt("checkid", checkid);
                            editor.putString("block",null);
                            editor.commit();
                            progressBar9.setVisibility(View.INVISIBLE);
                        }
                    });
                }else
                if (checkid==R.id.radioButton2){
                    jsonArray=new JSONArray();
                    for(int i=0;i<my_id.size();i++){
                        if(hmap.get(my_id.get(i))==0){
                            jsonArray.put(Integer.valueOf(my_id.get(i)));
                        }
                    }
                    BackgroundWorker backgroundWorker=new BackgroundWorker(activity);
                    backgroundWorker.execute("setting",uid,MY_CONTACTS,jsonArray.toString());
                    backgroundWorker.data(activity, new BackgroundWorker.Datasend() {
                        @Override
                        public void Getdata(String result) {
                            System.out.println("result = "+result);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putInt("checkid", checkid);
                            editor.putString("block",jsonArray.toString());
                            editor.commit();
                            progressBar9.setVisibility(View.INVISIBLE);
                        }
                    });


                }
                Toast.makeText(getActivity(),"Settings Saved",Toast.LENGTH_SHORT).show();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textView.getText().equals("Deselect all")){
                    textView.setText("Select all");
                    for(int i=0;i<my_id.size();i++){
                        hmap.put(my_id.get(i),0);
                    }
                    adapter.notifyDataSetChanged();
                }else{
                    textView.setText("Deselect all");
                    for(int i=0;i<my_id.size();i++){
                    hmap.put(my_id.get(i),1);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        listView.setItemsCanFocus(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                System.out.println(position);
//                hmap.put(my_number.get(position),0);
//                adapter=new DataListAdapter2((MainActivity) getActivity(),my_name,my_number,my_pic,hmap);
//                listView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
            }
        });



        return v;
    }

    private void dotask() {
        if (checkid==R.id.radioButton){
            radioGroup.check(checkid);
            System.out.println("Everyone = "+checkid);
            linearLayout.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
        }else
        if (checkid==R.id.radioButton1){
            radioGroup.check(checkid);
            System.out.println("No one = "+checkid);
            linearLayout.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
        }else
        if (checkid==R.id.radioButton2){
            radioGroup.check(checkid);
            linearLayout.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            System.out.println("My contacts = "+checkid);
            progressBar.setVisibility(View.VISIBLE);
            my_longi.clear();
            my_lati.clear();
            my_number.clear();
            my_name.clear();
            my_id.clear();
            getname(settings.getString("contacts",null));
        }
        else{
            radioGroup.check(R.id.radioButton2);
            linearLayout.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            System.out.println("My contacts = "+checkid);
            progressBar.setVisibility(View.VISIBLE);
            my_longi.clear();
            my_lati.clear();
            my_number.clear();
            my_name.clear();
            my_id.clear();
            getname(settings.getString("contacts",null));

        }
    }

    private void getname(String contacts) {

        BackgroundWorker backgroundWorker = new BackgroundWorker(activity);
        backgroundWorker.execute("get",uid,contacts);
        backgroundWorker.data(getActivity(), new BackgroundWorker.Datasend() {
            @Override
            public void Getdata(String result) {
                System.out.println("My result = "+result);
                String data=result;
                if(data.equals(null)){
                    progressBar.setVisibility(View.INVISIBLE);
                    System.out.println("No data found");
                    my_name.add("nulldata");
                    adapter=new DataListAdapter2((MainActivity) getActivity(),my_name);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else{
                    JSONArray jsonArray= null;
                    try {
                        JSONObject jsonObject1= new JSONObject(result);
                        status=jsonObject1.get("status").toString();
                        if(status=="1"){
                            jsonArray=jsonObject1.getJSONArray("data");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject2=jsonArray.getJSONObject(i);
                                my_id.add(jsonObject2.getString("id").toString());
                                my_name.add(jsonObject2.getString("name").toString());
                                my_number.add(jsonObject2.getString("number").toString());
                                if(jsonArrayid==null||jsonArray.length()>0){
                                    hmap.put(jsonObject2.getString("id").toString(),1);
                                }else{
                                    System.out.println(jsonObject2.getString("id").toString());

                                    for(int x=0;x<jsonArrayid.length();x++){
                                        System.out.println("bid = "+jsonArrayid.get(x));
                                        if(Integer.valueOf(jsonObject2.getString("id").toString())!=(int)jsonArrayid.get(x)){
                                            hmap.put(jsonObject2.getString("id").toString(),1);
                                            System.out.println("add = "+1);
                                        }else{
                                            hmap.put(jsonObject2.getString("id").toString(),0);
                                            System.out.println("add = "+0);
                                            break;
                                        }
                                    }
                                }
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
                            adapter=new DataListAdapter2((MainActivity) getActivity(),my_name);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    System.out.println("hashmap = "+hmap);
                    adapter=new DataListAdapter2((MainActivity) getActivity(),my_name,my_number,my_pic,hmap,my_id,Settings.this);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    textView.setVisibility(View.VISIBLE);
                   }
            }
        });
    }


    @Override
    public void senddata(int position) {
      if(hmap.get(my_id.get(position))==0){
          hmap.put(my_id.get(position),1);
          adapter.notifyDataSetChanged();
      }else{
          hmap.put(my_id.get(position),0);
          adapter.notifyDataSetChanged();
      }

    }
}
