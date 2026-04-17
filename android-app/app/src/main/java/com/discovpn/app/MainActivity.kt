package com.discovpn.app

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.discovpn.app.vpn.VpnProfile
import com.discovpn.app.vpn.AndroidVpnService
import com.discovpn.app.vpn.VpnViewModel
import com.discovpn.app.vpn.VpnState

class MainActivity : ComponentActivity() {

    private val viewModel: VpnViewModel by viewModels()

    private val vpnPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                startService(Intent(this, AndroidVpnService::class.java))
                viewModel.markConnected()
            } else {
                viewModel.requestDisconnect()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                MainScreen(
                    profiles = uiState.profiles,
                    activeProfile = uiState.activeProfile,
                    vpnState = uiState.vpnState,
                    onSelectProfile = viewModel::selectProfile,
                    onConnectClick = {
                        val nextState = viewModel.requestConnect()

                        if (nextState == VpnState.PermissionRequired) {
                            val intent = VpnService.prepare(this)
                            if (intent == null) {
                                startService(Intent(this, AndroidVpnService::class.java))
                                viewModel.markConnected()
                            } else {
                                vpnPermissionLauncher.launch(intent)
                            }
                        }
                    },
                    onDisconnectClick = {
                        stopService(Intent(this, AndroidVpnService::class.java))
                        viewModel.requestDisconnect()
                    }
                )
            }
        }
    }
}

private fun MainScreen(
    profiles: List<VpnProfile>,
    activeProfile: VpnProfile?,
    vpnState: VpnState,
    onSelectProfile: (String) -> Unit,
    onConnectClick: () -> Unit,
    onDisconnectClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "DiscoVPN",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Profile: ${activeProfile?.name ?: "None"}",
            modifier = Modifier.padding(top = 12.dp)
        )
        Text(
            text = "State: ${vpnState.label}",
            modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
        )

        profiles.forEach { profile ->
            Button(
                onClick = { onSelectProfile(profile.id) },
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text("Use ${profile.name}")
            }
        }

        Button(onClick = onConnectClick) {
            Text("Connect")
        }

        Button(
            onClick = onDisconnectClick,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text("Disconnect")
        }
    }
}
