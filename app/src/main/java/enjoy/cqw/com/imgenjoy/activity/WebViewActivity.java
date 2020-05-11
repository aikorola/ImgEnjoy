package enjoy.cqw.com.imgenjoy.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import enjoy.cqw.com.imgenjoy.R;
import enjoy.cqw.com.imgenjoy.util.WebViewUtil;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebView webView = findViewById(R.id.webView);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        String url = getIntent().getStringExtra("more_url");
        if (!TextUtils.isEmpty(url)) {
            WebViewUtil.getHtml(progressBar,url, webView);
        }
    }
}
