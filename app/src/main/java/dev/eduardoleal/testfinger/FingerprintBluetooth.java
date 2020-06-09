package dev.eduardoleal.testfinger;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;


public class FingerprintBluetooth extends FingerprintManager.AuthenticationCallback{

    private Context context;

    public FingerprintBluetooth(Context context) {
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
        //enable
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter.enable()){
            adapter.enable();
            Toast.makeText(context, "El bluetooth ha sido encendido!", Toast.LENGTH_SHORT).show();
        }else{
            adapter.disable();
            Toast.makeText(context, "El bluetooth ha sido apagado!", Toast.LENGTH_SHORT).show();
        }
    }
}

