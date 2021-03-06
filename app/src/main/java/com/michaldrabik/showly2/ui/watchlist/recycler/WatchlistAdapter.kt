package com.michaldrabik.showly2.ui.watchlist.recycler

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.michaldrabik.showly2.ui.common.base.BaseAdapter
import com.michaldrabik.showly2.ui.watchlist.views.WatchlistHeaderView
import com.michaldrabik.showly2.ui.watchlist.views.WatchlistItemView

class WatchlistAdapter : BaseAdapter<WatchlistItem>() {

  companion object {
    private const val VIEW_TYPE_ITEM = 1
    private const val VIEW_TYPE_HEADER = 2
  }

  override val asyncDiffer = AsyncListDiffer(this, WatchlistItemDiffCallback())

  var detailsClickListener: ((WatchlistItem) -> Unit)? = null
  var checkClickListener: ((WatchlistItem) -> Unit)? = null
  var itemLongClickListener: ((WatchlistItem, View) -> Unit)? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    when (viewType) {
      VIEW_TYPE_ITEM -> BaseViewHolder(WatchlistItemView(parent.context).apply {
        itemClickListener = { super.itemClickListener.invoke(it) }
        itemLongClickListener = { item, view ->
          this@WatchlistAdapter.itemLongClickListener?.invoke(item, view)
        }
        detailsClickListener = { this@WatchlistAdapter.detailsClickListener?.invoke(it) }
        checkClickListener = { this@WatchlistAdapter.checkClickListener?.invoke(it) }
        missingImageListener = { item, force -> super.missingImageListener.invoke(item, force) }
      })
      VIEW_TYPE_HEADER -> BaseViewHolder(WatchlistHeaderView(parent.context))
      else -> throw IllegalStateException()
    }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = asyncDiffer.currentList[position]
    when (holder.itemViewType) {
      VIEW_TYPE_HEADER -> (holder.itemView as WatchlistHeaderView).bind(item.headerTextResId!!)
      VIEW_TYPE_ITEM -> (holder.itemView as WatchlistItemView).bind(item)
    }
  }

  override fun getItemViewType(position: Int) =
    when {
      asyncDiffer.currentList[position].isHeader() -> VIEW_TYPE_HEADER
      else -> VIEW_TYPE_ITEM
    }
}
