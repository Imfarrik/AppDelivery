package com.ccinc.data.use_cases

import com.ccinc.data.model.Categories
import com.ccinc.data.model.Products
import com.ccinc.data.model.RequestResult
import com.ccinc.data.model.Tags
import com.ccinc.data.repositories.CategoriesRepository
import com.ccinc.data.repositories.ProductsRepository
import com.ccinc.data.repositories.TagsRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class CatalogUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
    private val productsRepository: ProductsRepository,
    private val tagsRepository: TagsRepository,
) {

    operator fun invoke(): Flow<RequestResult<CatalogUIModel>> {
        return combine(
            categoriesRepository.getCategories(),
            productsRepository.getProducts(),
            tagsRepository.observeTags(),
        )
        { c, p, t ->
            when {
                c is RequestResult.Error || p is RequestResult.Error || t is RequestResult.Error -> {
                    RequestResult.Error(
                        CatalogUIModel(
                            products = p.data,
                            categories = c.data,
                            tags = t.data
                        )
                    )
                }

                c is RequestResult.InProgress || p is RequestResult.InProgress || t is RequestResult.InProgress -> {
                    RequestResult.InProgress(
                        CatalogUIModel(
                            products = p.data,
                            categories = c.data,
                            tags = t.data
                        )
                    )
                }

                c is RequestResult.Success && p is RequestResult.Success && t is RequestResult.Success -> {
                    RequestResult.Success(
                        CatalogUIModel(
                            products = p.data,
                            categories = c.data,
                            tags = t.data
                        )
                    )
                }

                else -> error("Unimplemented branch products -> $p & categories -> $c & tags -> $t")
            }
        }
    }

}

data class CatalogUIModel(
    val products: List<Products>?,
    val categories: List<Categories>?,
    val tags: List<Tags>?,
)

//interface DataMergeStrategy<C : Any, P : Any, T : Any, R : Any> {
//    fun merge(
//        categories: RequestResult<C>,
//        products: RequestResult<P>,
//        tags: RequestResult<T>
//    ): RequestResult<R>
//}
//
//class MyDataMergeStrategy<C : Any, P : Any, T : Any> : DataMergeStrategy<C, P, T, CatalogUIModel> {
//    override fun merge(
//        categories: RequestResult<C>,
//        products: RequestResult<P>,
//        tags: RequestResult<T>
//    ): RequestResult<CatalogUIModel> {
//        return when {
//            categories is RequestResult.Success && products is RequestResult.Success && tags is RequestResult.Success -> {
//                RequestResult.Success(
//                    CatalogUIModel(
//                        products = products.data,
//                        categories = categories.data,
//                        tags = tags.data
//                    )
//                )
//            }
//
//            categories is RequestResult.InProgress && products is RequestResult.Success && tags is RequestResult.Success -> {
//                RequestResult.InProgress(
//                    CatalogUIModel(
//                        products = products.data,
//                        categories = categories.data,
//                        tags = tags.data
//                    )
//                )
//            }
//
//            categories is RequestResult.Error && products is RequestResult.Success && tags is RequestResult.Success -> {
//                RequestResult.Error(
//                    CatalogUIModel(
//                        products = products.data,
//                        categories = categories.data,
//                        tags = tags.data
//                    )
//                )
//            }
//
//            else -> throw IllegalArgumentException("Unimplemented branch products -> $products & categories -> $categories & tags -> $tags")
//        }
//    }
//}
