package dev.eduardoleal.testfinger;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.provider.Settings;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;

public class FingerprintWifi extends FingerprintManager.AuthenticationCallback{

    private Context context;

    public FingerprintWifi(Context context) {
        this.context = context;
    }

    public void startAuthentication(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cenCancellationSignal = new CancellationSignal();
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
            return;
        fingerprintManager.authenticate(cryptoObject,cenCancellationSignal,0,this,null);
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        Toast.makeText(context, "Fingerprint Authentication failed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        //enable wifi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.startActivity(new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY));
        } else {
            WifiManager wifiManager =
                    (WifiManager)this.context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);
        }
    }
}

