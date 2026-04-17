package com.discovpn.app.vpn

class VpnConnectionOrchestrator(
    private val validator: TunnelConfigValidator
) {

    fun requestConnect(config: TunnelConfig): VpnState {
        return when (val validation = validator.validate(config)) {
            ValidationResult.Valid -> VpnState.PermissionRequired
            is ValidationResult.Invalid -> VpnState.Error(validation.reason)
        }
    }

    fun requestDisconnect(): VpnState = VpnState.Idle
}
