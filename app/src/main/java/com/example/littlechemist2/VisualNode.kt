package com.example.littlechemist2

import android.graphics.Color
import android.graphics.drawable.shapes.Shape

/**
 * all data needed for drawing
 */
class VisualNode (val x:Float, val y:Float, val s: Shape, val tb:ToolBoxItem, var Current:Boolean=false)
{
    companion object
    {
        fun textFromColor(color:Int):String
        {
            return when(color)
            {
                Color.RED -> "O"
                Color.BLUE -> "N"
                Color.YELLOW -> "H"
                Color.BLACK -> "C"
                else -> {"OH"}
            }
        }
        fun colorFromText(text:String):Int
        {
            return when(text)
            {
                 "O"->Color.RED
                 "N"->Color.BLUE
                 "H"->Color.YELLOW
                 "C"->Color.BLACK
                else -> {Color.WHITE}
            }
        }
    }
}