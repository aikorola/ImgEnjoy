package enjoy.cqw.com.imgenjoy.service;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.os.IBinder;

import java.io.IOException;

import enjoy.cqw.com.imgenjoy.R;

public class SetWallpaperService extends Service {
    private int current = 0;  //当前壁纸下标
    //    private int[] papers = new int[]{R.drawable.ffrhh, R.drawable.pic1,R.drawable.fffg, R.drawable.jjde};
    private int[] papers;
    private WallpaperManager wManager = null;

    @Override
    public void onCreate() {
        super.onCreate();
        wManager = WallpaperManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (current > 4) {
            current = 0;
        }
        try {
           /* Bitmap bitmap =BitmapFactory.decodeResource(getResources(),papers[current]);
            wManager.setBitmap(bitmap);*/
            //切换第二张
            wManager.setResource(papers[current++]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}