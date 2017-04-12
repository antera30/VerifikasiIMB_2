package com.example.fajar.verifikasiimb.utils;

/**
 * Created by Fajar on 4/12/2017.
 */

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


public class Utils {

    // TODO: group with inner classes

    public static final String TAG = "Utils";
    private static final String DATABASE_NAME = "ptbbts.db";
    public static final String CACHE_DIR_NAME = "TUGAS_AKHIR";

    public static final String PATH_DATABAS = "/data/data/bbt.progsby.com.pt_bbt/ptbbts.db";

    public enum VideoQuality {MOBILE, SD, HD}

    ;
    public static final float BITMAP_SCALE = 0.4f;
    public static final float BLUR_RADIUS = 7.5f;
    private static File cacheDir = null;
    private static boolean cacheDirCreated = false;
    public static Typeface fontsStyle;
    private static SimpleDateFormat srcFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static SimpleDateFormat srcDate = new SimpleDateFormat("yyyy-MM-dd");

    // ------------------------------ API --------------------------------------

    public static String validateShortcutOrId(final String shortcut) {
        if (!shortcut.matches("^[\\d\\w_]+$"))
            throw new IllegalArgumentException("Not correct schortcut or _ID: " + shortcut);
        return shortcut;
    }

    public static String validateId(final String id) {
        if (!id.matches("^\\d+$")) throw new IllegalArgumentException("Not correct _ID: " + id);
        return id;
    }

    public static String validateActionName(String action) {
        if (!action.matches("^[\\w_]+$"))
            throw new IllegalArgumentException("Not correct action name: " + action);
        return action;
    }

    public static String authorIdFromProfileUrl(String uploaderProfileUrl) {
        return uploaderProfileUrl.substring(17);
    }

    // ------------------------------ Adapt / Format ---------------------------

    public static String crop(String value, int howMuch) {
        return (value.length() <= howMuch) ? value : (value.substring(0, howMuch - 3) + "...");
    }

    public static String quantity(Context context, int resId, int quantity) {
        return context.getResources().getQuantityString(resId, quantity, quantity);
    }

    /* public static String format(String source, String... params) {
        String result = source;
        int pos = 0;
        while (pos < params.length) {
            result = result.replaceAll("\\{" + params[pos++] + "\\}", params[pos++]);
        }
        return result;
    } */

    public static String adaptDuration(long duration) {
        final long remainder = duration % 60;
        return ((duration - remainder) / 60) + ":" + ((remainder < 10) ? ("0" + remainder) : remainder);
    }

    public static boolean adaptBoolean(int value) {
        return (value == 0) ? false : true;
    }

    // gets date in format yyyy-MM-dd hh:mm:ss
    // returns in dd MMM yyyy hh:mm format
    public static String TimeDate(String date) {
        try {
            return DateFormat.format("dd MMM yyyy kk:mm", srcFormat.parse(date)).toString();
        } catch (ParseException e) {
            return "## #### #### ##:##";
        }
    }

    public static String Date(String date) {
        try {
            return DateFormat.format("dd MMM yyyy", srcDate.parse(date)).toString();
        } catch (ParseException e) {
            return "## #### ####";
        }
    }

    public static String[] extractTags(String source) {
        final List<String> result = new LinkedList<String>();
        for (String tag : source.split(",")) {
            if (tag.trim().length() > 0) result.add(tag.trim());
        }
        return result.toArray(new String[result.size()]);
    }

    public static String adaptTags(String[] tags, String noneText, String delimiter) {
        if (tags.length == 0) return noneText;
        if (tags.length == 1) return tags[0];
        final StringBuffer result = new StringBuffer();
        for (int i = 0; i < (tags.length - 1); i++) {
            result.append(tags[i]).append(delimiter);
        }
        result.append(tags[tags.length - 1]);
        return result.toString();
    }

    public static String adaptTags(String[] tags, String noneText) {
        return adaptTags(tags, noneText, " / ");
    }

