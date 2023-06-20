package com.example.caritaappnew.model.pagging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.caritaappnew.model.api.ApiService
import com.example.caritaappnew.model.respon.Stories

//class StoryPaging(
//    private val token: String, private val apiService: ApiService) : PagingSource<Int, Stories>() {
//
//    private companion object{
//        const val INITIAL_PAGE_INDEX = 1
//    }
//
//
//    override fun getRefreshKey(state: PagingState<Int, Stories>): Int? {
//        return state.anchorPosition?.let { position ->
//            val anchor = state.closestPageToPosition(position)
//            anchor?.prevKey?.plus(1) ?: anchor?.nextKey?.minus(1)
//        }
//    }
//
//    override suspend fun load(params: LoadParams<Int>, state: PagingState<Int, Stories>): LoadResult<Int, Stories> {
//        return try {
//            val page = params.key ?: INITIAL_PAGE_INDEX
//            val responseData = apiService.listStory(
//                "Bearer $token",
//                page,
//                state.config.pageSize
//            )
//
//            LoadResult.Page(
//                data = responseData,
//                prevKey = if(page==1) null else page -1,
//                nextKey = if(responseData) null else page + 1
//            )
//        } catch (exception: Exception) {
//            return LoadResult.Error(exception)
//        }
//    }
//
//
//}