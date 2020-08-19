package com.example.light_client.ui.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.light_client.R
import com.example.light_client.src.services.ImageService
import com.squareup.picasso.Picasso


class ImageGridAdapter(private val context: Context, private val avatarList: List<String>) : RecyclerView.Adapter<ImageGridAdapter.GridItemViewHolder>() {
    lateinit var root: ProfileFragment

    inner class GridItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var squareAvatar: SquareImageView = view.findViewById(R.id.square_avatar_image_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.profile_avatar_item, parent, false)

        return GridItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GridItemViewHolder, position: Int) {
        val pathOfDrawable = avatarList[position]

        Picasso.get()
            .load(pathOfDrawable)
            .resize(500, 500)
            .into(holder.squareAvatar)

        holder.squareAvatar.setOnClickListener {
            val imageBase64 = root.encodeImageTobase64(root.imageListBitmap[position])
            ImageService.upload(imageBase64!!, root.application.user.username, root.application.authService, root.state.profileHandler)

            root.state.avatarPickerFragmentVisibility = false
            root.state.profileFragmentVisibility = true
            root.refreshView()
        }
    }

    override fun getItemCount(): Int {
        return avatarList.size
    }
}