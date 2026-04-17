package com.discovpn.app.vpn

sealed class VpnState(val label: String) {
    data object Idle : VpnState("Idle")
    data object PermissionRequired : VpnState("Permission required")
    data object Connected : VpnState("Connected")
    data class Error(val message: String) : VpnState("Error: $message")
}
