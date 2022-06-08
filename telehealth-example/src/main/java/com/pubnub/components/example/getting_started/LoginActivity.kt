package com.pubnub.components.example.getting_started

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.models.consumer.objects.member.PNUUIDWithCustom
import com.pubnub.components.DefaultDatabase
import com.pubnub.components.PubNubDatabase
import com.pubnub.components.data.Database
import com.pubnub.components.data.membership.DBMembership
import com.pubnub.components.example.getting_started.dto.*
import com.pubnub.components.example.getting_started.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

class LoginActivity : ComponentActivity() {

    private lateinit var pubNub: PubNub


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePubNub()
        //initializeChannels()
        //initializePatients()
        //initializeDoctors()
        //initializeMemberships()
        setContent{
            AppTheme(pubNub = pubNub) {
                Column(modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(modifier = Modifier.size(160.dp), painter = painterResource(id = R.drawable.logo), contentDescription = "logo")
                    Text(text = "Login as:")
                    Row(modifier = Modifier.padding(top = 30.dp)) {
                        Column(modifier = Modifier.size(114.dp)
                            .border(1.dp, Color(0xFF4AB2D9), RoundedCornerShape(12.dp))
                            .clickable {
                                val intent = Intent(this@LoginActivity, ChannelActivity::class.java).apply {
                                    //putExtra(EXTRA_MESSAGE, message)
                                }
                                startActivity(intent)
                            },
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(modifier = Modifier.size(50.dp), painter = painterResource(id = R.drawable.patient), contentDescription = "logo")
                            Text(text = "Patient")
                        }

                        Spacer(modifier = Modifier.width(20.dp))
                        Column(modifier = Modifier.size(114.dp)
                            .border(1.dp, Color(0xFF4AB2D9), RoundedCornerShape(12.dp))
                            .clickable {
                                val intent = Intent(this@LoginActivity, ChatActivity::class.java).apply {
                                    //putExtra(EXTRA_MESSAGE, message)
                                }
                                startActivity(intent)
                            },
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(modifier = Modifier.size(50.dp), painter = painterResource(id = R.drawable.doctor), contentDescription = "logo")
                            Text(text = "Doctor")
                        }
                    }
                    Text(modifier = Modifier.padding(top = 50.dp, start = 72.dp, end = 72.dp),textAlign = TextAlign.Center,text = "Health Hub app is for testing and demonstration purposes only. This app does not contain real patient data.")

                }
            }
        }
    }

    private fun initializePubNub() {
        pubNub = PubNub(
            PNConfiguration(uuid = "myFirstUser").apply {
                publishKey = BuildConfig.PUBLISH_KEY
                subscribeKey = BuildConfig.SUBSCRIBE_KEY
                logVerbosity = PNLogVerbosity.NONE
            }
        )
        //val db = DefaultDatabase()
    }

    private fun initializeChannels() {
        val jsonFileString = getJsonDataFromAsset(applicationContext, "channels.json")
        val gson = Gson()
        val listPersonType = object : TypeToken<List<ChannelsItem>>() {}.type
        var channels: List<ChannelsItem> = gson.fromJson(jsonFileString, listPersonType)
        channels.forEach { channel ->
            pubNub.setChannelMetadata(
                name = channel.name,
                channel = channel.id!!,
                description = channel.description,
                custom = channel.custom
            ).async { result, status ->
                if (status.error) {
                    //handle error
                    Log.i("error", "error channels")
                } else if (result != null) {
                    Log.i("data", "success channels")
                //handle result
                }
            }
        }
    }

    private fun initializeDoctors() {
        val jsonFileString = getJsonDataFromAsset(applicationContext, "doctors.json")
        val gson = Gson()
        val listPersonType = object : TypeToken<List<DoctorsItem>>() {}.type
        var channels: List<DoctorsItem> = gson.fromJson(jsonFileString, listPersonType)
        channels.forEach { item ->
            pubNub.setUUIDMetadata(
                uuid = item.id,
                name = item.name,
                profileUrl = item.profileUrl,
                custom = item.custom,
            ).async { result, status ->
                if (status.error) {
                    //handle error
                    Log.i("error", "error doctors")
                } else if (result != null) {
                    Log.i("data", "success doctors")

                    //handle result
                }
            }
        }
    }
    private fun initializeMemberships() {
        val jsonFileString = getJsonDataFromAsset(applicationContext, "membership.json")
        val gson = Gson()
        val listPersonType = object : TypeToken<List<MembershipsItem>>() {}.type
        var channels: List<MembershipsItem> = gson.fromJson(jsonFileString, listPersonType)
        channels.forEach { channel ->
            var ids = mutableListOf<PNUUIDWithCustom>()
            channel.members?.forEach {
                if (it != null) {
                    ids.add(PNUUIDWithCustom(it))
                }
            }
        }
    }
    private fun initializePatients() {
        val jsonFileString = getJsonDataFromAsset(applicationContext, "patients.json")
        val gson = Gson()
        val listPersonType = object : TypeToken<List<PatientsItem>>() {}.type
        var channels: List<PatientsItem> = gson.fromJson(jsonFileString, listPersonType)
        channels.forEach { item ->
            pubNub.setUUIDMetadata(
                uuid = item.id,
                name = item.name,
                profileUrl = item.profileUrl,
                custom = item.custom,
            ).async { result, status ->
                if (status.error) {
                    //handle error
                    Log.i("error", "error patients")
                } else if (result != null) {
                    Log.i("data", "success patients")
                    //handle result
                }
            }
        }
    }

    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }



    }