package com.discovpn.app.vpn

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Base64

class VpnProfileImportUseCaseTest {

    private val useCase = VpnProfileImportUseCase(
        parser = VlessSubscriptionParser(),
        validator = TunnelConfigValidator()
    )

    @Test
    fun `rejects placeholder octovpn-style profiles`() {
        val raw = """
            vless://0998fcb5-3446-463a-ae81-13a39adda475@0.0.0.0:1?security=&type=tcp#Application%20unsupported
            vless://f6d35b6a-cc28-4760-90ef-bd17d49347ac@0.0.0.0:1?security=&type=tcp#Contact%20support
        """.trimIndent()
        val encoded = Base64.getEncoder().encodeToString(raw.toByteArray())

        val result = useCase.importFromSubscription(encoded)

        assertEquals(
            VlessProfileImportResult.Failure("Imported profiles are placeholders or invalid"),
            result
        )
    }

    @Test
    fun `imports valid vless profiles`() {
        val raw = """
            vless://11111111-2222-3333-4444-555555555555@vpn.example.com:443?type=tcp#Primary%20Node
        """.trimIndent()
        val encoded = Base64.getEncoder().encodeToString(raw.toByteArray())

        val result = useCase.importFromSubscription(encoded)

        assertTrue(result is VlessProfileImportResult.Success)
        val profiles = (result as VlessProfileImportResult.Success).profiles
        assertEquals(1, profiles.size)
        assertEquals("vpn.example.com", profiles.first().serverAddress)
    }
}
