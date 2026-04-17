package com.discovpn.app.vpn

import java.net.URI
import java.net.URLDecoder
import java.util.Base64

class VlessSubscriptionParser {

    fun parseSubscription(rawSubscription: String): VlessProfileImportResult {
        val decoded = decodeSubscription(rawSubscription)
            ?: return VlessProfileImportResult.Failure("Subscription is not valid base64")

        val parsedProfiles = decoded
            .lineSequence()
            .map(String::trim)
            .filter(String::isNotEmpty)
            .mapNotNull(::parseVlessUri)
            .toList()

        if (parsedProfiles.isEmpty()) {
            return VlessProfileImportResult.Failure("No supported VLESS profiles found")
        }

        return VlessProfileImportResult.Success(parsedProfiles)
    }

    fun parseVlessUri(uri: String): VpnProfile? {
        if (!uri.startsWith("vless://")) return null

        val parsed = runCatching { URI(uri) }.getOrNull() ?: return null
        val rawUserInfo = parsed.userInfo ?: return null
        val host = parsed.host ?: return null
        val port = if (parsed.port == -1) 443 else parsed.port
        val fragment = parsed.rawFragment
            ?.let { URLDecoder.decode(it, Charsets.UTF_8) }
            ?.takeIf { it.isNotBlank() }
            ?: "Imported VLESS"

        return VpnProfile(
            id = rawUserInfo,
            name = fragment,
            serverAddress = host,
            dnsServers = defaultDnsServers(),
            mtu = 1500
        ).copy(
            name = "$fragment:$port"
        )
    }

    private fun decodeSubscription(rawSubscription: String): String? {
        val normalized = rawSubscription
            .trim()
            .replace("\n", "")
            .replace("\r", "")

        return runCatching {
            String(Base64.getDecoder().decode(normalized), Charsets.UTF_8)
        }.getOrNull()
    }

    private fun defaultDnsServers(): List<String> = listOf("1.1.1.1", "8.8.8.8")
}
