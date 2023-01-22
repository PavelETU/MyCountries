package com.abakan.electronics.data

import com.abakan.electronics.data.db.CountryEntity

internal val FALLBACK_DATA = listOf(
    CountryEntity(
        0,
        "Spain",
        "Madrid",
        "https://mainfacts.com/media/images/coats_of_arms/es.png"
    ),
    CountryEntity(
        1,
        "UK",
        "London",
        "https://mainfacts.com/media/images/coats_of_arms/gb.png"
    ),
    CountryEntity(
        2,
        "US",
        "Washington D.C.",
        "https://mainfacts.com/media/images/coats_of_arms/us.png"
    )
)