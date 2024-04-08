package com.ccinc.data.repositories

import com.ccinc.api.FoodApi
import com.ccinc.api.model.TagsResponse
import com.ccinc.common.Logger
import com.ccinc.data.model.RequestResult
import com.ccinc.data.model.Tags
import com.ccinc.data.utils.map
import com.ccinc.data.utils.toRequestResult
import com.ccinc.data.utils.toTags
import com.ccinc.data.utils.toTagsDBO
import com.ccinc.database.FoodDatabase
import com.ccinc.database.models.TagsDBO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class TagsRepository @Inject constructor(
    private val database: FoodDatabase,
    private val api: FoodApi,
    private val logger: Logger,
) {

    suspend fun getTags(): List<Tags> {
        return database.tagsDao.getTags().map { it.toTags() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeTags(mergeStrategy: MergeStrategy<RequestResult<List<Tags>>> = RequestResponseMergeStrategy()): Flow<RequestResult<List<Tags>>> {
        return getDataFromDatabase().combine(getDataFromServer(), mergeStrategy::merge)
            .flatMapConcat { result ->
                if (result is RequestResult.Success) {
                    database.tagsDao.observeTags()
                        .map { list ->
                            list.map { item ->
                                item.toTags()
                            }
                        }
                        .map {
                            RequestResult.Success(it)
                        }
                } else {
                    flowOf(result)
                }
            }
    }

    private fun getDataFromServer(): Flow<RequestResult<List<Tags>>> {

        val apiRequest = flow { emit(api.getTags()) }
            .onEach { result ->
                if (result.isSuccess) {
                    saveNetResultToCache(result.getOrThrow())
                } else {
                    logger.e(
                        LOG_TAG,
                        "Error getting from server. Case = ${result.exceptionOrNull()}"
                    )
                }
            }
            .map { it.toRequestResult() }

        val start = flowOf<RequestResult<List<TagsResponse>>>(RequestResult.InProgress())

        return merge(apiRequest, start)
            .map { result ->
                result.map { list ->
                    list.map { it.toTags() }
                }
            }
    }

    private fun getDataFromDatabase(): Flow<RequestResult<List<Tags>>> {
        val dbRequest = database.tagsDao::getTags.asFlow()
            .map<List<TagsDBO>, RequestResult<List<TagsDBO>>> { RequestResult.Success(it) }
            .catch {
                logger.d(LOG_TAG, "Error getting from database. Case = $it")
                emit(RequestResult.Error(error = it))
            }

        val start = flowOf<RequestResult<List<TagsDBO>>>(RequestResult.InProgress())
        return merge(start, dbRequest)
            .map { result ->
                result.map { list ->
                    list.map { it.toTags() }
                }
            }
    }

    private suspend fun saveNetResultToCache(data: List<TagsResponse>) {
        val dbo = data.map { it.toTagsDBO() }
        database.tagsDao.insertTags(dbo)
    }

    companion object {
        const val LOG_TAG = "TagsRepository"
    }

}



