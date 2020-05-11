package enjoy.cqw.com.imgenjoy.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class FileUtil {
    private static final String TAG = FileUtil.class.getSimpleName();

    /**
     * 复制文件
     *
     * @param source 输入文件
     * @param target 输出文件
     */
    public static void save(File source, File target) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(source);
            fileOutputStream = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void copy(Bitmap bitmap, String savePath) {
        if (TextUtils.isEmpty(savePath)) return;
        File file = new File(savePath);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveImage(Bitmap image) {
        String saveImagePath = null;
        Random random = new Random();
        String imageFileName = "JPEG_" + "down" + random.nextInt(10) + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES) + "test");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            saveImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fout = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                fout.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(saveImagePath);
//            Toast.makeText(mContext, "IMAGE SAVED", Toast.LENGTH_LONG).show();
            Log.e(TAG, "saveImage: IMAGE SAVED");
        }
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
//        sendBroadcast(mediaScanIntent);   TODO
    }


    /**
     * 保存图片到指定路径并更新相册
     *
     * @param context  上下文
     * @param bitmap   要保存的图片
     * @return true 成功 false失败
     */
    public static boolean saveImageToGallery(Context context, Bitmap bitmap, String filePath) {
        // 保存图片至指定路径
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片(100代表压缩0%)
            boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            //发送广播通知系统图库刷新数据
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 检查文件是否存在
     * @param filePath
     * @return
     */
    public static boolean checkFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }
}

