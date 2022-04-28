package com.wiki.cf_core.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.pagination(loadThreshold: Int, loadNextPage: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager.itemCount
            val lastVisible = layoutManager.findLastVisibleItemPosition()
            val endHasBeenReached = lastVisible + loadThreshold >= totalItemCount
            if (totalItemCount > 0 && endHasBeenReached) {
                loadNextPage()
            }
        }
    })
}

fun RecyclerView.listenerLastElementVisible(listener: (isVisible: Boolean) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager.itemCount
            val lastVisible = layoutManager.findLastVisibleItemPosition()
            val endHasBeenReached = lastVisible + 1 >= totalItemCount
            listener(endHasBeenReached)
        }
    })
}

