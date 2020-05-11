package enjoy.cqw.com.imgenjoy.util.wallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageUtil {
    private static final String TAG = "ImageUtil";

    public static Bitmap getImageBitmap(String srcPath) {
        return BitmapFactory.decodeFile(srcPath);
    }

    public static Bitmap compressBitmap(String imagePath) {
        Log.e(TAG, "onBindViewHolder_local: " + imagePath);
        // 设置参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
        BitmapFactory.decodeFile(imagePath, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2
        int minLen = Math.min(height, width); // 原图的最小边长
        if (minLen > 100) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
            float ratio = (float) minLen / 300.0f; // 计算像素压缩比例
            inSampleSize = (int) ratio;
        }
        options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
        options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
        Bitmap bm = BitmapFactory.decodeFile(imagePath, options); // 解码文件
        Log.e(TAG, "size: " + bm.getByteCount() + " width: " + bm.getWidth() + " heigth:" + bm.getHeight()); // 输出图像数据
        return bm;
    }

    public static Bitmap compressBitmap(Context context, int resId) {
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);

        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeResource(context.getResources(), resId, options2);
    }
}
