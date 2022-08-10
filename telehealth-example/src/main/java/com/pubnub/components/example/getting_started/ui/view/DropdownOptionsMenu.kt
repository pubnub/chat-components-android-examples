package com.pubnub.components.example.getting_started.ui.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pubnub.components.example.getting_started.ChannelActivity
import com.pubnub.components.example.getting_started.LoginActivity
import com.pubnub.components.example.getting_started.R

@Composable
fun DropdownOptionsMenu(
    visible: Boolean,
    onDismiss: () -> Unit,
    context: Context,
    uuid: String?
) {

    val localFocusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    localFocusManager.clearFocus()
                })
            }
    ) {
        DropdownMenu(
            expanded = visible,
            onDismissRequest = onDismiss
        ) {

            DropdownMenuItem(onClick = {

                val intent = Intent(context, ChannelActivity::class.java).apply {
                    if (uuid.equals("0761cba4")) {
                        putExtra("uuid", "27b34954")
                        putExtra("type", "Select Patient to talk with")
                    } else {
                        putExtra("uuid", "0761cba4")
                        putExtra("type", "Select Doctor to talk with")
                    }
                }
                context.startActivity(intent)
            }) {
                Image(
                    modifier = Modifier
                        .size(14.dp),
                    painter = painterResource(id = R.drawable.switch_patient),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(4.dp))
                if (uuid.equals("0761cba4")) {
                    Text(text = "Swap to Doctor")
                } else {
                    Text(text = "Swap to Patient")
                }
            }
            DropdownMenuItem(onClick = {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }) {
                Image(
                    modifier = Modifier
                        .size(14.dp),
                    painter = painterResource(id = R.drawable.dark_theme),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Dark Theme")
            }
            DropdownMenuItem(onClick = {
                val intent = Intent(context, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)

            }) {
                Image(
                    modifier = Modifier
                        .size(14.dp),
                    painter = painterResource(id = R.drawable.logout),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Logout")
            }
        }
    }
}
