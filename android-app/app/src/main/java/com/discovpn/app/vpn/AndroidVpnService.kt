package com.discovpn.app.vpn

import android.app.Service
import android.content.Intent
import android.net.VpnService
import android.os.IBinder
import android.os.ParcelFileDescriptor

class AndroidVpnService : VpnService() {

    private var tunnelInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (tunnelInterface == null) {
            tunnelInterface = Builder()
                .setSession("DiscoVPN")
                .setMtu(1500)
                .addAddress("10.10.0.2", 24)
                .addDnsServer("1.1.1.1")
                .addRoute("0.0.0.0", 0)
                .establish()
        }

        return Service.START_STICKY
    }

    override fun onDestroy() {
        tunnelInterface?.close()
        tunnelInterface = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? = super.onBind(intent)
}
