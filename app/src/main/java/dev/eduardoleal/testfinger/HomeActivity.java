package dev.eduardoleal.testfinger;

import android.app.KeyguardManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class HomeActivity extends AppCompatActivity {

    private KeyStore keyStore;
    private static final String KEY_NAME = "my_key";
    private Cipher cipher;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(ContextCompat.getColor(this ,R.color.purple));
        getSupportActionBar().hide();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Cesar Alvarez Salas | ITS | 1748577", Snackbar.LENGTH_LONG)
                        .setAction("Info", null).show();
            }
        });


        TextView txt_bluetooth = (TextView) findViewById(R.id.press_bluetooth);
        txt_bluetooth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                KeyguardManager keyguardManager = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
                FingerprintManager fingerprintManager = (FingerprintManager)getSystemService(FINGERPRINT_SERVICE);

                if(!fingerprintManager.isHardwareDetected())
                    Toast.makeText(HomeActivity.this, "Fingerprint authentication permission not enable", Toast.LENGTH_SHORT).show();
                else
                {
                    if(!fingerprintManager.hasEnrolledFingerprints())
                        Toast.makeText(HomeActivity.this, "Register at least one fingerprint in Settings", Toast.LENGTH_SHORT).show();
                    else{
                        if(!keyguardManager.isKeyguardSecure())
                            Toast.makeText(HomeActivity.this, "Lock screen security not enabled in Settings", Toast.LENGTH_SHORT).show();
                        else
                            genKey();

                        if(cipherInit())
                        {
                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                            FingerprintBluetooth helper = new FingerprintBluetooth(HomeActivity.this);
                            helper.startAuthentication(fingerprintManager,cryptoObject);
                        }
                    }
                }
            }
        });

        TextView txt_wifi = (TextView) findViewById(R.id.press_wifi);
        txt_wifi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                KeyguardManager keyguardManager = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
                FingerprintManager fingerprintManager = (FingerprintManager)getSystemService(FINGERPRINT_SERVICE);

                if(!fingerprintManager.isHardwareDetected())
                    Toast.makeText(HomeActivity.this, "Fingerprint authentication permission not enable", Toast.LENGTH_SHORT).show();
                else
                {
                    if(!fingerprintManager.hasEnrolledFingerprints())
                        Toast.makeText(HomeActivity.this, "Register at least one fingerprint in Settings", Toast.LENGTH_SHORT).show();
                    else{
                        if(!keyguardManager.isKeyguardSecure())
                            Toast.makeText(HomeActivity.this, "Lock screen security not enabled in Settings", Toast.LENGTH_SHORT).show();
                        else
                            genKey();

                        if(cipherInit())
                        {
                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                            FingerprintWifi helper = new FingerprintWifi(HomeActivity.this);
                            helper.startAuthentication(fingerprintManager,cryptoObject);
                        }
                    }
                }
            }
        });


        TextView txt_volume_off = (TextView) findViewById(R.id.press_volume_off);
        txt_volume_off.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                KeyguardManager keyguardManager = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
                FingerprintManager fingerprintManager = (FingerprintManager)getSystemService(FINGERPRINT_SERVICE);

                if(!fingerprintManager.isHardwareDetected())
                    Toast.makeText(HomeActivity.this, "Fingerprint authentication permission not enable", Toast.LENGTH_SHORT).show();
                else
                {
                    if(!fingerprintManager.hasEnrolledFingerprints())
                        Toast.makeText(HomeActivity.this, "Register at least one fingerprint in Settings", Toast.LENGTH_SHORT).show();
                    else{
                        if(!keyguardManager.isKeyguardSecure())
                            Toast.makeText(HomeActivity.this, "Lock screen security not enabled in Settings", Toast.LENGTH_SHORT).show();
                        else
                            genKey();

                        if(cipherInit())
                        {
                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                            FingerprintVolumeOff helper = new FingerprintVolumeOff(HomeActivity.this);
                            helper.startAuthentication(fingerprintManager,cryptoObject);
                        }
                    }
                }
            }
        });


        TextView txt_share = (TextView) findViewById(R.id.press_share);
        txt_share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Desarrollo de Aplicaciones Moviles");
                builder.setMessage("Uso de hardware y sensores digitales para acciones en dispositivos físicos");
                builder.setPositiveButton("Aceptar", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    private boolean cipherInit() {

        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES+"/"+KeyProperties.BLOCK_MODE_CBC+"/"+KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey)keyStore.getKey(KEY_NAME,null);
            cipher.init(Cipher.ENCRYPT_MODE,key);
            return true;
        } catch (IOException e1) {

            e1.printStackTrace();
            return false;
        } catch (NoSuchAlgorithmException e1) {

            e1.printStackTrace();
            return false;
        } catch (CertificateException e1) {

            e1.printStackTrace();
            return false;
        } catch (UnrecoverableKeyException e1) {

            e1.printStackTrace();
            return false;
        } catch (KeyStoreException e1) {

            e1.printStackTrace();
            return false;
        } catch (InvalidKeyException e1) {

            e1.printStackTrace();
            return false;
        }

    }

    private void genKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC).setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build()
            );
            keyGenerator.generateKey();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (InvalidAlgorithmParameterException e ){
            e.printStackTrace();
        }
    }

}
