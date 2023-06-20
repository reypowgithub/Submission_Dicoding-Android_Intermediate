package com.example.caritaappnew.view.main.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.caritaappnew.DataDummy
import com.example.caritaappnew.MainDispatcherRule
import com.example.caritaappnew.getOrAwaitValue
import com.example.caritaappnew.model.pagging.StoryRepo
import com.example.caritaappnew.model.respon.Stories
import com.example.caritaappnew.model.userPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ListActivityTest{
    private lateinit var pref : userPreference

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantExcecutorRule = InstantTaskExecutorRule()

    @Before
    fun Setup(){
        pref = mock(userPreference::class.java)
    }

    @Mock
    private lateinit var storyRepo: StoryRepo
    private val dummyToken =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWJDOTlPREpPVm9kYTZvODQiLCJpYXQiOjE2ODQ0MDYxMjV9.yxxHitCRRrryltCHv7L_ss75yVrXli7fb46XIkro3Ss"

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest{
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<Stories> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<Stories>>()
        expectedStory.value = data
        Mockito.`when`(storyRepo.getStories(dummyToken)).thenReturn(expectedStory)

        val listViewModel = ListViewModel(storyRepo, pref)
        val actualStory: PagingData<Stories> = listViewModel.story(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])

    }

    @Test
    fun `when Get Quote Empty Should Return No Data`() = runTest {
        val data: PagingData<Stories> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<Stories>>()
        expectedStory.value = data
        Mockito.`when`(storyRepo.getStories(dummyToken)).thenReturn(expectedStory)
        val listViewModel = ListViewModel(storyRepo, pref)
        val actualStory: PagingData<Stories> = listViewModel.story(dummyToken).getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)
        assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<Stories>>>() {
    companion object {
        fun snapshot(items: List<Stories>): PagingData<Stories> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Stories>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Stories>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}