package com.michaldrabik.showly2.ui.show.related

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.michaldrabik.showly2.R
import com.michaldrabik.showly2.model.Image.Status.AVAILABLE
import com.michaldrabik.showly2.ui.common.views.ShowView
import com.michaldrabik.showly2.utilities.extensions.gone
import com.michaldrabik.showly2.utilities.extensions.onClick
import com.michaldrabik.showly2.utilities.extensions.visible
import kotlinx.android.synthetic.main.view_related_show.view.*

class RelatedShowView : ShowView<RelatedListItem> {

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

  init {
    inflate(context, R.layout.view_related_show, this)
    layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    onClick { itemClickListener?.invoke(item) }
  }

  override val imageView: ImageView = relatedImage
  override val placeholderView: ImageView = relatedPlaceholder

  private lateinit var item: RelatedListItem

  override fun bind(
    item: RelatedListItem,
    missingImageListener: (RelatedListItem, Boolean) -> Unit
  ) {
    clear()
    this.item = item
    relatedTitle.text = item.show.title
    loadImage(item, missingImageListener)
  }

  override fun onImageLoadFail(item: RelatedListItem, missingImageListener: (RelatedListItem, Boolean) -> Unit) {
    super.onImageLoadFail(item, missingImageListener)
    if (item.image.status == AVAILABLE) relatedTitle.visible()
  }

  private fun clear() {
    relatedTitle.text = ""
    relatedPlaceholder.gone()
    relatedTitle.gone()
    Glide.with(this).clear(relatedImage)
  }
}
