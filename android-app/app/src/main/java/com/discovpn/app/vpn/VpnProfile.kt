package com.discovpn.app.vpn

data class VpnProfile(
    val id: String,
    val name: String,
    val serverAddress: String,
    val dnsServers: List<String>,
    val mtu: Int = 1500
)
