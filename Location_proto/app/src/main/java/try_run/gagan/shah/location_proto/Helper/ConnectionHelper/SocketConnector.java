package try_run.gagan.shah.location_proto.Helper.ConnectionHelper;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by sumanthanda on 10/28/16.
 */
public class SocketConnector {

    private static SocketConnector connector;
    private com.github.nkzawa.socketio.client.Socket socket;

    public static SocketConnector sharedInstance(){
        if(connector != null){
            return connector;
        }
        connector =  new SocketConnector();
        return connector;
    }

    public SocketConnector(){
        try {
            socket = IO.socket("http://frendsdom.com:3000");
            socket.connect();
        } catch (URISyntaxException e) {}
    }


    public Socket getSocket() {
        return socket;
    }


}
