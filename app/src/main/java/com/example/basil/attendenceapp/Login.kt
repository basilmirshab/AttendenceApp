package com.example.basil.attendenceapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import com.example.basil.attendenceapp.Event.Event
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONObject

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun Clear(view: View) {
        username.setText("")
        password.setText("")

    }

    fun login(view: View) {
        if (username.text.toString().isEmpty() || password.text.toString().isEmpty())
            toast("invalid UserName or Password")
        else {
            userlogin()
        }
    }

    fun userlogin() {
        progressBar.visibility = View.VISIBLE
        doAsync {
            val body = FormBody.Builder()
                    .add("username", username.text.toString())
                    .add("password", password.text.toString()).build()
            val req = Request.Builder().url("https://test3.htycoons.in/api/login").post(body).build()
            val client = OkHttpClient()
            val res = client.newCall(req).execute()
            uiThread {
                when (res.code()) {
                    200 -> {
                        if (res.body() != null) {
                            val jsonRes = JSONObject(res.body()!!.string())
                            val token = jsonRes.getString("access_token")
                            val pref = getSharedPreferences("eventToken", 0)
                            pref.edit().putString("access_token", token).apply()
                            startActivity(intentFor<Event>())

                            finish()
                        }
                    }
                    400 -> {
                        AlertDialog.Builder(this@Login)
                                .setTitle("Error")
                                .setMessage("an error is occourd")
                                .setNeutralButton("ok"){dialog, which ->
                                    dialog.dismiss()
                                }.show()
                    }

                    404 -> {

                    }
                }
            }

        }

    }
}
