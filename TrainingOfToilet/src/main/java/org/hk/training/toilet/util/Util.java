package org.hk.training.toilet.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class Util {

    public static void log(String log) {
        Log.e(Util.class.getClass().getPackage().getName(), log);
    }

    /**
     * 配列をjoinする.
     *
     * @param array - joinする配列
     * @param with  - 区切り文字
     */
    public static String join(final String[] array, final String with) {
        final StringBuilder str = new StringBuilder();
        str.setLength(0);
        for (final String s : array) {
            if (str.length() > 0) {
                str.append(with);
            }
            str.append(s);
        }
        return str.toString();
    }

    /**
     * Androidのバグ対応.
     * 日付をフォーマットする際に、いちいちロケールを見に行き、時間がかかるので、<br />
     * SimpleDateFormatをThreadLocalで保持する。
     */
    private static final ThreadLocal<Map<String, SimpleDateFormat>> formatMap = new ThreadLocal<Map<String, SimpleDateFormat>>() {
        @Override
        protected Map<String, SimpleDateFormat> initialValue() {
            return new HashMap<String, SimpleDateFormat>();
        }
    };

    /**
     * 日付を文字型にする.
     *
     * @param date   - 日付
     * @param format - 変換するフォーマット
     */
    public static String parseCreatedAt(final Date date, final String format) {
        SimpleDateFormat sdf = formatMap.get().get(format);
        if (null == sdf) {
            sdf = new SimpleDateFormat(format, Locale.getDefault());
            //sdf.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Tokyo"));
            formatMap.get().put(format, sdf);
        }
        return sdf.format(date);
    }

    public static final String FORMAT_PARSE_DATE = "yyyy-MM-dd HH:mm:ss";

    public static Date encodeDate(final String date, final String format)
            throws ParseException {
        DateFormat df = new SimpleDateFormat(format);
        return df.parse(date);
    }

    public static String decodeDate(final Date date, final String format) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    private static final String TEMP_FILE_NAME = "temp";

    public static File getTempFile(final Context context, final boolean isRandom) {
        //it will return /sdcard/image.tmp

        File directory = getSdCardDirectory("");
        if(directory == null){
            directory = context.getCacheDir();
        }
        final File path = new File(directory, "");

        if (!path.exists()) {
            path.mkdir();
        }
        if (isRandom) {
            String uuid = UUID.randomUUID().toString();
            return new File(path, uuid);
        }
        return new File(path, TEMP_FILE_NAME);
    }

    public static File getSdCardDirectory(final String path) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File directory = Environment.getExternalStorageDirectory();
            File shokunote = new File(directory, path);
            if(shokunote.exists()){
                return directory;
            }
            for(String dir : SDCARD_EXTERNAL_DIRECTORIES){
                File parent = new File(directory.getParentFile(), dir);
                File f = new File(directory, dir);
                if(f.exists()){
                    return f;
                }else if(parent.exists()){
                    return parent;
                }
            }
        }
        return null;
    }

    private static String[] SDCARD_EXTERNAL_DIRECTORIES = {"external-sd", "extSdCard", "sdcard-ext"};

    public static Bitmap createBitmap(InputStream is1, InputStream is2, int width,
                                      int height, String path, int orientation) {
        BufferedOutputStream outputStream = null;
        try {
            final Bitmap bitmap = decodeSampledBitmapFromResource(is1, is2, width, height);
            final Bitmap b = bitmap.copy(Bitmap.Config.ARGB_4444, true);
            Bitmap createScaledBitmap = Bitmap.createBitmap(
                    b,
                    0,
                    0,
                    b.getWidth(),
                    b.getHeight(),
                    getScale(b, width, height),
                    true);
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);
            createScaledBitmap = Bitmap.createBitmap(
                    createScaledBitmap,
                    0,
                    0,
                    createScaledBitmap.getWidth(),
                    createScaledBitmap.getHeight(),
                    matrix,
                    true);
            b.recycle();
            bitmap.recycle();

            File f = new File(path);
            try {
                outputStream = new BufferedOutputStream(new FileOutputStream(f.getPath()), 8);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            createScaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            return createScaledBitmap;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(InputStream is1, InputStream is2,
                                                         int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is1, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is2, null, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                             int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public static Matrix getScale(final Bitmap b, final int width, final int height) {
        final Matrix m = new Matrix();
        if (b.getWidth() > b.getHeight()) {
            m.postScale(((float) width) / b.getWidth(), ((float) width) / b.getWidth());
        } else if (b.getWidth() < b.getHeight()) {
            m.postScale(((float) height) / b.getHeight(), ((float) height) / b.getHeight());
        } else {
            m.postScale(((float) width) / b.getWidth(), ((float) height) / b.getHeight());
        }
        return m;
    }

    public static int[] decodeYUV(byte[] data, int width, int height) throws NullPointerException,
            IllegalArgumentException {
        int size = width * height;
        if (data == null) {
            throw new NullPointerException("bufffer data is null");
        }
        if (data.length < size) {
            throw new IllegalArgumentException("buffer data is illegal");
        }
        int[] out = new int[size];
        int Y, Cr = 0, Cb = 0;
        for (int i = 0; i < height; i++) {
            int index = i * width;
            int jDiv2 = i >> 1;
            for (int i2 = 0; i2 < width; i2++) {
                Y = data[index];
                if (Y < 0) {
                    Y += 255;
                }
                if ((i2 & 0x1) != 1) {
                    int c0ff = size + jDiv2 * width + (i2 >> 1) * 2;
                    Cb = data[c0ff];
                    if (Cb < 0) {
                        Cb += 127;
                    } else {
                        Cb -= 128;
                    }
                    Cr = data[c0ff + 1];
                    if (Cr < 0) {
                        Cr += 127;
                    } else {
                        Cr -= 128;
                    }
                }
                // red
                int R = Y + Cr + (Cr >> 2) + (Cr >> 3) + (Cr >> 5);
                if (R < 0) {
                    R = 0;
                } else if (R > 255) {
                    R = 255;
                }
                // green
                int G = Y - (Cb >> 2) + (Cb >> 4) + (Cb >> 5) - (Cr >> 1) + (Cr >> 3) + (Cr >> 4)
                        + (Cr >> 5);
                if (G < 0) {
                    G = 0;
                } else if (G > 255) {
                    G = 255;
                }
                int B = Y + Cb + (Cb >> 1) + (Cb >> 2) + (Cb >> 6);
                if (B < 0) {
                    B = 0;
                } else if (B > 255) {
                    B = 255;
                }
                out[index] = 0xff000000 + (B << 16) + (G << 8) + R;
                index++;
            }
        }
        return out;
    }

    public static Date getMonthEndDay(Date targetDate) {
        Calendar endMonthCal = Calendar.getInstance();
        endMonthCal.setTime(targetDate);
        endMonthCal.set(Calendar.DATE, endMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        endMonthCal.set(Calendar.HOUR_OF_DAY, 23);
        endMonthCal.set(Calendar.MINUTE, 59);
        endMonthCal.set(Calendar.SECOND, 59);
        endMonthCal.set(Calendar.MILLISECOND, 999);
        return endMonthCal.getTime();
    }

    public static Date getMonthStartDay(Date targetDate) {
        Calendar startMonthCal = Calendar.getInstance();
        startMonthCal.setTime(targetDate);
        startMonthCal.set(Calendar.DATE, startMonthCal.getActualMinimum(Calendar.DAY_OF_MONTH));
        startMonthCal.set(Calendar.HOUR_OF_DAY, 0);
        startMonthCal.set(Calendar.MINUTE, 0);
        startMonthCal.set(Calendar.SECOND, 0);
        startMonthCal.set(Calendar.MILLISECOND, 0);
        return startMonthCal.getTime();
    }

    public static Date getMonthEndDay(Calendar cal) {
        Calendar endMonthCal = Calendar.getInstance();
        endMonthCal.setTime(cal.getTime());
        endMonthCal.set(Calendar.DATE, endMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        endMonthCal.set(Calendar.HOUR_OF_DAY, 23);
        endMonthCal.set(Calendar.MINUTE, 59);
        endMonthCal.set(Calendar.SECOND, 59);
        endMonthCal.set(Calendar.MILLISECOND, 999);
        return endMonthCal.getTime();
    }

    public static Date getMonthStartDay(Calendar cal) {
        Calendar startMonthCal = Calendar.getInstance();
        startMonthCal.setTime(cal.getTime());
        startMonthCal.set(Calendar.DATE, startMonthCal.getActualMinimum(Calendar.DAY_OF_MONTH));
        startMonthCal.set(Calendar.HOUR_OF_DAY, 0);
        startMonthCal.set(Calendar.MINUTE, 0);
        startMonthCal.set(Calendar.SECOND, 0);
        startMonthCal.set(Calendar.MILLISECOND, 0);
        return startMonthCal.getTime();
    }

    public static Bitmap rotateBitmap(String path) {
        ExifInterface exif;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
            return BitmapFactory.decodeFile(path);
        }
        int o = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);
        int orientation = 0;
        switch (o) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                orientation = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                orientation = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                orientation = 270;
                break;
        }
        Bitmap b = BitmapFactory.decodeFile(path);
        Matrix matrix = new Matrix();
        matrix.postRotate(orientation);
        return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
    }

    public static int getImageOrientation(String path) throws IOException {
        ExifInterface exif = new ExifInterface(path);
        int o = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);
        switch (o) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
        }
        return 0;
    }

    public static String getDefaultVal(String str, String compVal, String defaultVal) {
        if (str == null || str.length() == 0 || str.equals(compVal)) {
            return defaultVal;
        }
        return str;
    }

    public static String getDefaultVal(String str, String defaultVal) {
        if (str == null || str.length() == 0) {
            return defaultVal;
        }
        return str;
    }

    public static Date formatDate(String strDate, String format) throws ParseException {
        strDate = strDate.replace('-', '/');
        DateFormat df = DateFormat.getDateInstance();
        df.setLenient(false);
        Date d = df.parse(strDate);
        d.setMonth(d.getMonth() + 1);
        return d;
    }

    public static boolean checkDate(String strDate) {
        if (strDate == null || strDate.length() != 10) {
            return false;
        }
        strDate = strDate.replace('-', '/');
        DateFormat format = DateFormat.getDateInstance();
        // 日付/時刻解析を厳密に行うかどうかを設定する。
        format.setLenient(false);
        try {
            format.parse(strDate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static long getPrevMonthDateMillis() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && cm.getActiveNetworkInfo().isConnected();
    }
}
