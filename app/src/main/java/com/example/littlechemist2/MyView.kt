package com.example.littlechemist2

import android.content.Context
import android.graphics.*

import android.util.AttributeSet
import android.util.Log
import android.view.View

class MyView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val textpaint = Paint()
    private val paint=Paint()
    private val linePaint = Paint()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        textpaint.textSize = 48f

        paint.color=Color.RED
        paint.strokeWidth = 5f

        InitToolbox(canvas!!)

        for(s in lista) {
            // show active node with border(=stroke)
            paint.color = s.tb.color
            paint.style = Paint.Style.FILL
            canvas.drawOval(s.x-25, s.y-25,s.x+s.s.width/2, s.y+s.s.height/2, paint )
            if(s.Current){
                paint.style = Paint.Style.STROKE
                paint.color = Color.BLACK
                canvas.drawOval(s.x-25, s.y-25,s.x+s.s.width/2, s.y+s.s.height/2, paint )
            }

            canvas.drawText(s.tb.text, s.x, s.y+50f, textpaint)
        }
        lines.forEach { l ->
            canvas.drawLine(l.left, l.top, l.right, l.bottom, linePaint)
        }
    }

    /**
     * Remove border (stroke) so that new node will be only with border
     */
    fun ResetStrokes() {
        lista.forEach {
            it.Current = false
        }
    }

    private val lista = mutableListOf<VisualNode>()
    private val lines = mutableListOf<RectF>() // using rectf to store 2 points

    /** Add item to list of drawables
     * @param shape VisualNode to add
     */
    fun Add(shape:VisualNode)  {
        lista.add(shape)
        invalidate()
    }

    fun SetCurrent(x: Int) {
        lista[x].Current = true
    }

    /**
     * Add line to list of drawable lines
     * @param start Starting point
     * @param end Line endpoint
     */
    fun AddLine(start:PointF, end: PointF) {
        lines.add(RectF(start.x, start.y, end.x, end.y))
    }

    //private val texts = listOf("H","O","C","OH","N")
    private val items = listOf(
        ToolBoxItem("H", Color.YELLOW),
        ToolBoxItem("O", Color.RED),
        ToolBoxItem("C", Color.GRAY),
        ToolBoxItem("OH", Color.parseColor("maroon")), //https://developer.android.com/reference/android/graphics/Color.html#parseColor(java.lang.String)
        ToolBoxItem("N", Color.BLUE),
    )

    private val radius2 = 100f  // 50f
    private val x1 = 2f*radius2
    private val y1 = 100f

    private fun InitToolbox(canvas:Canvas)  {

        val paint = Paint()
        //paint.color = Color.YELLOW

        (items.indices).forEach { i ->
            paint.color = items[i].color
            canvas.drawOval(i*x1, y1,i*x1+radius2, y1+radius2, paint )
            Log.d("OVAL", "${i*x1} -> ${i*x1+radius2}")
            canvas.drawText(items[i].text,i*x1+radius2/2, y1+radius2/2, textpaint)
        }
    }

    /**
     * View top contains toolbar
     * TODO: Use recyclerView? "H" not first?
     * @param x X coord of touch
     * @param y Y coord of touch
     */
    fun ToolHit(x:Float, y:Float): ToolBoxItem? {
        Log.d("TOOL_HIT", "y = $y")
        if(y< y1+ 2*radius2) {
            val index = (x/x1).toInt()
            Log.d("TOOL_HIT", "index = $index")
            return if(index<items.size) items[index] else null
        }
        return null
    }

    /**
     * Reset graph
     */
    fun Clear() {
        lines.clear()
        lista.clear()
        invalidate()
    }
}

data class ToolBoxItem(val text:String, val color:Int)