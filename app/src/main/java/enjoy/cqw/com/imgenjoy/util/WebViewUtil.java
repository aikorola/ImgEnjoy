package enjoy.cqw.com.imgenjoy.util;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebViewUtil {

    /**
     * @param progressBar
     * @param url         要打开html的路径
     * @param web         WebView控件
     */
    public static void getHtml(final ProgressBar progressBar, String url, WebView web) {
        initSetting(web);
        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false

        web.getSettings().setSupportZoom(true);// 是否可以缩放，默认true
        web.getSettings().setBuiltInZoomControls(true);// 是否显示缩放按钮，默认false
        web.getSettings().setUseWideViewPort(true);// 设置此属性，可任意比例缩放。大视图模式
        web.getSettings().setLoadWithOverviewMode(true);// 和setUseWideViewPort(true)一起解决网页自适应问题
        web.getSettings().setAppCacheEnabled(true);// 是否使用缓存
        web.getSettings().setDomStorageEnabled(true);// DOM Storage
        // w.getSettings().setUserAgentString("User-Agent:Android");//设置用户代理，一般不用
        web.loadUrl(url);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (View.GONE == progressBar.getVisibility()) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    private static void initSetting(WebView web) {
        // TODO Auto-generated method stub
        WebSettings settings = web.getSettings();
        // 是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        settings.setJavaScriptEnabled(true);
        /*
         * LOAD_DEFAULT设置如何缓存 默认使用缓存，当缓存没有，或者缓存过期，才使用网络
         * LOAD_CACHE_ELSE_NETWORK 设置默认使用缓存，即使是缓存过期，也使用缓存
         * 只有缓存消失，才使用网络
         */
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //是否展示一个缩放按钮（）
        settings.setBuiltInZoomControls(true);
    }
}