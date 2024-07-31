package com.example.firestorage.ui.xml.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firestorage.databinding.ItemGalleryBinding

class GalleryAdapter (private val images :MutableList<String> = mutableListOf()) : RecyclerView.Adapter<GalleryViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
       val binding = ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalleryViewHolder(binding)
    }

    override fun getItemCount(): Int  = images.size



    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(images[position])
    }

    fun updateImages(newImages: List<String>){  // función para actualizar las imágenes
        images.clear()  // limpiar la lista de imágenes
        images.addAll(newImages)// añadir las nuevas imágenes a la lista
        notifyDataSetChanged() // notificar al adaptador que los datos han cambiado
    }

}


class GalleryViewHolder (private val binding: ItemGalleryBinding ): RecyclerView.ViewHolder(binding.root) {

    fun bind(image: String) {
        Glide.with(binding.ivImage.context)
            .load(image)
            .into(binding.ivImage)
    }


}