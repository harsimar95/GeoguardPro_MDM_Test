package try_run.gagan.shah.testapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sumanthanda on 9/5/16.
 */
class DataListAdapter extends BaseAdapter {

    ArrayList arlname = new ArrayList<String>();
    ArrayList arlno = new ArrayList<String>();
    TextView listname,listno;
    private Context context;

    DataListAdapter() {
        arlname = null;
        arlno = null;


    }

    public DataListAdapter(MainActivity mainActivity, ArrayList arlname, ArrayList arlno) {
        this.context=mainActivity;
        this.arlname=arlname;
        this.arlno = arlno;

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

    public View getView(int position, View convertView, ViewGroup parent) {
     //   Log.d("adaptercheck","=>"+position);
        View v ;


       // get view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.listdata, parent, false);

      //  System.out.println("datelist[position] = "+datelist[position]);
            // set data
            listname = (TextView) v.findViewById(R.id.listname);
            listname.setText(arlname.get(position).toString());
            listno = (TextView) v.findViewById(R.id.listno);
            listno.setText(arlno.get(position).toString());

        return(v);
    }

    public Context getContext() {
        return context;
    }
}
