package try_run.gagan.shah.location_proto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sumanthanda on 9/12/16.
 */
public class ExpListAdapter extends BaseExpandableListAdapter {
    ArrayList<String> listData;
    HashMap<String, ArrayList> listDataChild;
    Context context;

    public ExpListAdapter(MainActivity mainActivity, ArrayList<String> listData, HashMap<String, ArrayList> listDataChild) {
        this.context=mainActivity;
        this.listData=listData;
        this.listDataChild=listDataChild;
    }

    @Override
    public int getGroupCount() {
        return listData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listDataChild.get(listData.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listDataChild.get(listData.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.listdata, parent, false);
        ImageView imageView=(ImageView)v.findViewById(R.id.indic);
        TextView textView=(TextView)v.findViewById(R.id.textView);
        textView.setText(listData.get(groupPosition).toString());
        if(getChildrenCount(groupPosition)!=0){
            if(isExpanded){
                imageView.setImageResource(R.drawable.down);
            }else
            {imageView.setImageResource(R.drawable.up);}
        }



        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v;
        String childText = getChild(groupPosition, childPosition).toString();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.listdatachield, parent, false);
        TextView textView=(TextView)v.findViewById(R.id.textViewc);
        textView.setText(childText);


        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
