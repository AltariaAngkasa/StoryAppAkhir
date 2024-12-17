package com.example.mystoryappsubmission.pref

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyappsubmission.data.response.ListStoryItem
import com.example.storyappsubmission.data.retrofit.ApiService
import kotlinx.coroutines.flow.first
import retrofit2.awaitResponse

class PagingStory(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) : PagingSource<Int, ListStoryItem>() {

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.let { anchorPage ->
                anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        val pageIndex = params.key ?: STARTING_PAGE_INDEX
        return try {
            val user = userPreference.getUser().first()
            val token = user.token
            val response = apiService.getAllStories(token, pageIndex, params.loadSize).awaitResponse()
            val stories = response.body()?.listStory.orEmpty()

            LoadResult.Page(
                data = stories,
                prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex - 1,
                nextKey = if (stories.isEmpty()) null else pageIndex + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
