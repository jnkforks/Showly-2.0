package com.michaldrabik.showly2.model

import com.michaldrabik.network.trakt.model.User
import org.threeten.bp.ZonedDateTime

data class Comment(
  val id: Long,
  val parentId: Long,
  val comment: String,
  val userRating: Int,
  val spoiler: Boolean,
  val review: Boolean,
  val createdAt: ZonedDateTime?,
  val user: User
) {

  fun hasSpoilers() = spoiler || comment.contains("spoiler", true)
}
