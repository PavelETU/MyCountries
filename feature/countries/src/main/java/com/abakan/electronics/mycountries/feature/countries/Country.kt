package com.abakan.electronics.mycountries.feature.countries

data class Country(val name: Name, val capital: Capital, val coatOfArmsImage: CoatOfArmsImage)

@JvmInline
value class Name(val name: String)

@JvmInline
value class Capital(val capital: String)

@JvmInline
value class CoatOfArmsImage(val url: String)
