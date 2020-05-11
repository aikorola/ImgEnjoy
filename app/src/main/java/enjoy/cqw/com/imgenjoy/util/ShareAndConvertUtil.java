package enjoy.cqw.com.imgenjoy.util;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ShareAndConvertUtil {

    // ===========================================================分享全平台===========================================================

    /**
     * 分享文本到全平台
     *
     * @param context 上下文关系
     * @param text    要分享的文本
     */
    public static void shareTextToAll(Context context, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, "请选择"));
    }

    /**
     * 分享单张图片到全平台
     *
     * @param context 上下文关系
     * @param text    文本内容，并非所有平台在分享图片时候都支持此参数
     * @param img     图片uri地址
     */
    public static void shareImgToAll(Context context, String text, Uri img) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_STREAM, img);
        context.startActivity(Intent.createChooser(intent, "请选择"));
    }

    /**
     * 分享多张图片到全平台
     *
     * @param context 上下文关系
     * @param text    文本内容
     * @param images  图片Uri地址的集合
     */
    public static void shareImgsToAll(Context context, String text, ArrayList<Uri> images) {
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, images);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, "请选择"));
    }


    // ===========================================================分享部分平台===========================================================

    /**
     * 分享到Weichat
     *
     * @param context
     * @param uris
     * @param desc
     */
    public static void shareWeichat(AppCompatActivity context, ArrayList<Uri> uris, String desc) {
        if (!checkPermission(context)) {
            return;
        }
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.putExtra("Kdescription", desc);
        context.startActivity(intent);
    }

    /**
     * 分享到新浪微博
     *
     * @param context
     * @param uris
     * @param desc
     */
    public static void shareWeibo(AppCompatActivity context, ArrayList<Uri> uris, String desc) {
        if (!checkPermission(context)) {
            return;
        }
        Intent intent = new Intent();
        intent.setPackage("com.sina.weibo");
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.putExtra("Kdescription", desc);
        context.startActivity(intent);
    }

    /**
     * 分享到QQ
     *
     * @param context
     * @param uris
     * @param desc
     */
    public static void shareQQ(AppCompatActivity context, ArrayList<Uri> uris, String desc) {
        if (!checkPermission(context)) {
            return;
        }
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.putExtra("Kdescription", desc);
        context.startActivity(intent);
    }


    // ===========================================================类型转换===========================================================

    /**
     * bitmapToUri
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static Uri bitmapToUri(AppCompatActivity context, Bitmap bitmap) {
        if (!checkPermission(context)) {
            return null;
        }
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, null, null));
    }

    /**
     * drawableToUri
     *
     * @param context
     * @param drawable
     * @return
     */
    public static Uri drawableToUri(AppCompatActivity context, Drawable drawable) {
        if (!checkPermission(context)) {
            return null;
        }
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), ((BitmapDrawable) drawable).getBitmap(), null, null));
    }

    /**
     * viewToUri
     *
     * @param context
     * @param view
     * @return
     */
    public static Uri viewToUri(AppCompatActivity context, View view) {
        if (!checkPermission(context)) {
            return null;
        }
        view.buildDrawingCache();
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), view.getDrawingCache(), null, null));
    }

    /**
     * createUriList 创建Uri容器
     *
     * @param uris
     * @return
     */
    public static ArrayList<Uri> createUriList(Uri... uris) {
        ArrayList<Uri> result = new ArrayList<>();
        Collections.addAll(result, uris);
        return result;
    }

    /**
     * 文件(path)转Bitmap
     */
    public static Bitmap pathToBitmap(Context context, String filePath){
        Bitmap bitmap = null;
        File file = new File(filePath);
        Uri uri = Uri.fromFile(file);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        /*
        同类型的还有
        Bitmap loadbitmap = BitmapFactory.decodeFile(file);

        BitmapFactory.decodeStream(InputStream inputStream);//stream 转 bitmap
        BitmapFactory.decodeResource();//resource 转 bitmap
        BitmapFactory.decodeByteArray();//bytes 转 bitmap
        BitmapFactory.decodeFileDescriptor();//fileDescriptor 转 bitmap
        ...


        或 先压缩，再转
        Bitmap loadbitmap = BitmapFactory.decodeFile(imagePath, getBitmapOption(2));
        private BitmapFactory.Options getBitmapOption(int inSampleSize){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
            options.inSampleSize = inSampleSize;
            return options;
        }
        */
    }

    /**
     * Bitmap 转 文件
     */
    public static void bitmapToFile(Bitmap bitmap, String filePath) {
        File file = new File(filePath);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Uri 转 File
     */
    public static File uriToFile(Uri uri) {
        return new File(uri.getPath());
    }

    /**
     * File 转 Uri
     */
    public Uri fileToUri(File file) {
        return Uri.fromFile(file);
    }

    // ===========================================================检查权限===========================================================

    /**
     * 权限检查
     *
     * @param context
     * @return
     */
    private static boolean checkPermission(AppCompatActivity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] mPermissionList = new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(context, mPermissionList, 1);
                return false;
            }
        }
        return true;
    }
}
