package com.discovpn.app.vpn

sealed interface VlessProfileImportResult {
    data class Success(val profiles: List<VpnProfile>) : VlessProfileImportResult
    data class Failure(val reason: String) : VlessProfileImportResult
}
