package try_run.gagan.shah.testapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class Main4Activity extends AppCompatActivity {
    private Button button;
    private TextView textView;
    private EditText editText;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://frendsdom.com:3000");
        } catch (URISyntaxException e) {}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        button=(Button)findViewById(R.id.button3);
        textView=(TextView)findViewById(R.id.textViewnew);
        editText=(EditText)findViewById(R.id.editText1);
        mSocket.connect();
        mSocket.on("update", onNewMessage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });
    }
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            System.out.println("listener work");
            JSONObject data = (JSONObject) args[0];
          //  System.out.println("data = "+data.toString());

            String username;
            String message;
            try {
                username = data.getString("id");
                message = data.getString("message");
            } catch (JSONException e) {
                return;
            }

            // add the message to view

            System.out.println(username+"  -   "+message);

            addMessage(username, message);
//            System.out.println("message recive = "+message);
//            message="";
        }
    };

    private void addMessage(final String username, final String message) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                textView.setText(username+"  -   "+message);

            }
        });
    }

    private void attemptSend() {
        String message = editText.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }
        System.out.println("message send = "+message);
        editText.setText("");
        mSocket.emit("send", message);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("update", onNewMessage);
    }

}
