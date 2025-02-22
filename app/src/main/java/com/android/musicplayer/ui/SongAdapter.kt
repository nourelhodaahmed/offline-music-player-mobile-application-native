package com.android.musicplayer.ui


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.musicplayer.R
import com.android.musicplayer.data.domain.Song
import com.android.musicplayer.databinding.SongItemMainactivityBinding

class SongAdapter(val songList: List<Song>, val listener: SongListInteractionListener): RecyclerView.Adapter<SongAdapter.SongViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.song_item_mainactivity,
            parent,
            false
        )
        return SongViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.binding.apply {
            songNameText.text = songList[position].name

            root.setOnClickListener{
                listener.onClickItem(position)
            }
        }
    }

    class SongViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val binding = SongItemMainactivityBinding.bind(itemView)
    }
}