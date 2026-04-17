package com.discovpn.app.vpn

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VpnViewModel(
    private val profileRepository: VpnProfileRepository = InMemoryVpnProfileRepository(),
    private val orchestrator: VpnConnectionOrchestrator = VpnConnectionOrchestrator(TunnelConfigValidator())
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        VpnUiState(
            profiles = profileRepository.getProfiles(),
            activeProfile = profileRepository.getActiveProfile(),
            vpnState = VpnState.Idle
        )
    )
    val uiState: StateFlow<VpnUiState> = _uiState.asStateFlow()

    fun selectProfile(profileId: String) {
        val selectedProfile = profileRepository.setActiveProfile(profileId) ?: return
        _uiState.update { current ->
            current.copy(activeProfile = selectedProfile)
        }
    }

    fun requestConnect(): VpnState {
        val profile = _uiState.value.activeProfile
            ?: return VpnState.Error("Select a VPN profile first").also(::updateVpnState)

        val result = orchestrator.requestConnect(
            TunnelConfig(
                serverAddress = profile.serverAddress,
                dnsServers = profile.dnsServers,
                appName = profile.name,
                mtu = profile.mtu
            )
        )
        updateVpnState(result)
        return result
    }

    fun markConnected() {
        updateVpnState(VpnState.Connected)
    }

    fun requestDisconnect() {
        updateVpnState(orchestrator.requestDisconnect())
    }

    private fun updateVpnState(vpnState: VpnState) {
        _uiState.update { current ->
            current.copy(vpnState = vpnState)
        }
    }
}
