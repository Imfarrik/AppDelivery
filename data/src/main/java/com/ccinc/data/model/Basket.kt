package com.ccinc.data.model

data class Basket(
    val carbohydratesPer100Grams: Double?,
    val categoryId: Long?,
    val description: String?,
    val energyPer100Grams: Double?,
    val fatsPer100Grams: Double?,
    val id: Long,
    val image: String?,
    val measure: Long?,
    val measureUnit: String?,
    val name: String?,
    val priceCurrent: Long?,
    val priceOld: Long?,
    val proteinsPer100Grams: Double?,
    val tagIds: List<Long>,
    val count: Int
) {
    companion object {
        fun fake() =
            Basket(
                carbohydratesPer100Grams = 42.8,
                categoryId = 676168,
                description = "Оригинальный темпура ролл с острым лососем, " +
                        "сливочным сыром и огурцом. " +
                        "Украшается кунжутом, зеленым луком, " +
                        "соусами манго и унаги  Комплектуется бесплатным набором для роллов " +
                        "(Соевый соус Лайт 35г., васаби 6г., имбирь 15г.). " +
                        "+1 набор за каждые 600 рублей в заказе",
                energyPer100Grams = 452.5,
                fatsPer100Grams = 28.1,
                id = 48,
                image = "1.jpg",
                measure = 280,
                measureUnit = "г",
                name = "Темпура с острым лососем и манговым соусом 8шт",
                priceCurrent = 48000,
                priceOld = 92249,
                proteinsPer100Grams = 7.1,
                tagIds = emptyList(),
                count = 3
            )
    }
}