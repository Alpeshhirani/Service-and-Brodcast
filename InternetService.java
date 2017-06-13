package com.weapplinse.xitix.Service;


import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.weapplinse.xitix.Activity.Preferences;
import com.weapplinse.xitix.Utils.commankey;
import com.weapplinse.xitix.Webservice.WebserviceCall;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class InternetService extends Service {
    Preferences pref;
    WebserviceCall com;
    ArrayList<String> StringKeyArray = new ArrayList<String>();
    String namespace = "http://tempuri.org/";
    String SOAP_ACTION;
    SoapObject request = null;
    SoapSerializationEnvelope envelope;
    HttpTransportSE androidHttpTransport;
    JSONObject jsonobject;
    private String url = "http://staffappws.xitixworld.com/MyService.asmx";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void SetEnvelope() {

        try {
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            androidHttpTransport = new HttpTransportSE(url, 300000);

            androidHttpTransport.debug = true;

        } catch (Exception e) {
            System.out.println("Soap Exception---->>>" + e.toString());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        StringKeyArray.clear();

        StringKeyArray.addAll(pref.getListString(commankey.SavekeyArray));

        if (StringKeyArray.size() > 0) {

            new MarkStudAttd().execute("");

        }

        return Service.START_NOT_STICKY;

    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        pref = new Preferences(InternetService.this);

        com = new WebserviceCall();
    }

    private class MarkStudAttd extends AsyncTask<String, Void, Boolean> {
        String a;

        public MarkStudAttd() {

        }

        @Override
        protected Boolean doInBackground(String... params) {

            if (StringKeyArray.size() > 0) {
                try {

                    SOAP_ACTION = namespace + "MarkStudAttd";

                    request = new SoapObject(namespace, "MarkStudAttd");

                    request.addProperty("strJSON", "" + pref.getString(StringKeyArray.get(0)));
                    a = pref.getString(StringKeyArray.get(0));
                    SetEnvelope();

                    try {

                        androidHttpTransport.call(SOAP_ACTION, envelope);
                        Log.e("service", a);
                        if (StringKeyArray.size() > 0) {
                            pref.remove(StringKeyArray.get(0));

                            StringKeyArray.remove(0);

                            pref.remove(commankey.SavekeyArray);

                            pref.putListString(commankey.SavekeyArray, StringKeyArray);
                        }

                        jsonobject = new JSONObject(envelope.getResponse().toString());

                    } catch (Exception e) {

                    }

                } catch (Exception e) {

                }

            }

            return false;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.e("finishservice", a);
            if (jsonobject != null) {

                String msg = jsonobject.optString("ResponseMsg");

                if (msg.equals("Attendance Update Successfully")) {

                }

            }
            if (StringKeyArray.size() > 0) {
                StringKeyArray.clear();

                StringKeyArray.addAll(pref.getListString(commankey.SavekeyArray));
                if (StringKeyArray.size() > 0) {
                    new MarkStudAttd().execute("");
                } else {
                    stopService(new Intent(InternetService.this, InternetService.class));
                }


            } else {

                stopService(new Intent(InternetService.this, InternetService.class));

            }
        }
    }
}