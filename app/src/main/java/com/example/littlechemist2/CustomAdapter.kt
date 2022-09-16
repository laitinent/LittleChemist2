package com.example.littlechemist2

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val dataSet: Array<ToolBoxItem2>, val enable: Boolean,val clickListener: (ToolBoxItem2) -> Unit) :   // was Int //was <String>
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        var imageView: ImageView
        init {
            // Define click listener for the ViewHolder's View.
            imageView = view.findViewById(R.id.imageView)
            textView = view.findViewById(R.id.textView)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // https://stackoverflow.com/questions/29424944/recyclerview-itemclicklistener-in-kotlin
        var item = dataSet[position]
        viewHolder.imageView.setOnClickListener { clickListener(item) }  // redirect listener received as parameter to clicklistener of each image in array
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        // text found from color of drawable
        //TODO: may be simplified
        viewHolder.textView.text = item.text//VisualNode.textFromColor(MyView.getColorFromDrawable(dataSet[position]))//dataSet[position]
        viewHolder.imageView.setImageResource(item.drawableResId)//dataSet[position])
        viewHolder.imageView.isEnabled = enable
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
