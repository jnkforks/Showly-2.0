package com.michaldrabik.showly2.ui.show

import com.michaldrabik.network.Cloud
import com.michaldrabik.showly2.Config.ACTORS_CACHE_DURATION
import com.michaldrabik.showly2.UserManager
import com.michaldrabik.showly2.di.AppScope
import com.michaldrabik.showly2.model.Actor
import com.michaldrabik.showly2.model.Episode
import com.michaldrabik.showly2.model.EpisodeBundle
import com.michaldrabik.showly2.model.ImageType
import com.michaldrabik.showly2.model.ImageType.FANART
import com.michaldrabik.showly2.model.Season
import com.michaldrabik.showly2.model.SeasonBundle
import com.michaldrabik.showly2.model.Show
import com.michaldrabik.showly2.model.mappers.Mappers
import com.michaldrabik.showly2.ui.common.EpisodesManager
import com.michaldrabik.showly2.ui.common.ImagesManager
import com.michaldrabik.showly2.utilities.extensions.nowUtcMillis
import com.michaldrabik.storage.database.AppDatabase
import com.michaldrabik.storage.database.model.FollowedShow
import javax.inject.Inject

@AppScope
class ShowDetailsInteractor @Inject constructor(
  private val cloud: Cloud,
  private val database: AppDatabase,
  private val userManager: UserManager,
  private val imagesManager: ImagesManager,
  private val episodesManager: EpisodesManager,
  private val mappers: Mappers
) {

  suspend fun loadShowDetails(traktId: Long): Show {
    val localShow = database.showsDao().getById(traktId)
    if (localShow == null) {
      val remoteShow = cloud.traktApi.fetchShow(traktId)
      val show = mappers.show.fromNetwork(remoteShow)
      database.showsDao().upsert(listOf(mappers.show.toDatabase(show)))
      return show
    }
    return mappers.show.fromDatabase(localShow)
  }

  suspend fun loadBackgroundImage(show: Show) =
    imagesManager.loadRemoteImage(show, FANART)

  suspend fun loadNextEpisode(traktId: Long): Episode? {
    val episode = cloud.traktApi.fetchNextEpisode(traktId) ?: return null
    return mappers.episode.fromNetwork(episode)
  }

  suspend fun loadActors(show: Show): List<Actor> {
    val localActors = database.actorsDao().getAllByShow(show.ids.tvdb)
    if (localActors.isNotEmpty() && nowUtcMillis() - localActors[0].updatedAt < ACTORS_CACHE_DURATION) {
      return localActors.map { mappers.actor.fromDatabase(it) }
    }

    userManager.checkAuthorization()
    val token = userManager.getTvdbToken()
    val remoteActors = cloud.tvdbApi.fetchActors(token, show.ids.tvdb)
      .filter { it.image.isNotBlank() }
      .sortedBy { it.sortOrder }
      .take(20)
      .map { mappers.actor.fromNetwork(it) }

    database.actorsDao().deleteAllAndInsert(remoteActors.map { mappers.actor.toDatabase(it) }, show.ids.tvdb)
    return remoteActors
  }

  suspend fun loadRelatedShows(show: Show) =
    cloud.traktApi.fetchRelatedShows(show.ids.trakt)
      .sortedWith(compareBy({ it.votes }, { it.rating }))
      .reversed()
      .map { mappers.show.fromNetwork(it) }

  suspend fun findCachedImage(show: Show, type: ImageType) =
    imagesManager.findCachedImage(show, type)

  suspend fun loadMissingImage(show: Show, type: ImageType, force: Boolean) =
    imagesManager.loadRemoteImage(show, type, force)

  suspend fun loadSeasons(show: Show): List<Season> {
    return cloud.traktApi.fetchSeasons(show.ids.trakt)
      .filter { it.number != 0 } //Filtering out "special" seasons
      .map { mappers.season.fromNetwork(it) }
  }

  suspend fun isFollowed(show: Show) =
    database.followedShowsDao().getById(show.ids.trakt) != null

  suspend fun addToFollowed(show: Show) {
    val dbShow = FollowedShow.fromTraktId(show.ids.trakt, nowUtcMillis())
    database.followedShowsDao().insert(listOf(dbShow))
  }

  suspend fun removeFromFollowed(show: Show) {
    database.followedShowsDao().deleteById(show.ids.trakt)
  }

  suspend fun setEpisodeWatched(episodeBundle: EpisodeBundle) =
    episodesManager.setEpisodeWatched(episodeBundle)

  suspend fun setEpisodeUnwatched(episodeBundle: EpisodeBundle) =
    episodesManager.setEpisodeUnwatched(episodeBundle)

  suspend fun setSeasonWatched(seasonBundle: SeasonBundle) =
    episodesManager.setSeasonWatched(seasonBundle)

  suspend fun setSeasonUnwatched(seasonBundle: SeasonBundle) =
    episodesManager.setSeasonUnwatched(seasonBundle)

  suspend fun loadWatchedSeasons(show: Show) =
    episodesManager.getWatchedSeasonsIds(show)

  suspend fun loadWatchedEpisodes(show: Show) =
    episodesManager.getWatchedEpisodesIds(show)
}