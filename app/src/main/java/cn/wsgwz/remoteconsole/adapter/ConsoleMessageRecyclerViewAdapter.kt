package cn.wsgwz.remoteconsole.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.wsgwz.remoteconsole.bean.ConsoleMessageBean
import cn.wsgwz.remoteconsole.R

/**
 * Created by admin on 2017/11/24 0024.
 */
class ConsoleMessageRecyclerViewAdapter(context:Context ,val data:List<ConsoleMessageBean> ): RecyclerView.Adapter<ConsoleMessageRecyclerViewAdapter.ViewHolder>() {


    var inflater:LayoutInflater? = null
    init {
        inflater = LayoutInflater.from(context)
    }
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val bean: ConsoleMessageBean? = data.get(position)
        holder?.TV?.text = bean?.type.toString()+" "+bean?.msg
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(inflater?.inflate(R.layout.console_message_item,parent,false))
    }

    inner class ViewHolder (itemView: View?): RecyclerView.ViewHolder (itemView){
        var TV:TextView? = itemView?.findViewById<TextView>(R.id.TV)
    }




}