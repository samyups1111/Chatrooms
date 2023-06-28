package com.sample.mainapplication.model

data class MainData(
    var name: String = "",
)

data class MainResponse(
    var results: List<MainData>
)
