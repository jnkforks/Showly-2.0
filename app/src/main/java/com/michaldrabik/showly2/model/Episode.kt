package com.michaldrabik.showly2.model

import org.threeten.bp.ZonedDateTime

data class Episode(
  val season: Int,
  val number: Int,
  val title: String,
  val ids: Ids,
  val overview: String,
  val rating: Float,
  val votes: Int,
  val commentCount: Int,
  val firstAired: ZonedDateTime?,
  val updatedAt: ZonedDateTime?,
  val runtime: Int
) {

  companion object {
    val EMPTY = Episode(-1, -1, "", Ids.EMPTY, "", -1F, -1, -1, null, null, -1)
  }

  val id = ids.trakt

  fun toDisplayString() = String.format("S.%02d E.%02d", season, number)
}