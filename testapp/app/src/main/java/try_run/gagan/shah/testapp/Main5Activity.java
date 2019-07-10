package try_run.gagan.shah.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Main5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        BackgroundServies backgroundServies=new BackgroundServies(this);
        Intent intent=new Intent(this,backgroundServies.getClass());
        startService(intent);
    }

}
