package com.appsbyjpak.donzo.Adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.appsbyjpak.donzo.R
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.SwipeLayout.SwipeListener
import com.daimajia.swipe.adapters.ArraySwipeAdapter


class TodoListAdapter(context: Context, var todoLists: ArrayList<String?>, var todoItems: Int)
    : ArraySwipeAdapter<String>(context, R.layout.navigation_list_item, todoLists) {

    override fun getCount(): Int {
        return todoLists.size
    }

    override fun getItem(position: Int): String? {
        return todoLists[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v = LayoutInflater.from(context).inflate(R.layout.navigation_list_item, parent, false)
        v.findViewById<TextView>(R.id.todo_list_item).text = getItem(position)

        val swipeLayout = v.findViewById<SwipeLayout>(getSwipeLayoutResourceId(position))
        swipeLayout.showMode = SwipeLayout.ShowMode.PullOut
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, v.findViewById(R.id.trash_wrapper))
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, v.findViewById(R.id.archive_wrapper))

        swipingDrawerItems(swipeLayout, position)

        return v
    }

    // Sets the View as Selected
    private fun setAsSelected(v: SwipeLayout) {
        v.surfaceView.setBackgroundColor(Color.parseColor("#1f1f1f"))
    }

    // Delete & Archive functionality for Nav Drawer Lists
    private fun swipingDrawerItems(swipeLayout: SwipeLayout, position: Int) {
        swipeLayout.addSwipeListener(object : SwipeListener {
            private var isOpen: Boolean = false
            private var isDelete = false
            private var isArchive = false

            override fun onClose(layout: SwipeLayout) {
                //when the SurfaceView totally cover the BottomView.
                isOpen = false
                layout.surfaceView.setBackgroundColor(ContextCompat.getColor(context, R.color.sidemenu))
            }

            override fun onUpdate(layout: SwipeLayout, leftOffset: Int, topOffset: Int) {}

            override fun onStartOpen(layout: SwipeLayout) {
                layout.close()
            }

            override fun onOpen(layout: SwipeLayout) {
                isOpen = true
                if (layout.dragEdge == SwipeLayout.DragEdge.Left) {
                    layout.surfaceView.setBackgroundColor(Color.parseColor("#C3524C"))
                    isDelete = true
                } else if (layout.dragEdge == SwipeLayout.DragEdge.Right) {
                    layout.surfaceView.setBackgroundColor(Color.parseColor("#C4A44E"))
                    isArchive = true
                }
            }

            override fun onStartClose(layout: SwipeLayout) {
                if (isDelete) {
                    layout.postDelayed({
                        todoLists.remove(todoLists[position])
                        notifyDataSetChanged()
                    }, 400)
                } else if (isArchive) {

                }
            }

            override fun onHandRelease(layout: SwipeLayout, xvel: Float, yvel: Float) {
                layout.postDelayed({
                    layout.close()
                }, 300)
            }
        })
    }
}