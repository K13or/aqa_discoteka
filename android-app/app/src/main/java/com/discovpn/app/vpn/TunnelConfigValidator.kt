package com.discovpn.app.vpn

class TunnelConfigValidator {

    fun validate(config: TunnelConfig): ValidationResult {
        if (config.serverAddress.isBlank()) {
            return ValidationResult.Invalid("Server address is required")
        }

        if (config.serverAddress == "0.0.0.0") {
            return ValidationResult.Invalid("Profile contains a placeholder server address")
        }

        if (config.dnsServers.isEmpty()) {
            return ValidationResult.Invalid("At least one DNS server is required")
        }

        if (config.mtu !in 1280..9000) {
            return ValidationResult.Invalid("MTU must stay in a safe range")
        }

        return ValidationResult.Valid
    }
}

sealed interface ValidationResult {
    data object Valid : ValidationResult
    data class Invalid(val reason: String) : ValidationResult
}
