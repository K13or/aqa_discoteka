package com.discovpn.app.vpn

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Base64

class VlessSubscriptionParserTest {

    private val parser = VlessSubscriptionParser()

    @Test
    fun `parses base64 subscription with vless entries`() {
        val raw = """
            vless://11111111-2222-3333-4444-555555555555@vpn.example.com:443?type=tcp#Primary%20Node
            vless://aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee@backup.example.com:8443?type=tcp#Backup%20Node
        """.trimIndent()
        val encoded = Base64.getEncoder().encodeToString(raw.toByteArray())

        val result = parser.parseSubscription(encoded)

        assertTrue(result is VlessProfileImportResult.Success)
        val profiles = (result as VlessProfileImportResult.Success).profiles
        assertEquals(2, profiles.size)
        assertEquals("vpn.example.com", profiles.first().serverAddress)
        assertEquals("Primary Node:443", profiles.first().name)
    }

    @Test
    fun `rejects invalid base64`() {
        val result = parser.parseSubscription("not-base64")

        assertEquals(
            VlessProfileImportResult.Failure("Subscription is not valid base64"),
            result
        )
    }

    @Test
    fun `ignores unsupported links`() {
        val raw = """
            ss://abcdef
            trojan://example
        """.trimIndent()
        val encoded = Base64.getEncoder().encodeToString(raw.toByteArray())

        val result = parser.parseSubscription(encoded)

        assertEquals(
            VlessProfileImportResult.Failure("No supported VLESS profiles found"),
            result
        )
    }
}
