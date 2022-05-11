package com.example.littlechemist2

import android.graphics.Color
import android.graphics.PointF
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent.ACTION_DOWN
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET


// d:\Android\android-sdk\platform-tools\adb start-server
interface GitHubService {
    @GET("moleculelist.csv")    //"users/{user}/repos")
    fun listRepos(/*@Path("user") user: String? */): Call<String?>?
}

class MainActivity : AppCompatActivity() {
    private lateinit var myView: MyView
    private lateinit var textView: TextView
    private lateinit var button: Button
    private lateinit var toolBox2: RecyclerView
    private var mAdapter: CustomAdapter? = null
    private var selected: ToolBoxItem2? = null//ToolBoxItem? = null
    private lateinit var App: ChainSystem

    //var numbers = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    private var selectedPoint = PointF()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myView = findViewById(R.id.myView)
        textView = findViewById(R.id.textView)
        toolBox2 = findViewById(R.id.recyclerToolView)
        var images =
            arrayOf(  // was R.drawable.yellowball
                ToolBoxItem2("H",Color.YELLOW, R.drawable.yellowball),
                ToolBoxItem2("N",Color.BLUE, R.drawable.blueball),
                ToolBoxItem2("O",Color.RED, R.drawable.redball),
                ToolBoxItem2("C",Color.BLACK, R.drawable.gray),
                ToolBoxItem2("OH",Color.WHITE, R.drawable.lightgray)
            )
        mAdapter = CustomAdapter(images) //TODO:
        {
            //Item on recyclerview of drawables is clicked
            if (App!!.isEmpty() || !(App!!.IsComplete() || App!!.CountAndMatchKnown() != "")) {
                // pick item from toolbar
                //if (event.y < 200f) { selected = ToolHit(event.x, event.y) }
                // data class ToolBoxItem(val text:String, val color:Int)
                //var c = it.color //MyView.getColorFromDrawable(it)
                //var text = it.text //VisualNode.textFromColor(c)
                selected = it//selected = ToolBoxItem(text, c)
                /*when(it) {
                R.drawable.redball -> ToolBoxItem("O",c)
                else -> { ToolBoxItem("N",c)  }
            }*/
            }
        }

// Connect the adapter with the RecyclerView.
        toolBox2.adapter = mAdapter

// Give the RecyclerView a default layout manager.
        toolBox2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Clear-button
        button = findViewById(R.id.button)
        button.setOnClickListener {
            App!!.Clear()
            myView.Clear()
            selectedPoint = PointF()
            textView.text = ""
        }

        // draw size
        val shape = OvalShape()
        shape.resize(90f, 90f)

        myView.setOnTouchListener { v, event ->
            when (event?.action) {
                ACTION_DOWN -> with(myView) {
                    performClick()

                    if (App!!.isEmpty() || !(App!!.IsComplete() || App!!.CountAndMatchKnown() != "")&& selected != null) {
                        // pick item from toolbar
                        //if (event.y < 200f) {
                        //selected = ToolHit(event.x, event.y)
                        //} else {
                        // Use picked tool item
                        //Add(VisualNode(event.x, event.y, shape, selectedText))
                        val selectedNode = App!!.Link(selected!!.text)

                        //TODO: Hydrogen autofill when new >1 link node is selected
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
                        if (App!!.IsComplete() || App!!.CountAndMatchKnown() != "") {
                            textView.text = App!!.toString(true)
                            Log.d("TOOL_HIT", "READY - Pretty print here")
                        }
                    }
                    //    }
                    else {
                        textView.text = "${App!!.toString(true)} Clear to Start Again"
                    }

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
        val repos: Call<String?>? = service.listRepos()//"octocat")

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