    public static String[] stringArrayFromJson(JSONArray jsonArr) throws JSONException {
        final String[] array = new String[jsonArr.length()];
        for (int i = 0; i < jsonArr.length(); i++) {
            array[i] = jsonArr.getString(i);
        }
        return array;
    }

    // ------------------------------ Network ----------------------------------

    public static int lookupHost(String hostname) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            return -1;
        }
        byte[] addrBytes;
        int addr;
        addrBytes = inetAddress.getAddress();
        addr = ((addrBytes[3] & 0xff) << 24)
                | ((addrBytes[2] & 0xff) << 16)
                | ((addrBytes[1] & 0xff) << 8)
                | (addrBytes[0] & 0xff);
        return addr;
    }

    // ------------------------ Files: Streams / Cache -------------------------

    public static void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public static void copyFile(File oldLocation, File newLocation) throws IOException {
        if (oldLocation.exists()) {
            BufferedInputStream reader = new BufferedInputStream(new FileInputStream(oldLocation));
            BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(newLocation, false));
            try {
                byte[] buff = new byte[8192];
                int numChars;
                while ((numChars = reader.read(buff, 0, buff.length)) != -1) {
                    writer.write(buff, 0, numChars);
                }
            } catch (IOException ex) {
                throw new IOException("IOException when transferring " + oldLocation.getPath() + " to " + newLocation.getPath());
            } finally {
                try {
                    if (reader != null) {
                        writer.close();
                        reader.close();
                    }
                } catch (IOException ex) {
                    Log.e(TAG, "Error closing files when transferring " + oldLocation.getPath() + " to " + newLocation.getPath());
                }
            }
        } else {
            throw new IOException("Old location does not exist when transferring " + oldLocation.getPath() + " to " + newLocation.getPath());
        }
    }

    public static File createCacheDir(Context context, String dirName) {
        File preparedDir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            preparedDir = context.getDir(dirName /* + UUID.randomUUID().toString()*/, Context.MODE_PRIVATE);
            Log.i(TAG, "Cache dir initialized at SD card " + preparedDir.getAbsolutePath());
        } else {
            preparedDir = context.getCacheDir();
            Log.i(TAG, "Cache dir initialized at phone storage " + preparedDir.getAbsolutePath());
        }
        if (!preparedDir.exists()) {
            Log.i(TAG, "Cache dir not existed, creating");
            preparedDir.mkdirs();
        }
        return preparedDir;
    }

    public static File getDefaultCacheDir(Context context) {
        if (cacheDirCreated) return cacheDir;
        else {
            cacheDir = createCacheDir(context, CACHE_DIR_NAME);
            cacheDirCreated = true;
            return cacheDir;
        }
    }

    public static File newTempFile(Context context, String prefix, String suffix) throws IOException {
        return File.createTempFile(prefix, suffix, getDefaultCacheDir(context));
    }

    public static long computeFreeSpace() {
        File dataDir = Environment.getDataDirectory();
        StatFs stat = new StatFs(dataDir.getPath());
        return stat.getAvailableBlocks() * stat.getBlockSize();
    }

    // ------------------------ Views ------------------------------------------

    public static View getItemViewIfVisible(AdapterView<?> holder, int itemPos) {
        int firstPosition = holder.getFirstVisiblePosition();
        int wantedChild = itemPos - firstPosition;
        if (wantedChild < 0 || wantedChild >= holder.getChildCount()) return null;
        return holder.getChildAt(wantedChild);
    }

    public static void invalidateByPos(AdapterView<?> parent, int position) {
        final View itemView = getItemViewIfVisible(parent, position);
        Log.d(TAG, "Trying to invalidate view " + itemView + " at pos " + position + " ");
        if (itemView != null) itemView.invalidate();
    }

    public static void forcePostInvalidate(AdapterView<?> parent, int position) {
        final View itemView = getItemViewIfVisible(parent, position);
        if (itemView != null) itemView.postInvalidate();
    }


    public static void TypeFace(TextView tv, AssetManager asm, String fnt) {

        fontsStyle = Typeface.createFromAsset(asm, fnt + ".ttf");
        tv.setTypeface(fontsStyle);
    }

    private static long timeStringtoMilis(String time) {
        long milis = 0;

        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sd.parse(time);
            milis = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return milis;
    }


    public static String getSettingTime(Context mContext) {

        String timeSettings = android.provider.Settings.System.getString(
                mContext.getContentResolver(),
                android.provider.Settings.System.AUTO_TIME);
        if (timeSettings.contentEquals("0")) {
            android.provider.Settings.System.putString(
                    mContext.getContentResolver(),
                    android.provider.Settings.System.AUTO_TIME, "1");
        }
        Date now = new Date(System.currentTimeMillis());
        Log.d("Date", now.toString());
        return now.toString();

    }


    public static String getFilename(String snameimage, String Folder) {

        File file = new File(Environment.getExternalStorageDirectory().getPath(), "TUGAS AKHIR/" + Folder);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + snameimage + ".jpg");

        return uriSting;

    }

    public static String WriteFilePdf(String namfile, String Folder) {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "PTBBT/" + Folder);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + namfile + ".pdf");

        return uriSting;

    }

    public static String WriteDatabase(String namfile, String Folder) {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "PTBBT/" + Folder);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + namfile);

        return uriSting;

    }


    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


    public static String getDateTimeTreeMonth() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 3);
        Date date = cal.getTime();
        return dateFormat.format(date);
    }

    public static String getDateTimeOri() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getDateTimeer() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getDateTimeSql() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getDatePending() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getDateTimeNameFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Copies the database file at the specified location
     * over the current internal application database.
     */
    private void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }

    public static void exportDB(Context context, String sdbname) {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + "ptbbt.progsby.com.ptbbt" + "/databases/" + "ptbbts.db";
        String backupDBPath = "PTBBT/BackUp/" + sdbname;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(context, "Backup Sucess", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Backup Gagal Silahkan Coba Lagi", Toast.LENGTH_LONG);

        }
    }

    public static void importDB(Context context, String sdbname) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "/data/" + "ptbbt.progsby.com.ptbbt" + "/databases/" + "ptbbts.db";
                String backupDBPath = "PTBBT/BackUp/" + sdbname; // From SD directory.
                File backupDB = new File(data, currentDBPath);
                File currentDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(context, "Restore Sucess", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Restore Gagal Silahkan Coba Lagi", Toast.LENGTH_LONG).show();

        }
    }

