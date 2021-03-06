package com.michaldrabik.showly2.ui.show.comments

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.michaldrabik.showly2.model.Comment
import com.michaldrabik.showly2.ui.common.views.CommentView

class CommentsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private val asyncDiffer = AsyncListDiffer(this, CommentItemDiffCallback())

  fun setItems(newItems: List<Comment>) = asyncDiffer.submitList(newItems)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    ViewHolderShow(CommentView(parent.context))

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = asyncDiffer.currentList[position]
    (holder.itemView as CommentView).bind(item)
  }

  override fun getItemCount() = asyncDiffer.currentList.size

  class ViewHolderShow(itemView: View) : RecyclerView.ViewHolder(itemView)
}
