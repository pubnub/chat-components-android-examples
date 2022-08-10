package com.pubnub.components.example.getting_started

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.components.example.getting_started.ui.theme.AppTheme

class LoginActivity : ComponentActivity() {

    private lateinit var pubNub: PubNub


    @OptIn(ExperimentalGraphicsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePubNub()
        setContent {
            var login by rememberSaveable { mutableStateOf("") }
            val pass by rememberSaveable { mutableStateOf("") }
            AppTheme(pubNub = pubNub) {
                Box {
                    var visible by remember { mutableStateOf(false) }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(top = 50.dp)
                                .size(140.dp)
                                .align(Alignment.CenterHorizontally),
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "logo"
                        )
                        Text(
                            text = "Log in",
                            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(start = 24.dp, bottom = 24.dp)
                        )
                        Text(
                            text = "Username",
                            style = TextStyle(fontSize = 16.sp),
                            modifier = Modifier.padding(start = 24.dp)
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(start = 24.dp, bottom = 24.dp, end = 24.dp)
                                .fillMaxWidth(),
                            value = login,
                            onValueChange = {
                                login = it
                            },
                            leadingIcon = {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_login),
                                    contentDescription = "logo"
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color.hsl(2F, 0.72F, 0.53F, 1F),
                                unfocusedBorderColor = Color.hsl(0F, 0F, 0.80F, 1F),
                                backgroundColor = Color.White
                            ),
                            maxLines = 1
                        )
                        Text(
                            text = "Password",
                            style = TextStyle(fontSize = 16.sp),
                            modifier = Modifier.padding(start = 24.dp)
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(start = 24.dp, bottom = 18.dp, end = 24.dp)
                                .fillMaxWidth(),
                            value = pass,
                            onValueChange = {
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color.hsl(2F, 0.72F, 0.53F, 1F),
                                unfocusedBorderColor = Color.hsl(0F, 0F, 0.80F, 1F),
                                backgroundColor = Color.White
                            ),
                            leadingIcon = {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_password),
                                    contentDescription = "logo"
                                )
                            },
                            maxLines = 1
                        )

                        Column(
                            modifier = Modifier
                                .size(width = 420.dp, height = 48.dp)
                                .padding(start = 24.dp, end = 24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                modifier = Modifier.fillMaxSize(),
                                onClick = {
                                    when (login) {
                                        "lukeyoung" -> {
                                            visible = false
                                            openChannelActivity(
                                                "601f9324",
                                                "Select Doctor to talk with"
                                            )
                                        }
                                        "suejones" -> {
                                            visible = false
                                            openChannelActivity(
                                                "0948aa84",
                                                "Select Patient to talk with"
                                            )
                                        }
                                        "saraflores" -> {
                                            visible = false
                                            openChannelActivity(
                                                "0222c484",
                                                "Select Doctor to talk with"
                                            )
                                        }
                                        else -> {
                                            visible = true
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(20), // = 50% percent
                                // or shape = CircleShape
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.hsl(
                                        196F,
                                        0.65F,
                                        0.57F,
                                        1F
                                    )
                                )
                            ) {
                                Text(
                                    text = "Log in",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(
                                initialAlpha = 0.4f
                            ),
                            exit = fadeOut(
                                animationSpec = tween(durationMillis = 250)
                            )
                        ) {
                            Row(modifier = Modifier.padding(top = 12.dp, start = 24.dp)) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_icon),
                                    contentDescription = "sd",
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                Text(
                                    text = "This username is not recognized, please check valid accounts on the Demo page",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color.hsl(2F, 0.72F, 0.53F, 1F)
                                    ),
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }

                    }
                    Box(
                        modifier = Modifier
                            .background(Color.hsl(0F, 0F, 0.96F, 1F))
                            .align(
                                Alignment.BottomCenter
                            )
                    ) {

                        Text(
                            modifier = Modifier
                                .padding(top = 50.dp, start = 72.dp, end = 72.dp),
                            textAlign = TextAlign.Center, text = textResource(
                                R.string.password_info
                            )
                        )
                    }
                }
            }
        }
    }

    @Composable
    @ReadOnlyComposable
    fun textResource(@StringRes id: Int): String =
        LocalContext.current.resources.getText(id).toString()

    private fun openChannelActivity(uuid: String, type: String) {
        val intent =
            Intent(this@LoginActivity, ChannelActivity::class.java).apply {
                putExtra("uuid", uuid)
                putExtra("type", type)
            }
        startActivity(intent)
    }

    private fun initializePubNub() {
        pubNub = PubNub(
            PNConfiguration(userId = UserId("1")).apply {
                publishKey = BuildConfig.PUBLISH_KEY
                subscribeKey = BuildConfig.SUBSCRIBE_KEY
                logVerbosity = PNLogVerbosity.NONE
            }
        )
    }
}