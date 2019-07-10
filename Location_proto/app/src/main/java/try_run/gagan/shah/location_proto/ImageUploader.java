package try_run.gagan.shah.location_proto;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by sumanthanda on 10/7/16.
 */
public class ImageUploader extends AsyncTask<String,Void,String>{
    public interface Datasend{
        public void Getdata(String result);
    }
    private Datasend datasend;
    Context context;
    public ImageUploader(Context context) {
        this.context=context;

    }
    void data(Context ctx,Datasend datasend) {
        context = ctx;
        this.datasend=datasend;
    }
    @Override
    protected String doInBackground(String... params) {
        String image = params[1];
        String uid = params[2];
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
    }
}
