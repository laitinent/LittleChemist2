package com.example.littlechemist2

import android.graphics.drawable.shapes.Shape

/**
 * all data needed for drawing
 */
data class VisualNode (val x:Float, val y:Float, val s: Shape, val tb:ToolBoxItem, var Current:Boolean=false)