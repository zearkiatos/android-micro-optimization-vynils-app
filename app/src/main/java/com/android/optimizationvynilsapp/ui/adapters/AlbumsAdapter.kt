package com.android.optimizationvynilsapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.optimizationvynilsapp.R
import com.android.optimizationvynilsapp.databinding.AlbumItemBinding
import com.android.optimizationvynilsapp.models.Album
import com.android.optimizationvynilsapp.ui.AlbumFragmentDirections

class AlbumsAdapter (coll: Int) : RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>() {
    var collector: Int = coll
    var albums :List<Album> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val withDataBinding: AlbumItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AlbumViewHolder.LAYOUT,
            parent,
            false)
        return AlbumViewHolder(withDataBinding)
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.album = albums[position]
        }
        holder.viewDataBinding.root.setOnClickListener {
            Log.d("args2", albums[position].albumId.toString()+" "+collector)
            val action = AlbumFragmentDirections.actionAlbumFragment2ToCommentsFragment2(albums[position].albumId, collector)
            // Navigate using that action
            holder.viewDataBinding.root.findNavController().navigate(action)
        }
    }

    class AlbumViewHolder(val viewDataBinding: AlbumItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.album_item
        }
    }
}