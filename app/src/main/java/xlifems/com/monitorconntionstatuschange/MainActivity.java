package xlifems.com.monitorconntionstatuschange;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.browse.MediaBrowser;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    // para versiones mayores a 24
    private ConnectivityManager.NetworkCallback mNetworkCallback ;
    private ConnectivityManager mConnectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

    }

    // Para versiones menores a 24, crea un receiver para procesar los cambios.
    public BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnected() &&
                    activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;

            String connectionMsg = isConnected ? "Red disponible" : "Red no disponible";
            Log.d("MainActivity", connectionMsg);

        }
    };

    // para versiones mayores a 24
    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mNetworkCallback = new ConnectivityManager.NetworkCallback(){
                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    Toast.makeText(MainActivity.this,
                            "Conexión disponible",
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onLost(Network network) {
                    super.onLost(network);
                    Toast.makeText(MainActivity.this,
                            "Conexión perdida",
                            Toast.LENGTH_LONG).show();
                }
            };

            mConnectivityManager.registerDefaultNetworkCallback(mNetworkCallback);
        }
    }

    // Al final elimino el registro para no recibir más llamadas en el método equivalente onStop():
    @Override
    protected void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mConnectivityManager.unregisterNetworkCallback(mNetworkCallback);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        //registerReceiver(connectionReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(connectionReceiver);
    }


}
