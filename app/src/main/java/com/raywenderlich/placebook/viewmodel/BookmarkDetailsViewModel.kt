

package com.raywenderlich.placebook.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.raywenderlich.placebook.model.Bookmark
import com.raywenderlich.placebook.repository.BookmarkRepo
import com.raywenderlich.placebook.util.ImageUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BookmarkDetailsViewModel(application: Application) : AndroidViewModel(application) {

  private val bookmarkRepo: BookmarkRepo = BookmarkRepo(getApplication())
  private var bookmarkDetailsView: LiveData<BookmarkDetailsView>? = null

  fun getBookmark(bookmarkId: Long): LiveData<BookmarkDetailsView>? {
    if (bookmarkDetailsView == null) {
      mapBookmarkToBookmarkView(bookmarkId)
    }
    return bookmarkDetailsView
  }

  fun updateBookmark(bookmarkDetailsView: BookmarkDetailsView) {
    GlobalScope.launch {
      val bookmark = bookmarkViewToBookmark(bookmarkDetailsView)
      bookmark?.let { bookmarkRepo.updateBookmark(it) }
    }
  }

  private fun bookmarkViewToBookmark(bookmarkDetailsView: BookmarkDetailsView): Bookmark? {
    val bookmark = bookmarkDetailsView.id?.let {
      bookmarkRepo.getBookmark(it)
    }
    if (bookmark != null) {
      bookmark.id = bookmarkDetailsView.id
      bookmark.name = bookmarkDetailsView.name
      bookmark.phone = bookmarkDetailsView.phone
      bookmark.address = bookmarkDetailsView.address
      bookmark.notes = bookmarkDetailsView.notes
    }
    return bookmark
  }

  private fun mapBookmarkToBookmarkView(bookmarkId: Long) {
    val bookmark = bookmarkRepo.getLiveBookmark(bookmarkId)
    bookmarkDetailsView = Transformations.map(bookmark) { repoBookmark ->
      bookmarkToBookmarkView(repoBookmark)
    }
  }

  private fun bookmarkToBookmarkView(bookmark: Bookmark): BookmarkDetailsView {
    return BookmarkDetailsView(
        bookmark.id,
        bookmark.name,
        bookmark.phone,
        bookmark.address,
        bookmark.notes
    )
  }

  data class BookmarkDetailsView(
      var id: Long? = null,
      var name: String = "",
      var phone: String = "",
      var address: String = "",
      var notes: String = ""
  ) {
    fun getImage(context: Context) = id?.let {
      ImageUtils.loadBitmapFromFile(context, Bookmark.generateImageFilename(it))
    }
  }

}
