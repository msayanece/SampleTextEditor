package com.sayan.sample.sampletexteditor;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;

public class ReadWriteFileUtil {

    public static void readFile(Activity activity, String fileName, ReadCallback readCallback){
        ReadFileAsync readFileAsync = new ReadFileAsync(activity, readCallback);
        readFileAsync.execute(fileName);
    }

    public static void writeFile(Activity activity, String data, String fileName, WriteCallback writeCallback){
        WriteFileAsync writeFileAsync = new WriteFileAsync(activity, writeCallback);
        writeFileAsync.execute(data, fileName);

    }

    private static class ReadFileAsync extends AsyncTask<String, Void, String> {

        WeakReference<Activity> activityWeakReference;
        WeakReference<ReadCallback> readCallbackWeakReference;

        private ReadFileAsync(Activity activity, ReadCallback readCallback) {
            activityWeakReference = new WeakReference<>(activity);
            readCallbackWeakReference = new WeakReference<>(readCallback);
        }

        @Override
        protected String doInBackground(String... strings) {
            return readFromFile(activityWeakReference.get(), strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            readCallbackWeakReference.get().onSuccess(s);
        }

        private String readFromFile(Context context, String name) {
            InputStream inputStream = null;
            String ret = "";

            try {
                inputStream = context.openFileInput(name);

                if ( inputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        stringBuilder.append(receiveString);
                        stringBuilder.append("\n");
                    }

                    ret = stringBuilder.toString();
                }
            }
            catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return ret;
        }
    }

    private static class WriteFileAsync extends AsyncTask<String, Void, Boolean> {

        WeakReference<Activity> activityWeakReference;
        WriteCallback writeCallback;

        private WriteFileAsync(Activity activity, WriteCallback writeCallback) {
            activityWeakReference = new WeakReference<>(activity);
            this.writeCallback = writeCallback;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            writeToFile(activityWeakReference.get(), strings[0], strings[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean isSuccessful) {
            super.onPostExecute(isSuccessful);
            writeCallback.onSuccess();
        }

        private void writeToFile(Context context, String data, String name) {
            OutputStreamWriter outputStreamWriter = null;
            try {
                outputStreamWriter = new OutputStreamWriter(context.openFileOutput(name, Context.MODE_PRIVATE));
                if (outputStreamWriter != null) {
                    outputStreamWriter.write(data);
                }

            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }
            finally {
                try {
                    if (outputStreamWriter != null) {
                        outputStreamWriter.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    interface ReadCallback{
        void onSuccess(String s);
    }

    interface WriteCallback{
        void onSuccess();
    }
}
