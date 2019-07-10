package try_run.gagan.shah.location_proto;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class aboutus extends Fragment {
    private TextView textView;
     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_aboutus, container, false);
        textView=(TextView)v.findViewById(R.id.infos);
        textView.setText(Html.fromHtml(getString(R.string.aboputus)));
        return v;
    }


}
