package hu.bme.aut.bkknotifier.network

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.RingtoneManager
import android.os.IBinder
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import hu.bme.aut.bkknotifier.R
import hu.bme.aut.bkknotifier.feature.details.departure.DetailsDepartureFragment
import hu.bme.aut.bkknotifier.feature.stop.StopActivity
import hu.bme.aut.bkknotifier.feature.stop.StopAdapter
import hu.bme.aut.bkknotifier.feature.stop.data.Alert
import hu.bme.aut.bkknotifier.feature.stop.data.StopDatabase
import hu.bme.aut.bkknotifier.model.StopData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.thread


class DataService : Service() {
    private lateinit var database: StopDatabase
    private val channelId = "bkknotifier"
    private val channelName = "bkknotifier"
    private val notificationId = 221
    private lateinit var notificationManager: NotificationManager
    private val shortPollInterval: Long = 10000

    companion object {
        const val   ActionTag = "DataActionTag"
    }
    private val activeTimers: HashMap<String, Timer> = hashMapOf()
    private val tempTimerIDs: ArrayList<String> = arrayListOf()
    private val toggleSignalReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val extra = intent?.getStringExtra("id")
            if(extra != null) removeTimer(extra)
        }
    }
    private val stopActiveReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val extra = intent?.getStringExtra("id")
            if(extra != null) removeTimer(extra)
        }
    }

    fun removeTimer(id: String?) {
        if(id != null){
            if(!tempTimerIDs.contains(id)){
                activeTimers[id]?.cancel()
                activeTimers.remove(id)
            } else {
                tempTimerIDs.remove(id)
            }

            if(activeTimers.isEmpty() && tempTimerIDs.isEmpty()){
                stopSelf()
            }
        }


    }

    override fun onCreate() {
        super.onCreate()
        val channel =
            NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
            channel
        )

        IntentFilter(StopAdapter.ActionTag).also {
            registerReceiver(toggleSignalReceiver, it)
        }
        IntentFilter(DetailsDepartureFragment.ActionTag).also {
            registerReceiver(stopActiveReceiver, it)
        }
        database = StopDatabase.getDatabase(applicationContext)
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(toggleSignalReceiver)
        unregisterReceiver(stopActiveReceiver)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val stopId = intent?.getStringExtra("id")
        if(stopId != null){
            if(!activeTimers.containsKey(stopId)){
                activeTimers[stopId] = fixedRateTimer("updateData", true, 0L, shortPollInterval) {
                    loadStopData(stopId)
                }
            } else {
                tempTimerIDs.add(stopId)
            }
        }


        val notificationIntent = Intent(this, StopActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Data Service")
            .setContentText("Data service running in the background")
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        return START_STICKY
    }

    private fun loadStopData(stopId: String){
        NetworkManager.getStopData(stopId)?.enqueue(object : Callback<StopData?> {

            override fun onResponse(
                call: Call<StopData?>,
                response: Response<StopData?>,
            ) {
                if (response.isSuccessful) {
                    processData(response.body(), stopId)
                    Log.d("DataService", response.message())
                } else {
                    Toast.makeText(this@DataService, "Error: " + response.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(
                call: Call<StopData?>,
                throwable: Throwable,
            ) {
                throwable.printStackTrace()
            }
        })
    }

    private fun processData(stopData: StopData?, stopId: String){
        val intentSent = Intent()
        intentSent.action = stopId
        intentSent.putExtra("DATA", (stopData as Serializable))
        sendBroadcast(intentSent)
        thread{
            val alerts = database.stopDao().getStop(stopId).alerts
            for (alert in alerts) {
                if (satisfiesAlert(stopData, alert)) {
                    sendNotification(alert)
                }
            }
        }
    }

    private fun sendNotification(alert: Alert) {
        val notifications: Array<StatusBarNotification> = notificationManager.activeNotifications
        for (notification in notifications) {
            if (notification.id == 221) {
                return
            }
        }
        notificationManager.sendNotification(getString(R.string.alert_notif, alert.shortName, alert.time), applicationContext)
    }

    private fun satisfiesAlert(stopData: StopData, alert: Alert): Boolean {
        for(stopTime in stopData.data?.entry?.stopTimes!!){
            val trip = stopData.data?.references?.trips?.get(stopTime.tripId)
            val route = stopData.data?.references?.routes?.get(trip?.routeId)
            if(route?.shortName.equals(alert.shortName)){
                val arrivalTime = (stopData.currentTime?.div(1000)?.let {
                    (stopTime.predictedDepartureTime ?: stopTime.departureTime)?.minus(it)?.div(60)?.plus(1)
                })
                if (arrivalTime != null) {
                    if ((arrivalTime.toInt() == alert.time)){
                        return true
                    }
                }
            }
        }
        return false
    }


    private fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
        val contentIntent = Intent(applicationContext, StopActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            notificationId,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(
            applicationContext,
            channelId
        )
            .setSmallIcon(R.drawable.ic_trashcan)
            .setContentTitle(getString(R.string.alert_title))
            .setContentText(messageBody)
            .setContentIntent(contentPendingIntent)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setAutoCancel(true)
            .setSilent(false)
        try {
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(applicationContext, uri)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        notify(notificationId, builder.build())
    }
}