package try_run.gagan.shah.testapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Main6Activity extends AppCompatActivity {

    private Button button;
    ImageView IV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        button=(Button)findViewById(R.id.button4);
        IV=(ImageView)findViewById(R.id.imageView) ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionInflater transitionInflater=TransitionInflater.from(Main6Activity.this);
                Transition transition=transitionInflater.inflateTransition(R.transition.trans);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setSharedElementExitTransition(transition);
                }
                ActivityOptionsCompat activityOptionsCompat= ActivityOptionsCompat.makeSceneTransitionAnimation(Main6Activity.this,IV,"tran");
                startActivity(new Intent(Main6Activity.this,Main7Activity.class), activityOptionsCompat.toBundle());

            }
        });
    }
}
