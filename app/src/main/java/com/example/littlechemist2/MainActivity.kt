package com.example.littlechemist2

import android.graphics.Point
import android.graphics.PointF
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var myView:MyView
    lateinit var textView:TextView
    lateinit var button:Button
    var selectedText=""
    val App = ChainSystem()
    var numbers = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
     var selectedPoint = PointF()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myView = findViewById(R.id.myView)
        textView = findViewById(R.id.textView)
        button = findViewById(R.id.button)
        button.setOnClickListener {
            App.Clear()
            myView.Clear()
            selectedPoint = PointF()
        }

        var shape = OvalShape()
        shape.resize(50f,50f)

        myView.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    with(myView) {
                        performClick()
                        if(event.y < 200f) {
                            selectedText = ToolHit(event.x, event.y)
                        }
                        else {
                            //Add(VisualNode(event.x, event.y, shape, selectedText))
                            val selectedNode = App.Link(selectedText);
                            if (!selectedNode.IsFull()) {
                                //TODO: wide stroke
                                Add(VisualNode(event.x, event.y, shape, selectedText, true))
                            }
                            else {
                                Add(VisualNode(event.x, event.y, shape, selectedText))
                            }
                            if(selectedText == "OH") {
                                //TODO: show as 2 ellipses
                            }

                            if(selectedPoint.x != 0f && selectedPoint.y != 0f) {
                                myView.AddLine(PointF(event.x, event.y), selectedPoint)
                            }
                            if(!selectedNode.IsFull()) {
                                selectedPoint = PointF(event.x, event.y)// for next use, remember position for line
                            }
                            if (App.IsComplete()) {
                                textView.text = App.toString(true)
                                Log.d("TOOL_HIT","READY - Pretty print here")
                            }
                        }
                    }

                }

            }

            v?.onTouchEvent(event) ?: true
        }

    }
}