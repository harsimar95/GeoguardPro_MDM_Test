package try_run.gagan.shah.location_proto;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.nearby.messages.Distance;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sumanthanda on 9/5/16.
 */
class DataListAdapter extends BaseAdapter {

    ArrayList arllongi = new ArrayList<String>();
    ArrayList arllati = new ArrayList<String>();
    ArrayList arlname = new ArrayList<String>();
    ArrayList arlno = new ArrayList<String>();
    ArrayList arlpic = new ArrayList<String>();
    private HashMap<String,Integer> arllast = new HashMap<String,Integer>();

    double longi,lati;
    TextView listname,listno,textimage,testdist;
    CircleView circleImageView;
    ImageView statu;
    CardView cardView;
    private Context context;

    DataListAdapter() {
        arlname = null;
        arlno = null;


    }
    public DataListAdapter(MainActivity mainActivity, ArrayList arlname, ArrayList arlno, ArrayList picurl) {
        this.context=mainActivity;
        this.arlname=arlname;
        this.arlno = arlno;
        this.arlpic=picurl;


    }
    public DataListAdapter(MainActivity mainActivity, ArrayList arlname, ArrayList arlno, ArrayList picurl, ArrayList<String> my_longi, ArrayList<String> my_lati,HashMap lastseen, double logg, double lt) {
        this.context=mainActivity;
        this.arlname=arlname;
        this.arlno = arlno;
        this.arlpic=picurl;
        this.arllati=my_lati;
        this.arllongi=my_longi;
        this.lati=lt;
        this.longi=logg;
        this.arllast=lastseen;

    }

    public DataListAdapter(MainActivity activity, ArrayList arlname) {
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

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
     //   System.out.println("notify");
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
     //   Log.d("adaptercheck","=>"+position);
        View v ;


       // get view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


     //   System.out.println("data.get(position) = "+arlname.get(position));
            // set data

        if(arlname.get(position).equals("nulldata")){
            v = inflater.inflate(R.layout.nodata, parent, false);

        }else {
            if(convertView==null){
                v = inflater.inflate(R.layout.customlist, parent, false);
            }
            else{
                v=convertView;
            }



            cardView=(CardView)v.findViewById(R.id.card);

            statu=(ImageView)v.findViewById(R.id.status);
      //      System.out.println("arllast data "+arllast);
      //      System.out.println("arllast data "+arlname);
            if(arllast.get(arlname.get(position))==0) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }else{
                cardView.setCardBackgroundColor(Color.GREEN);
               // statu.setTextColor(context.getResources().getColor(R.color.blue));
               // statu.setText("Online");
            }
            String dist;
            Location locationa=new Location("a");
            locationa.setLatitude(lati);
            locationa.setLongitude(longi);
            Location locationb=new Location("b");
            locationb.setLatitude(Double.valueOf((String) arllati.get(position)));
            locationb.setLongitude(Double.valueOf((String) arllongi.get(position)));
            float distance = locationa.distanceTo(locationb)/1000 ;
       //     System.out.println("distance"+distance);
            testdist=(TextView)v.findViewById(R.id.distance);
            testdist.setText(String.format("%.2f", distance) +" Km away");
            circleImageView = (CircleView) v.findViewById(R.id.circle);
      //      System.out.println(arlpic.get(position));
            if (arlpic.get(position) != "null") {
         //       System.out.println("for not null");
                Picasso.with(context)
                        .load((String) arlpic.get(position))
                        .into(circleImageView);
                textimage = (TextView) v.findViewById(R.id.imagetext);
                textimage.setText("");
            } else {
        //        System.out.println("fornull");
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

        return(v);
    }

    public Context getContext() {
        return context;
    }
}
