package try_run.gagan.shah.location_proto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ContactusFragment extends Fragment {
    private EditText editTextname,editTextemail,editTextmessage;
    private Button buttonsubmit;
    private AlertDialog alertDialog;
    private String name,email,message;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_contactus, container, false);
        editTextname=(EditText)v.findViewById(R.id.name);
        editTextemail=(EditText)v.findViewById(R.id.email);
        editTextmessage=(EditText)v.findViewById(R.id.message);
        buttonsubmit=(Button) v.findViewById(R.id.bt);
        TextView tv = (TextView) v.findViewById(R.id.info);
        tv.setText(Html.fromHtml(getString(R.string.contactus)));
        buttonsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=editTextname.getText().toString();
                email=editTextemail.getText().toString();
                message=editTextmessage.getText().toString();
                message="Name : "+name+ "\n" +"Email : "+email+"\n"+"Message : "+message;
                if( editTextname.length() == 0 || editTextname.equals("") || editTextname == null)
                {
                    alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Warning");
                    alertDialog.setMessage("Please enter your Name. ");
                    alertDialog.show();
                }
                else{
                    if( editTextemail.length() == 0 || editTextemail.equals("") || editTextemail == null)
                    {
                        alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Warning");
                        alertDialog.setMessage("Please enter your Email. ");
                        alertDialog.show();
                    }
                    else{
                        if( editTextmessage.length() == 0 || editTextmessage.equals("") || editTextmessage == null)
                        {
                            alertDialog = new AlertDialog.Builder(getActivity()).create();
                            alertDialog.setTitle("Warning");
                            alertDialog.setMessage("Please enter your Message. ");
                            alertDialog.show();
                        }
                        else{
                            sendEmail();
                        }
                    }
                }




            }
        });
        return v;
    }
    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"deepakmishra@mansainfotech.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Omapp Contact Us");
        emailIntent.putExtra(Intent.EXTRA_TEXT, message );

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
        editTextmessage.setText("");
        editTextname.setText("");
        editTextemail.setText("");
    }

}
