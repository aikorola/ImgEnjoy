package enjoy.cqw.com.imgenjoy.activity.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import enjoy.cqw.com.imgenjoy.R;
import enjoy.cqw.com.imgenjoy.activity.guide_image.GuideActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                finish();
            }
        }, 2000);
    }
}