//----------------------------------------------- WRITE EXTERNAL -------------------------------------------------

    public static void checkExternalMedia() {
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

        Log.i("ile written to", "External Media: readable="
                + mExternalStorageAvailable + " writable=" + mExternalStorageWriteable);
    }

    /**
     * Method to write ascii text characters to file on SD card. Note that you must add a
     * WRITE_EXTERNAL_STORAGE permission to the manifest file or this method will throw
     * a FileNotFound Exception because you won't have write permission.
     */

    public static void WriteBug(String sbug, String sname) {

        // Find the root of the external storage.
        // See http://developer.android.com/guide/topics/data/data-storage.html#filesExternal

        File root = Environment.getExternalStorageDirectory();
        Log.i("External", root.toString());

        // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

        File dir = new File(root.getAbsolutePath() + "/PTBBT/BackUp/");
        dir.mkdirs();
        File file = new File(dir, sname);

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println(sbug);
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("ile written to", file.toString());
    }

    /**
     * Method to read in a text file placed in the res/raw directory of the application. The
     * method reads in all lines of the file sequentially.
     */


    public static void writeToSDFile(String sbug) {

        // Find the root of the external storage.
        // See http://developer.android.com/guide/topics/data/data-storage.html#filesExternal

        File root = Environment.getExternalStorageDirectory();
        Log.i("External", root.toString());

        // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

        File dir = new File(root.getAbsolutePath() + "/PTBBT/BackUp/");
        dir.mkdirs();
        File file = new File(dir, "Log.txt");

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println(sbug);
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("ile written to", file.toString());
    }

    /** Method to read in a text file placed in the res/raw directory of the application. The
     method reads in all lines of the file sequentially. */


}


