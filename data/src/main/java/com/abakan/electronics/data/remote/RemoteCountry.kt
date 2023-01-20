package com.abakan.electronics.data.remote

import com.abakan.electronics.data.db.CountryEntity
import kotlinx.serialization.Serializable

@Serializable
internal data class RemoteCountry(
    val name: Name,
    val capital: List<String>? = null,
    val coatOfArms: CoatOfArmsResponse
)

@Serializable
internal data class Name(val common: String)

@Serializable
internal data class CoatOfArmsResponse(val png: String? = null)

internal fun RemoteCountry.toEntity(id: Int): CountryEntity =
    CountryEntity(id, name.common, capital?.getOrNull(0) ?: "", coatOfArms?.png ?: "")
