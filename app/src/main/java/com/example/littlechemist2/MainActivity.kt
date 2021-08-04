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
import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface GitHubService {
    @GET("moleculelist.csv")    //"users/{user}/repos")
    fun listRepos(/*@Path("user") user: String? */): Call<String?>?
}

class MainActivity : AppCompatActivity() {
    lateinit var myView:MyView
    lateinit var textView:TextView
    lateinit var button:Button
    var selected:ToolBoxItem? = null
    lateinit var App : ChainSystem
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
                            selected = ToolHit(event.x, event.y)
                        }
                        else {
                            //Add(VisualNode(event.x, event.y, shape, selectedText))
                            val selectedNode = App.Link(selected!!.text);

                            // latest node is shown with border
                            if (!selectedNode.IsFull()) {
                                ResetStrokes()
                                Add(VisualNode(event.x, event.y, shape, selected!!, true))
                            }
                            else {
                                Add(VisualNode(event.x, event.y, shape, selected!!))
                            }
                            if(selected!!.text == "OH") {
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

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://laitinent.github.io/") //https://laitinent.github.io/moleculelist.csv
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val service: GitHubService = retrofit.create(GitHubService::class.java)
        val repos: Call<String?>?  = service.listRepos()//"octocat")

        repos?.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>?, response: Response<String?>) {
                if (response.isSuccessful) {
                    val responseString: String? = response.body()
                    // todo: do something with the response string
                    App = ChainSystem(responseString!!)
                    Log.d("REPOS", responseString!!)
                }
            }

            override fun onFailure(call: Call<String?>?, t: Throwable?) {}
        })
        //val repos: String = service.listRepos()!!.execute().body()!!.string();

    }
}