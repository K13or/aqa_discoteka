package com.discovpn.app.vpn

interface VpnProfileRepository {
    fun getProfiles(): List<VpnProfile>
    fun getActiveProfile(): VpnProfile?
    fun setActiveProfile(profileId: String): VpnProfile?
}
