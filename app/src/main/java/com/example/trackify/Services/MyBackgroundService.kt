import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyBackgroundService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service started")

        // Add your background service logic here

        // If the system kills the service, it will attempt to restart it
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        // We don't need to bind this service, so return null
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service destroyed")
    }

    companion object {
        private const val TAG = "MyBackgroundService"
    }
}
