package try_run.gagan.shah.location_proto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sumanthanda on 9/5/16.
 */
class DataListAdapter2 extends BaseAdapter {

    ArrayList arlname = new ArrayList<String>();
    ArrayList my_id = new ArrayList<String>();
    ArrayList arlno = new ArrayList<String>();
    private HashMap<String,Integer> hmap = new HashMap<String,Integer>();
    ArrayList arlpic = new ArrayList<String>();
    TextView listname,listno,textimage;
    CircleView circleImageView;
    private CheckBox checkBox;
    private Context context;

    DataListAdapter2() {
        arlname = null;
        arlno = null;


    }
    public interface Listitem{
        public void senddata(int position);
    }
    Listitem listitem;
    public DataListAdapter2(MainActivity mainActivity, ArrayList arlname, ArrayList arlno, ArrayList picurl, HashMap hmap, ArrayList my_id, Listitem listitem) {
        this.context=mainActivity;
        this.arlname=arlname;
        this.arlno = arlno;
        this.arlpic=picurl;
        this.hmap=hmap;
        this.listitem=listitem;
        this.my_id=my_id;
    }

    public DataListAdapter2(MainActivity activity, ArrayList arlname) {
        this.context=activity;
        this.arlname=arlname;
    }


    public int getCount() {
         return arlname.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
     //   Log.d("adaptercheck","=>"+position);
        View v ;


            // get view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            System.out.println("data.get(position) = " + arlname.get(position));
            // set data
        System.out.println("view = "+ view);


            if (arlname.get(position).equals("nulldata")) {
                v = inflater.inflate(R.layout.nodata, parent, false);
            } else {
                if(view==null){
                    v = inflater.inflate(R.layout.settinglist, parent, false);
                }
                else{
                    v=view;
                }

                v = inflater.inflate(R.layout.settinglist, parent, false);
                checkBox=(CheckBox)v.findViewById(R.id.checkBox);
                String temp = my_id.get(position).toString();
                int z = hmap.get(temp);
                if(z==1){
                   checkBox.setChecked(true);
                }else{
                    checkBox.setChecked(false);
                }
                circleImageView = (CircleView) v.findViewById(R.id.circle);
                System.out.println(arlpic.get(position));
                if (arlpic.get(position) != "null") {
                    System.out.println("for not null");
                    Picasso.with(context)
                            .load((String) arlpic.get(position))
                            .into(circleImageView);
                } else {
                    System.out.println("fornull");
                    circleImageView.setImageResource(R.drawable.backcon);
                    String n = arlname.get(position).toString();
                    String nm = String.valueOf(n.charAt(0));
                    textimage = (TextView) v.findViewById(R.id.imagetext);
                    textimage.setText(nm);
                }


                listname = (TextView) v.findViewById(R.id.listname);
                listname.setText(arlname.get(position).toString());
                listno = (TextView) v.findViewById(R.id.listno);
                listno.setText(arlno.get(position).toString());
            }
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listitem.senddata(position);
            }
        });
        return(v);
    }

    public Context getContext() {
        return context;
    }
}
