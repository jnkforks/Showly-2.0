package com.michaldrabik.showly2.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.michaldrabik.showly2.di.AppScope
import com.michaldrabik.showly2.ui.common.ImagesManager
import com.michaldrabik.showly2.ui.discover.DiscoverInteractor
import com.michaldrabik.showly2.ui.discover.DiscoverViewModel
import com.michaldrabik.showly2.ui.followedshows.FollowedShowsInteractor
import com.michaldrabik.showly2.ui.followedshows.FollowedShowsViewModel
import com.michaldrabik.showly2.ui.followedshows.myshows.MyShowsInteractor
import com.michaldrabik.showly2.ui.followedshows.myshows.MyShowsViewModel
import com.michaldrabik.showly2.ui.followedshows.watchlater.LaterShowsViewModel
import com.michaldrabik.showly2.ui.main.MainInteractor
import com.michaldrabik.showly2.ui.main.MainViewModel
import com.michaldrabik.showly2.ui.search.SearchInteractor
import com.michaldrabik.showly2.ui.search.SearchViewModel
import com.michaldrabik.showly2.ui.show.ShowDetailsInteractor
import com.michaldrabik.showly2.ui.show.ShowDetailsViewModel
import com.michaldrabik.showly2.ui.show.seasons.episodes.EpisodesInteractor
import com.michaldrabik.showly2.ui.show.seasons.episodes.details.EpisodeDetailsViewModel
import com.michaldrabik.showly2.ui.watchlist.WatchlistInteractor
import com.michaldrabik.showly2.ui.watchlist.WatchlistViewModel
import javax.inject.Inject

@AppScope
class ViewModelFactory @Inject constructor(
  private val mainInteractor: MainInteractor,
  private val discoverInteractor: DiscoverInteractor,
  private val followedShowsInteractor: FollowedShowsInteractor,
  private val myShowsInteractor: MyShowsInteractor,
  private val showDetailsInteractor: ShowDetailsInteractor,
  private val episodesInteractor: EpisodesInteractor,
  private val searchInteractor: SearchInteractor,
  private val watchlistInteractor: WatchlistInteractor,
  private val imagesManager: ImagesManager,
  private val uiCache: UiCache
) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>) = when {
    modelClass.isAssignableFrom(MainViewModel::class.java) ->
      MainViewModel(mainInteractor) as T

    modelClass.isAssignableFrom(DiscoverViewModel::class.java) ->
      DiscoverViewModel(discoverInteractor, uiCache) as T

    modelClass.isAssignableFrom(ShowDetailsViewModel::class.java) ->
      ShowDetailsViewModel(showDetailsInteractor, episodesInteractor) as T

    modelClass.isAssignableFrom(EpisodeDetailsViewModel::class.java) ->
      EpisodeDetailsViewModel(imagesManager) as T

    modelClass.isAssignableFrom(SearchViewModel::class.java) ->
      SearchViewModel(searchInteractor) as T

    modelClass.isAssignableFrom(MyShowsViewModel::class.java) ->
      MyShowsViewModel(myShowsInteractor, uiCache) as T

    modelClass.isAssignableFrom(LaterShowsViewModel::class.java) ->
      LaterShowsViewModel() as T

    modelClass.isAssignableFrom(FollowedShowsViewModel::class.java) ->
      FollowedShowsViewModel(followedShowsInteractor, uiCache) as T

    modelClass.isAssignableFrom(WatchlistViewModel::class.java) ->
      WatchlistViewModel(watchlistInteractor, episodesInteractor) as T

    else -> throw IllegalStateException("Unknown ViewModel class")
  }
}
