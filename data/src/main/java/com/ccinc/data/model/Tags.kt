package com.ccinc.data.model

data class Tags(
    val id: Long,
    val name: String,
    val isChecked: Boolean = false
) {
    companion object {
        fun fake() =
            Tags(
                id = 2,
                name = "Вегетарианское блюдо",
                isChecked = true,
            )
    }
}