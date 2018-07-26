package com.example.basil.attendenceapp.Event

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.basil.attendenceapp.Login
import com.example.basil.attendenceapp.R

import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_event.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.uiThread
import org.json.JSONObject

class Event : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        doAsync {
            val pref = getSharedPreferences("event", 0)
            val token = pref.getString("access_token", "")
            val req = Request.Builder().url("https://test3.htycoons.in/api/list_events")
                    .header("Authorization", "Bearer $token")
                    .post(FormBody.Builder().build())
                    .build()
            val client = OkHttpClient()
            val res = client.newCall(req).execute()
            if (res.body() != null) {
                val responseString = res.body()!!.string()
                val json = JSONObject(responseString)
                val eventsJson = json.getJSONArray("events")
                val events = ArrayList<Events>()
                for (i in 0..eventsJson.length() - 1) {
                    val event = Events(
                            eventsJson.getJSONObject(i).getString("_id"),
                            eventsJson.getJSONObject(i).getString("event_name"),
                            eventsJson.getJSONObject(i).getString("date"),
                            eventsJson.getJSONObject(i).getString("venue"),
                            eventsJson.getJSONObject(i).getInt("days"),
                            eventsJson.getJSONObject(i).getInt("no_of_participants")
                    )
                }
                uiThread {
                    recycleview.layoutManager=LinearLayoutManager(this@Event,LinearLayoutManager.VERTICAL,false)
                    val adapter =Eventadapter(events)
                    recycleview.adapter=adapter

                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val mymenu = MenuInflater(this).inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.logout -> {
                getSharedPreferences("eventToken", 0)
                        .edit().putString("access_token", "").apply()
                startActivity(intentFor<Login>())
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
