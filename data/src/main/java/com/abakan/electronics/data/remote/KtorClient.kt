package com.abakan.electronics.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Inject

internal class KtorClient @Inject constructor(engine: HttpClientEngine) :
    CountriesRemoteDataSource {
    private val httpClient: HttpClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            }, contentType = ContentType.Any)
        }
    }

    override suspend fun getCountries(): List<RemoteCountry> =
        httpClient.get("https://restcountries.com/v3.1/all").body()
}
