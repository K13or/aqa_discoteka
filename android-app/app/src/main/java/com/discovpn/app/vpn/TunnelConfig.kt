package com.discovpn.app.vpn

data class TunnelConfig(
    val serverAddress: String,
    val dnsServers: List<String>,
    val appName: String,
    val mtu: Int = 1500
)
