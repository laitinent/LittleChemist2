package com.example.littlechemist2

import android.graphics.PointF
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent.ACTION_DOWN
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET


interface GitHubService {
    @GET("moleculelist.csv")    //"users/{user}/repos")
    fun listRepos(/*@Path("user") user: String? */): Call<String?>?
}

class MainActivity : AppCompatActivity() {
    private lateinit var myView:MyView
    private lateinit var textView:TextView
    private lateinit var button:Button
    private var selected:ToolBoxItem? = null
    lateinit var App : ChainSystem
    var numbers = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
     private var selectedPoint = PointF()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myView = findViewById(R.id.myView)
        textView = findViewById(R.id.textView)

        // Clear-button
        button = findViewById(R.id.button)
        button.setOnClickListener {
            App.Clear()
            myView.Clear()
            selectedPoint = PointF()
            textView.text=""
        }

        val shape = OvalShape()
        shape.resize(50f,50f)

        myView.setOnTouchListener { v, event ->
            when (event?.action) {
                ACTION_DOWN -> with(myView) {
                    performClick()
                    if (App.isEmpty() || !(App.IsComplete() || App.CountAndMatchKnown() != "")) {
                        // pick item from toolbar
                        if (event.y < 200f) {
                            selected = ToolHit(event.x, event.y)
                        }
                        else {
                            // Use picked tool item
                            //Add(VisualNode(event.x, event.y, shape, selectedText))
                            val selectedNode = App.Link(selected!!.text)

                            // latest node is shown with border
                            if (!selectedNode.IsFull(selected!!.text)) {
                                ResetStrokes()
                                Add(VisualNode(event.x, event.y, shape, selected!!, true))
                            } else {
                                Add(VisualNode(event.x, event.y, shape, selected!!))
                            }
                            if (selected!!.text == "OH") {
                                //TODO: show as 2 ellipses
                            }

                            if (selectedPoint.x != 0f && selectedPoint.y != 0f) {
                                myView.AddLine(PointF(event.x, event.y), selectedPoint)
                            }
                            if (!selectedNode.IsFull()) {
                                selectedPoint = PointF(
                                    event.x,
                                    event.y
                                )// for next use, remember position for line
                            }
                            if (App.IsComplete() || App.CountAndMatchKnown() != "") {
                                textView.text = App.toString(true)
                                Log.d("TOOL_HIT", "READY - Pretty print here")
                            }
                        }
                    }
                    else textView.text = "${App.toString(true)} Clear to Start Again"
                }
            }

            v?.onTouchEvent(event) ?: true
        }

        // Retrieve list of known results from net
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
                    Log.d("REPOS", responseString)
                }
            }

            override fun onFailure(call: Call<String?>?, t: Throwable?) {}
        })
        //val repos: String = service.listRepos()!!.execute().body()!!.string();
    }
}