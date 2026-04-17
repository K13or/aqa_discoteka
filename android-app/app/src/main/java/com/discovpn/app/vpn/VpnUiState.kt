package com.discovpn.app.vpn

data class VpnUiState(
    val profiles: List<VpnProfile> = emptyList(),
    val activeProfile: VpnProfile? = null,
    val vpnState: VpnState = VpnState.Idle
)
