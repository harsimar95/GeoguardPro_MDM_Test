package assignment.mansa.com.iskconapp.ProcessorInfo;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sumanthanda on 23/08/16.
 */
public class JSONProcessor {


    public String loadJSON(InputStream inputStream) {
        Log.d("day", "loadJSON");
        String json = null;
        try {
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
            Log.d("day", "json => "+json);
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.d("day", "exeption => "+ex.toString());
            return null;
        }
        return json;
    }
}
