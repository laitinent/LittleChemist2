package com.example.littlechemist2


import java.util.*

class Node(  text:String)
{
    var Text = text.uppercase(Locale.getDefault())
        get() = field
    var Id:Int = 0
        get() {
            TODO()
        }

    var MaxNodes:Int  = 1
        get() = field
    val  Nodes =  mutableListOf<Node>()
        get() = field
    var BondCount:Int = 0;


    init
    {
        SetMaxNodes(Text)
        Id = currentId
        currentId++
    }



    fun  SetMaxNodes( text:String)
    {
        MaxNodes = when(text)     //        switch (text)
        {
            "H" -> 1// break;
            "OH" -> 1
            "CN" -> 1  // TODO: implement cyanide
            "C" -> 4
            "O" -> 2
            "N" -> 3
            else -> 0
        };
    }

    /// <summary>
    /// Add link, if link count not full
    /// </summary>
    /// <param name="n">Node to link</param>
    /// <returns>Number of free node slots left</returns>
    fun   AddLink( n:Node):Int
    {
        val bondsPlus = CheckSpecialBond(Text, n.Text);  // e.g. C-O uses 2x bonds
        //if (Nodes.Count < MaxNodes)
        if ((bondsPlus + BondCount) <= MaxNodes) // was Nodes.Count
        {
            if (!n.Nodes.contains(this))
            {
                n.Nodes.add(this);
                n.AddBonds(bondsPlus);
            }

            Nodes.add(n);
            BondCount += bondsPlus;
        }
        else
        {
            println("No more items can be added to $Text (max. $MaxNodes)");
        }
        return if (IsFull()) // was n.IsFull
        {
            0;
        } else {
            MaxNodes - BondCount;// Nodes.Count;
        }
    }

    fun  AddBonds( n:Int)
    {
        if (n in 1..4) // so far 4+ bonds not supported
        {
            BondCount += n;
        }
    }

    fun   IsFull():Boolean
    {
        //checks also nodes with 2+ bonds
        var cnt = 0;
        for ( n in Nodes)
        {
            cnt += CheckSpecialBond(Text, n.Text);
        }
        return cnt == MaxNodes;
    }




    override fun  toString():String
    {
        return "$Id: $Text ($Nodes.Count) ";
    }

    companion object {
        private var  currentId:Int = 1;
        fun Reset() {
            currentId = 1;
        }

        /// <summary>
        /// Double etc bonding. Param order doesn't matter.
        /// </summary>
        /// <param name="from">First node text</param>
        /// <param name="to">Second node text</param>
        /// <returns>Bond count, default 1</returns>
        fun  CheckSpecialBond(  from:String,  to:String):Int
        {
            if ((from == "C" || to == "C") && from != to)
            {
                //if ((from == "C" && to == "O") || (from == "O" && to == "C"))
                if (to == "O" || from == "O")
                {
                    return 2;
                }
                if (to == "N" || from == "N")
                {
                    return 3;
                }
            }

            return 1;
        }
    }
}

