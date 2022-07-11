package com.wiki.cf_ui.recycler_view_decorators

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.view.children
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.recycler.decorator.Decorator
import kotlin.math.abs

class StickyHeaderDecor(
    private val itemViewTypes: List<Int>
) : Decorator.RecyclerViewDecor {

    private data class StickHeaderInfo(
        val absoluteAdapterPosition: Int,
        val bitmap: Bitmap
    )

    private val stickyHeadersInfo = mutableListOf<StickHeaderInfo>()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var lastY: Float = 0f
    private var lastPosition: Int? = 0
    private var lastMoveWasNegative = true
    override fun draw(canvas: Canvas, recyclerView: RecyclerView, state: RecyclerView.State) {

        //find all StiсkyHolders
        val stickyHolders = recyclerView.children
            .map { recyclerView.findContainingViewHolder(it) }
            .filter { itemViewTypes.contains(it?.itemViewType) }.toList()

        //ensure that we see all attached holders
        stickyHolders.forEach { it?.itemView?.alpha = 1f }

        //take first StiсkyHolder and his params

        val stickyViewHolder = stickyHolders.firstOrNull()
        val stickyViewHolderBitmap =
            if (stickyViewHolder?.itemView?.isLaidOut == true) stickyViewHolder.itemView.drawToBitmap() else null
        val viewHolderY = stickyViewHolder?.itemView?.y ?: 0f
        stickyHeadersInfo.sortBy { it.absoluteAdapterPosition }
        stickyHeadersInfo.removeIf {
            recyclerView.adapter?.itemCount!! <= it.absoluteAdapterPosition ||
                !itemViewTypes.contains(recyclerView.adapter?.getItemViewType(it.absoluteAdapterPosition))
        }

        if (stickyViewHolder != null && stickyViewHolderBitmap != null && viewHolderY >= 0) {
            if (!stickyHeadersInfo.any { it.absoluteAdapterPosition == stickyViewHolder.absoluteAdapterPosition }) {
                stickyHeadersInfo.add(
                    StickHeaderInfo(
                        stickyViewHolder.absoluteAdapterPosition,
                        stickyViewHolderBitmap
                    )
                )
            }
        }
        var currentStickInfo = if (stickyViewHolder != null && viewHolderY <= 0f && stickyHeadersInfo.isNotEmpty()) {
            stickyHeadersInfo.currentStickHeaderGetByPosition(stickyViewHolder.absoluteAdapterPosition)
        } else {
            lastPosition
        }

        if (viewHolderY > 0f && lastMoveWasNegative && lastPosition == currentStickInfo && currentStickInfo != 0) {
            if (isUpDirection(viewHolderY, lastY) && currentStickInfo != null) {
                currentStickInfo -= 1
            }
        }


        //calculate bitmap top offset
        val bitmapHeight = currentStickInfo?.let { stickyHeadersInfo.getOrNull(it)?.bitmap?.height?.toFloat() } ?: 0f
        val bitmapTopOffset = if (0 <= viewHolderY && viewHolderY <= bitmapHeight && stickyViewHolderBitmap != null) {
            viewHolderY - bitmapHeight
        } else {
            0f
        }

        //draw bitmap header
        println("1234 currentStickInfo $currentStickInfo isUpDirection ${isUpDirection(viewHolderY, lastY)}")
        if (currentStickInfo != null) {
            stickyHeadersInfo.getOrNull(currentStickInfo)?.bitmap?.let {
                canvas.drawBitmap(it, 0f, bitmapTopOffset, paint)
            }
        }

        lastY = viewHolderY
        lastPosition = currentStickInfo
        lastMoveWasNegative = viewHolderY < 0
    }

    private fun List<StickHeaderInfo>.currentStickHeaderGetByPosition(position: Int): Int? {
        val test = this.firstOrNull { it.absoluteAdapterPosition == position } ?: return null
        return this.indexOf(test)
    }

    private fun isDownDirection(currentY: Float, lastY: Float): Boolean {
        return lastY > currentY
    }

    private fun isUpDirection(currentY: Float, lastY: Float): Boolean {
        return lastY < currentY && abs((abs(currentY) - abs(lastY))) < 100
    }
}