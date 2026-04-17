package com.discovpn.app.vpn

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TunnelConfigValidatorTest {

    private val validator = TunnelConfigValidator()

    @Test
    fun `accepts valid config`() {
        val config = TunnelConfig(
            serverAddress = "10.0.0.1",
            dnsServers = listOf("1.1.1.1"),
            appName = "DiscoVPN"
        )

        val result = validator.validate(config)

        assertEquals(ValidationResult.Valid, result)
    }

    @Test
    fun `rejects blank server address`() {
        val config = TunnelConfig(
            serverAddress = "",
            dnsServers = listOf("1.1.1.1"),
            appName = "DiscoVPN"
        )

        val result = validator.validate(config)

        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Server address is required", (result as ValidationResult.Invalid).reason)
    }

    @Test
    fun `rejects config without dns`() {
        val config = TunnelConfig(
            serverAddress = "10.0.0.1",
            dnsServers = emptyList(),
            appName = "DiscoVPN"
        )

        val result = validator.validate(config)

        assertTrue(result is ValidationResult.Invalid)
        assertEquals("At least one DNS server is required", (result as ValidationResult.Invalid).reason)
    }
}
