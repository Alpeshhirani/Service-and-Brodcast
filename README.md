# Service-and-Brodcast
# Manifest
``````````
 <receiver
            android:name=".Service.ConnectivityChangeReceiver"
            android:enabled="true"
            android:exported="true">

            <intent-filter>
                <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />

            </intent-filter>
        </receiver>
        
<service android:name=".Service.InternetService"></service>
