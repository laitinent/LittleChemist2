package com.example.littlechemist2

import android.R.attr
import android.content.Context
import android.graphics.*
import android.os.Debug

import android.util.AttributeSet
import android.util.Log
import android.view.View



class MyView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    // static funcs for drawables
    private val textpaint = Paint()
    private val paint=Paint()
    private val linePaint = Paint()

    override fun onDraw(canvas: Canvas?) {

        super.onDraw(canvas)
        textpaint.textSize = 48f

        //paint.color= Color.RED
        paint.strokeWidth = 5f

        for(vnode in lista) {
            val xsize = vnode.s.width/2
            val ysize = vnode.s.height/2
            val left = (vnode.x-xsize)
            val top = (vnode.y-ysize)
            val right = (vnode.x+xsize)
            val bottom = (vnode.y+ysize)
            // show active node with border(=stroke)
            //paint.color = vnode.tb.color
            //paint.style = Paint.Style.FILL
            //canvas.drawOval(vnode.x-25, vnode.y-25,vnode.x+vnode.vnode.width/2, vnode.y+vnode.vnode.height/2, paint )
            //val d = resources.getDrawable(R.drawable.redball, null)

            val d = resources.getDrawable(vnode.tb.drawableResId, null)
            //Log.d("MYVIEW", "${d.intrinsicHeight} x ${d.intrinsicWidth}")

            d.setBounds(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
            d.draw(canvas!!)

            if(vnode.Current){
                paint.style = Paint.Style.STROKE
                paint.color = Color.BLACK
                canvas.drawOval(left, top,right, bottom, paint )
            }

            canvas.drawText(vnode.tb.text, vnode.x, vnode.y+50f, textpaint)
        }
        lines.forEach { l ->
            canvas?.drawLine(l.left, l.top, l.right, l.bottom, linePaint)
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

    /**
     * Reset graph
     */
    fun Clear() {
        lines.clear()
        lista.clear()
        invalidate()
    }
}

//data class ToolBoxItem(val text:String, val color:Int) // draw
data class ToolBoxItem2(val text:String, val color:Int, val drawableResId: Int) // use image