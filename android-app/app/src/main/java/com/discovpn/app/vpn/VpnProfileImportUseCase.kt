package com.discovpn.app.vpn

class VpnProfileImportUseCase(
    private val parser: VlessSubscriptionParser,
    private val validator: TunnelConfigValidator
) {

    fun importFromSubscription(rawSubscription: String): VlessProfileImportResult {
        return when (val parsed = parser.parseSubscription(rawSubscription)) {
            is VlessProfileImportResult.Failure -> parsed
            is VlessProfileImportResult.Success -> {
                val validProfiles = parsed.profiles.filter { profile ->
                    validator.validate(
                        TunnelConfig(
                            serverAddress = profile.serverAddress,
                            dnsServers = profile.dnsServers,
                            appName = profile.name,
                            mtu = profile.mtu
                        )
                    ) == ValidationResult.Valid
                }

                if (validProfiles.isEmpty()) {
                    VlessProfileImportResult.Failure("Imported profiles are placeholders or invalid")
                } else {
                    VlessProfileImportResult.Success(validProfiles)
                }
            }
        }
    }
}
