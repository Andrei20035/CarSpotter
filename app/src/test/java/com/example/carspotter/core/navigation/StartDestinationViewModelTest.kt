package com.example.carspotter.core.navigation

import com.example.carspotter.MainDispatcherRule
import com.example.carspotter.data.local.preferences.UserPreferences
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.util.UUID

/**
 * StartDestinationViewModel decide pe ce ecran ajunge userul la deschiderea aplicației.
 * Un bug aici = userul ajunge pe ecranul greșit la app start.
 * 4 cazuri practice — atât.
 */
class StartDestinationViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun prefsMock(
        onboardingDone: Boolean,
        token: String?,
        userId: UUID?
    ): UserPreferences = mockk<UserPreferences>().apply {
        every { onboardingCompleted } returns flowOf(onboardingDone)
        every { authToken } returns flowOf(token)
        every { this@apply.userId } returns flowOf(userId)
    }

    @Test
    fun `onboarding nefacut - duce la Onboarding indiferent de token`() = runTest {
        val vm = StartDestinationViewModel(
            prefsMock(onboardingDone = false, token = "any", userId = UUID.randomUUID())
        )

        assertEquals(Screen.Onboarding.route, vm.startDestination.value)
    }

    @Test
    fun `onboarding facut, fara token - duce la Login`() = runTest {
        val vm = StartDestinationViewModel(
            prefsMock(onboardingDone = true, token = null, userId = null)
        )

        assertEquals(Screen.Login.route, vm.startDestination.value)
    }

    @Test
    fun `token salvat dar userId null - duce la ProfileCustomization`() = runTest {
        val vm = StartDestinationViewModel(
            prefsMock(onboardingDone = true, token = "jwt", userId = null)
        )

        assertEquals(Screen.ProfileCustomization.route, vm.startDestination.value)
    }

    @Test
    fun `token + userId salvate - duce la Feed`() = runTest {
        val vm = StartDestinationViewModel(
            prefsMock(onboardingDone = true, token = "jwt", userId = UUID.randomUUID())
        )

        assertEquals(Screen.Feed.route, vm.startDestination.value)
    }
}
