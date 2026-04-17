package com.discovpn.app.vpn

import org.junit.Assert.assertEquals
import org.junit.Test

class VpnConnectionOrchestratorTest {

    private val orchestrator = VpnConnectionOrchestrator(TunnelConfigValidator())

    @Test
    fun `requests permission for valid config`() {
        val state = orchestrator.requestConnect(
            TunnelConfig(
                serverAddress = "10.0.0.1",
                dnsServers = listOf("1.1.1.1"),
                appName = "DiscoVPN"
            )
        )

        assertEquals(VpnState.PermissionRequired, state)
    }

    @Test
    fun `returns error for invalid config`() {
        val state = orchestrator.requestConnect(
            TunnelConfig(
                serverAddress = "",
                dnsServers = listOf("1.1.1.1"),
                appName = "DiscoVPN"
            )
        )

        assertEquals(VpnState.Error("Server address is required"), state)
    }

    @Test
    fun `returns idle on disconnect`() {
        assertEquals(VpnState.Idle, orchestrator.requestDisconnect())
    }
}
