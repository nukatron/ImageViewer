package com.nutron.imageviewer.presentation.widget

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import com.nutron.imageviewer.R


class TitleDetailCustomViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val titleTextView by lazy<AppCompatTextView> { findViewById(R.id.cvg_tile_txt) }
    private val detailTextView by lazy<AppCompatTextView> { findViewById(R.id.cvg_detail_text) }

    init {
        initInflateLayout()
    }

    private fun initInflateLayout() {
        View.inflate(context, R.layout.layout_titile_detail, this)
    }

    fun setTitle(title: String) {
        titleTextView.text = title
    }

    fun setDetail(detail: String) {
        detailTextView.text = detail
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>?) {
        // tell this custom view group to not travel to its child for saving child state. Save only itself state
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>?) {
        // tell this custom view group to not restore its childs state, restore only it self
        dispatchThawSelfOnly(container)
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()

        // save children's state as a Bundle
        val childrenStates = Bundle()
        for (i in 0 until childCount) {
            val id = getChildAt(i).id
            if (id != 0) {
                val childrenState = SparseArray<Parcelable>()
                getChildAt(i).saveHierarchyState(childrenState)
                childrenStates.putSparseParcelableArray(id.toString(), childrenState)
            }
        }

        val bundle = Bundle()
        bundle.putBundle("childrenStates", childrenStates)

        // save it to Parcelable
        val ss = BundleSavedState(superState)
        ss.bundle = bundle
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as BundleSavedState
        super.onRestoreInstanceState(ss.superState)

        // Restore SpareArray
        val childrenStates = ss.bundle.getBundle("childrenStates")
        childrenStates?.let {
            // Restore Children's state
            for (i in 0 until childCount) {
                val id = getChildAt(i).id
                if (id != 0) {
                    if (childrenStates.containsKey(id.toString())) {
                        val childrenState = childrenStates.getSparseParcelableArray<Parcelable>(id.toString())
                        getChildAt(i).restoreHierarchyState(childrenState)
                    }
                }
            }
        }




    }
}