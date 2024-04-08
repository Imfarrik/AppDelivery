package com.ccinc.data.model

data class Categories(
    val id: Long,
    val name: String
) {
    companion object {
        fun fake() : Categories =
            Categories(id = 676153, name = "Горячие блюда")
    }
}