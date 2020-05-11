package enjoy.cqw.com.imgenjoy;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.tencent.bugly.crashreport.CrashReport;

import org.litepal.LitePal;

public class App extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        LitePal.initialize(this);
        initBugly();
    }

    private void initBugly() {
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setAppChannel("myChannel");
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            strategy.setAppVersion(packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        strategy.setAppPackageName(getPackageName());
        CrashReport.initCrashReport(this, "2ecb540c79", false, strategy);
    }

    public static Context getContext() {
        return mContext;
    }
}
