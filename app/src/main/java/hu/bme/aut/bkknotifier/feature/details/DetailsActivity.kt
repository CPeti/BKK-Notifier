package hu.bme.aut.bkknotifier.feature.details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.tabs.TabLayoutMediator
import hu.bme.aut.bkknotifier.R
import hu.bme.aut.bkknotifier.databinding.ActivityDetailsBinding
import hu.bme.aut.bkknotifier.feature.stop.data.Stop
import hu.bme.aut.bkknotifier.model.StopData
import hu.bme.aut.bkknotifier.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class DetailsActivity : AppCompatActivity(), StopDataHolder {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var stop: Stop
    private var stopData: StopData? = null



    companion object{
        private const val TAG = "DetailsActivity"
        const val EXTRA_STOP = "EXTRA_STOP"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        stop = intent.getSerializableExtra("EXTRA_STOP") as Stop

        supportActionBar?.title = getString(R.string.stop, stop.name)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val detailsPagerAdapter = DetailsPagerAdapter(this)
        binding.mainViewPager.adapter = detailsPagerAdapter
    }

    override fun onResume() {
        super.onResume()
        val detailsPagerAdapter = DetailsPagerAdapter(this)
        binding.mainViewPager.adapter = detailsPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.mainViewPager) { tab, position ->
            tab.text = when(position) {
                0 -> getString(R.string.main)
                1 -> getString(R.string.details)
                else -> ""
            }
        }.attach()
        loadStopData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getStopData(): StopData? {
        return stopData
    }

    override fun loadStopData() {
        NetworkManager.getStopData(stop.id)?.enqueue(object : Callback<StopData?> {
            override fun onResponse(
                call: Call<StopData?>,
                response: Response<StopData?>
            ) {
                Log.d(TAG, "onResponse: " + response.code())
                if (response.isSuccessful) {
                    displayStopData(response.body())
                } else {
                    Toast.makeText(this@DetailsActivity, "Error: " + response.message(), Toast.LENGTH_LONG).show()
                    Log.e(TAG, response.toString())
                }
            }

            override fun onFailure(
                call: Call<StopData?>,
                throwable: Throwable
            ) {
                throwable.printStackTrace()
                Toast.makeText(this@DetailsActivity, "Network request error occurred, check LOG", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun getStop(): Stop {
        return stop
    }

    private fun displayStopData(receivedStopData: StopData?) {
        stopData = receivedStopData
        val intentSent = Intent()
        intentSent.action = stopData?.data?.entry?.stopId
        intentSent.putExtra("DATA", (stopData as Serializable))
        sendBroadcast(intentSent)
    }



}