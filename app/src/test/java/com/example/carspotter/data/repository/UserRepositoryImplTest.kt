package com.example.carspotter.data.repository

import com.example.carspotter.core.network.ApiResult
import com.example.carspotter.data.model.User
import com.example.carspotter.data.remote.api.UserApi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import java.util.UUID

class UserRepositoryImplTest {

    private val api: UserApi = mockk()
    private lateinit var repository: UserRepositoryImpl

    private val userId = UUID.fromString("11111111-1111-1111-1111-111111111111")
    private val jsonMedia = "application/json".toMediaType()

    private fun user() = User(
        id = userId,
        fullName = "Test User",
        username = "testuser",
        country = "Romania",
    )

    @Before
    fun setUp() {
        repository = UserRepositoryImpl(api)
    }

    @Test
    fun `getUserById success returneaza ApiResult Success cu datele utilizatorului`() = runTest {
        coEvery { api.getUserById(userId) } returns Response.success(user())

        val result = repository.getUserById(userId)

        assertTrue(result is ApiResult.Success)
        val data = (result as ApiResult.Success).data
        assertEquals(userId, data.id)
        assertEquals("testuser", data.username)
        assertEquals("Test User", data.fullName)
    }

    @Test
    fun `getUserById 404 returneaza ApiResult Error cu mesajul din body`() = runTest {
        val errorBody = """{"error":"User not found"}""".toResponseBody(jsonMedia)
        coEvery { api.getUserById(userId) } returns Response.error(404, errorBody)

        val result = repository.getUserById(userId)

        assertTrue(result is ApiResult.Error)
        assertEquals("User not found", (result as ApiResult.Error).message)
    }

    @Test
    fun `getUserById eroare de retea returneaza ApiResult Error cu prefixul Network error`() = runTest {
        coEvery { api.getUserById(userId) } throws IOException("Connection refused")

        val result = repository.getUserById(userId)

        assertTrue(result is ApiResult.Error)
        assertTrue((result as ApiResult.Error).message.startsWith("Network error"))
    }
}
