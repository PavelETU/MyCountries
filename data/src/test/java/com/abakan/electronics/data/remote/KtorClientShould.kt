package com.abakan.electronics.data.remote

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class KtorClientShould {
    private lateinit var ktorClient: KtorClient

    @Before
    fun setUp() {
        val mockEngine = MockEngine {
            respond(
                ByteReadChannel(JSON_TEST_RESPONSE),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        ktorClient = KtorClient(mockEngine)
    }

    @Test
    fun `parse JSON response to a list of countries`() = runTest {
        val countries = ktorClient.getCountries()
        assertEquals(2, countries.size)
        assertEquals("United Kingdom", countries[0].name.common)
        assertEquals(1, countries[0].capital?.size)
        assertEquals("London", countries[0].capital?.getOrNull(0))
        assertEquals(
            "https://mainfacts.com/media/images/coats_of_arms/gb.png",
            countries[0].coatOfArms.png
        )
        assertEquals("Bouvet Island", countries[1].name.common)
        assertEquals(null, countries[1].capital)
        assertEquals(null, countries[1].coatOfArms.png)
    }
}