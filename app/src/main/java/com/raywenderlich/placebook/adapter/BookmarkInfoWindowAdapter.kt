

package com.raywenderlich.placebook.adapter

import android.app.Activity
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.raywenderlich.placebook.databinding.ContentBookmarkInfoBinding
import com.raywenderlich.placebook.ui.MapsActivity
import com.raywenderlich.placebook.viewmodel.MapsViewModel

class BookmarkInfoWindowAdapter(val context: Activity) : GoogleMap.InfoWindowAdapter {

  private val binding = ContentBookmarkInfoBinding.inflate(context.layoutInflater)

  override fun getInfoWindow(marker: Marker): View? {
    // This function is required, but can return null if
    // not replacing the entire info window
    return null
  }

  override fun getInfoContents(marker: Marker): View {
    binding.title.text = marker.title ?: ""
    binding.phone.text = marker.snippet ?: ""

    val imageView = binding.photo
    when (marker.tag) {
      is MapsActivity.PlaceInfo -> {
        imageView.setImageBitmap(
            (marker.tag as MapsActivity.PlaceInfo).image)
      }
      is MapsViewModel.BookmarkMarkerView -> {
        val bookMarkview = marker.tag as
            MapsViewModel.BookmarkMarkerView
        // Set imageView bitmap here
        imageView.setImageBitmap(bookMarkview.getImage(context))
      }
    }
    return binding.root
  }
}
