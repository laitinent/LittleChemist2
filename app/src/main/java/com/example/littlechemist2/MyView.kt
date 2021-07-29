package com.example.littlechemist2

import android.content.Context
import android.graphics.*
import android.graphics.drawable.shapes.Shape
import android.util.AttributeSet
import android.util.Log
import android.view.View

class MyView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val textpaint = Paint()
    val paint=Paint()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        textpaint.textSize = 48f

        paint.color=Color.RED
        paint.strokeWidth = 5f


        InitToolbox(canvas!!)
        for(s in lista) {
            // show active node with border(=stroke)
            paint.color=Color.RED
            paint.style = Paint.Style.FILL
            canvas?.drawOval(s.x-25, s.y-25,s.x+s.s.width/2, s.y+s.s.height/2, paint )
            if(s.Current == true){
                paint.style = Paint.Style.STROKE
                paint.color = Color.BLACK
                canvas?.drawOval(s.x-25, s.y-25,s.x+s.s.width/2, s.y+s.s.height/2, paint )
            }

            canvas.drawText(s.text,s.x, s.y+50f, textpaint)
        }
        for(l in lines) {
            canvas.drawLine(l.left, l.top, l.right, l.bottom, Paint())
        }
    }

    private val lista = mutableListOf<VisualNode>();
    private val lines = mutableListOf<RectF>(); // using rectf to store 2 points

    fun Add(shape:VisualNode)  {
        lista.add(shape)
        invalidate()
    }

    fun SetCurrent(x: Int) {
        lista[x].Current = true
    }

    fun AddLine(start:PointF, end: PointF) {
        lines.add(RectF(start.x, start.y, end.x, end.y))
    }

    private val texts = listOf("H","O","C","OH","N")

    private fun InitToolbox(canvas:Canvas)  {

        val paint = Paint()
        paint.color = Color.YELLOW

        for(i in 0..4) {
            canvas.drawOval(i*100f, 100f,i*100f+50f, 100f+50f, paint )
            canvas.drawText(texts[i],i*100f, 100f+50f, textpaint)
        }
    }

    fun ToolHit(x:Float, y:Float): String {
        Log.d("TOOL_HIT", "y = $y")
        if(y<200f) {
            val index = (x/100f).toInt()
            Log.d("TOOL_HIT", "index = $index")
            return if(index<5) texts[index] else ""
        }
        return ""
    }

    fun Clear() {
        lines.clear()
        lista.clear()
        invalidate()
    }
}