package com.discovpn.app.vpn

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class VpnViewModelTest {

    @Test
    fun `exposes default profile on init`() {
        val viewModel = VpnViewModel()

        val state = viewModel.uiState.value

        assertEquals("default-eu", state.activeProfile?.id)
        assertEquals(VpnState.Idle, state.vpnState)
    }

    @Test
    fun `updates active profile after selection`() {
        val viewModel = VpnViewModel()

        viewModel.selectProfile("default-us")

        assertEquals("default-us", viewModel.uiState.value.activeProfile?.id)
    }

    @Test
    fun `returns permission required for valid active profile`() {
        val viewModel = VpnViewModel()

        val result = viewModel.requestConnect()

        assertEquals(VpnState.PermissionRequired, result)
        assertEquals(VpnState.PermissionRequired, viewModel.uiState.value.vpnState)
    }

    @Test
    fun `returns error when no profile is selected`() {
        val repository = object : VpnProfileRepository {
            override fun getProfiles(): List<VpnProfile> = emptyList()
            override fun getActiveProfile(): VpnProfile? = null
            override fun setActiveProfile(profileId: String): VpnProfile? = null
        }
        val viewModel = VpnViewModel(
            profileRepository = repository,
            orchestrator = VpnConnectionOrchestrator(TunnelConfigValidator())
        )

        val result = viewModel.requestConnect()

        assertTrue(result is VpnState.Error)
        assertEquals("Select a VPN profile first", (result as VpnState.Error).message)
    }

    @Test
    fun `moves to connected after confirmation`() {
        val viewModel = VpnViewModel()

        viewModel.markConnected()

        assertEquals(VpnState.Connected, viewModel.uiState.value.vpnState)
    }
}
