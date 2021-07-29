package com.example.littlechemist2

import java.util.*


class ChainSystem {
    val Chain = mutableListOf<Node>();


    fun Clear() {
        Chain.clear();
    }
    /*
    public void Add(string s)
    {
        var n = new Node(s);

        Chain.Add(n);
        previous = n;
    }*/

    /** Link and add
     ** @param s - new node text **/
    fun Link(s: String): Node {
        val n = Node(s);
        var free: Int = -1; // 0 means no mode left, -1 = first in chain

        if (Chain.count() > 0) {
            free = previous.AddLink(n);
        } else {
            previous = n;
        }

        // TODO: find way to get this correct every time, maybe one with biggest maxnodes?
        if (n.MaxNodes > 1 && free == 0) {
            previous = n; }

        /* not used, using OH  type instead
        // O+H -> OH when last
        if(false)//Chain.Count > 2 && IsComplete() && previous.Text == "O" && n.Text == "H")
        {
            // TODO: CH3OH -> C still has extra O (changes to extra OH)
            // Replace O with OH, don't add H
            Node ohnode = new("OH");
            // previous = "O"
            if (previous?.Nodes.Count > 0) {
                ohnode.AddLink(previous?.Nodes[0]); // Nodes[0] links back
                //TODO: fix
                var index = previous.Nodes[0].Nodes.FindIndex(n => n.Id == ohnode.Id);
                if(index >= 0)
                {
                    previous.Nodes[0].Nodes[index] = ohnode;
                }
                //if (!previous?.Nodes[0].Nodes.Contains(ohnode))
                //{
                //previous?.Nodes[0].Nodes.Add(ohnode);
                //}
                // replace node when someone links to "O"
                var masternode = FindNode(previous.Id);
                var index2 = masternode.Nodes.FindIndex(n=> n.Id ==previous.Id);
                masternode.Nodes[index2] = ohnode;
            }
            Chain[^1] = ohnode;

        }
        else
        {
        */
        Chain.add(n);
        //}
        return n;
    }

    /** Check, which node has link to specified id
     * @param id Id to search in nodes links
     * @returns Node, null otherwise **/
    private fun FindNode(id: Int): Node? {
        var retval: Node?;
        for (item in Chain) {
            retval = item.Nodes.find { it.Id == id };
            if (retval != null) {
                return item;
            }
        }

        return null;
    }

    /** toString
     *
     **/
    override fun toString(): String {
        var s = "";

        //foreach (var n in Chain)
        for (i in Chain.indices)
        //for (var i = 0; i < Chain.Count; i++)
        {
            val n: Node = Chain[i];
            s += "$i. $n.Text (";
            for (l in n.Nodes) {
                s += "$l.Text ";
            }
            s += ") ";
        }

        return s;
    }

    // how to print via links

    /** Link to selected item
     * @param sel Item index
     * @param v New linked item text**/
    fun Link(sel: Int, v: String) {
        if (sel < Chain.count()) {
            previous = Chain[sel];
            Link(v);
        }
    }

    /** Print like H2O
     * @param formatted - true for pretty print
     * @returns formatted string **/
    fun toString(formatted: Boolean): String// = false)
    {
        var s = "";
        if (formatted) {
            val counts = mutableMapOf<String, Int>()
            //numbersMap.put("three", 3)
            //numbersMap["one"] = 11

            //val counts = dictionaryOf<string, int>();

            // count
            for (n in Chain) {
                if (!counts.containsKey(n.Text)) {
                    counts[n.Text] = 1;
                } else {
                    counts[n.Text] = counts[n.Text]!!.plus(1);
                }
            }

            for (item in counts) {
                s += if (item.value > 1) {
                    "${item.key}${item.value}";
                } else {
                    "${item.key}";
                }
            }
            s = MatchKnown(s, counts);
            if (IsComplete()) {
                s += " READY "; }
            return s;
        } else {
            return toString();
        }
    }

    /// <summary>
    /// Is every possible bond used
    /// </summary>
    /// <returns>true / false</returns>
    fun IsComplete(): Boolean {
        var retval = true;
        Chain.forEach { retval = (retval and it.IsFull()) };
        return retval;
    }


    val list = mutableListOf<FormulaItem>();

    private fun MatchKnown(s: String, counts: MutableMap<String, Int>): String {
        for (item in counts) {
            list.add(FormulaItem(item.key, item.value));
        }
        val list2 = list.sortedWith(compareBy { it.Code }) // not used?

        val ss = ParseNodeText(s);
        //TODO: C2H5OH vs C2H6O
        val v: String = when (ss) {
            "H2O" -> " (Vesi)"
            "OH2" -> "H2O (Vesi)"
            "CO2" -> "Hiilidioksidi"
            "CH2O" -> "Formaldehydi"
            "C2H6" -> "Etaani"
            "C2H5OH" -> "Etanoli"
            "C3H6O" -> "Asetoni"
            "CH4" -> "Metaani"
            "C3H8" -> "Propaani"  // lines from 2nd C->H mismatch
            //"CH3CH2CH3" => "Propaani",
            "C3OH" -> "Metanoli"
            "NH3" -> "Ammoniakki"
            "NH2OH" -> "Hydroksyyliamiini"
            "CHN" -> "Syaanivety/ Sinihappo"
            "CNH" -> "Syaanivety/ Sinihappo"
            "CH3CN" -> "Asetonitriili"
            else -> s
        };
        return v;
    }


    companion object {
        private lateinit var previous: Node; // TODO: bad

        /// <summary>
        /// TODO: sort node text for simpler matching
        /// </summary>
        /// <param name="s"></param>
        /// <returns></returns>
        private fun ParseNodeText(s: String): String {
            //TODO: parsenodetext
            return s;
        }
    }

    data class FormulaItem(var Code: String, var Count: Int) //: IComparable

}


