package com.discovpn.app.vpn

class InMemoryVpnProfileRepository(
    initialProfiles: List<VpnProfile> = defaultProfiles()
) : VpnProfileRepository {

    private val profiles = initialProfiles.toMutableList()
    private var activeProfileId: String? = profiles.firstOrNull()?.id

    override fun getProfiles(): List<VpnProfile> = profiles.toList()

    override fun getActiveProfile(): VpnProfile? {
        return profiles.firstOrNull { it.id == activeProfileId }
    }

    override fun setActiveProfile(profileId: String): VpnProfile? {
        val profile = profiles.firstOrNull { it.id == profileId } ?: return null
        activeProfileId = profile.id
        return profile
    }

    companion object {
        fun defaultProfiles(): List<VpnProfile> {
            return listOf(
                VpnProfile(
                    id = "default-eu",
                    name = "Europe Gateway",
                    serverAddress = "10.0.0.1",
                    dnsServers = listOf("1.1.1.1", "8.8.8.8")
                ),
                VpnProfile(
                    id = "default-us",
                    name = "US Gateway",
                    serverAddress = "10.0.1.1",
                    dnsServers = listOf("9.9.9.9", "1.0.0.1")
                )
            )
        }
    }
}
