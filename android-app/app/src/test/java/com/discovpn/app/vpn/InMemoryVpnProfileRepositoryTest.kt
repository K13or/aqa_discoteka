package com.discovpn.app.vpn

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class InMemoryVpnProfileRepositoryTest {

    @Test
    fun `returns default profiles`() {
        val repository = InMemoryVpnProfileRepository()

        val profiles = repository.getProfiles()

        assertTrue(profiles.isNotEmpty())
        assertEquals("default-eu", profiles.first().id)
    }

    @Test
    fun `switches active profile when id exists`() {
        val repository = InMemoryVpnProfileRepository()

        val selected = repository.setActiveProfile("default-us")

        assertEquals("default-us", selected?.id)
        assertEquals("default-us", repository.getActiveProfile()?.id)
    }

    @Test
    fun `does not switch active profile for unknown id`() {
        val repository = InMemoryVpnProfileRepository()

        val selected = repository.setActiveProfile("missing")

        assertNull(selected)
        assertEquals("default-eu", repository.getActiveProfile()?.id)
    }
}
