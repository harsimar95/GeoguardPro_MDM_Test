package try_run.gagan.shah.testapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class BlankFragment3 extends Fragment {

    int id=70;
    String ids;
    String nameid;
    String  mobileid;
    String latitudeid;
    String logitudeid;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_blank_fragment3, container, false);
        BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
//        String query="SELECT MAX(id) FROM user_location";
//        backgroundWorker.execute("request",
//                query);
//        backgroundWorker.data(getActivity(), new BackgroundWorker.Datasend() {
//            @Override
//            public void Getdata(String result) {
//                System.out.println("result = "+result );
//                JSONArray jsonArray= null;
//                try {
//                    jsonArray = new JSONArray(result);
//                    JSONObject parentObject =jsonArray.getJSONObject(0);
//                     id=parentObject.get("MAX(id)").toString();
//                    System.out.println("id = "+id );
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        String query1="SELECT * FROM user_location WHERE id="+id;
        backgroundWorker.execute("request", query1);
        backgroundWorker.data(getActivity(), new BackgroundWorker.Datasend() {
            @Override
            public void Getdata(String result) {
                System.out.println("result = "+result );
                JSONArray jsonArray= null;
                try {
                    jsonArray = new JSONArray(result);
                    JSONObject parentObject =jsonArray.getJSONObject(0);
                    ids= parentObject.get("id").toString();
                    nameid=parentObject.get("name").toString();
                    mobileid=parentObject.get("mobile").toString();
                    latitudeid=parentObject.get("latitude").toString();
                    logitudeid=parentObject.get("longitude").toString();
                    System.out.println("id = " + id);
                    System.out.println("nameid = " + nameid);
                    System.out.println("mobileid = " + mobileid);
                    System.out.println("adapterid = " + latitudeid);
                    System.out.println("adapterid = " + logitudeid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return v;
    }

}
