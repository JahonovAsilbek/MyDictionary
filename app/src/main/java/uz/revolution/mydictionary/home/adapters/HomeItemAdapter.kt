package uz.revolution.mydictionary.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.revolution.mydictionary.database.entities.Word
import uz.revolution.mydictionary.databinding.HomeItemWordBinding

class HomeItemAdapter : RecyclerView.Adapter<HomeItemAdapter.VH>() {

    private var wordList:ArrayList<Word>?=null
    var onItemClick:OnItemClick?=null

    fun setAdapter(wordList: ArrayList<Word>) {
        this.wordList=wordList
    }

    inner class VH(var homeItemWordBinding: HomeItemWordBinding) :
        RecyclerView.ViewHolder(homeItemWordBinding.root) {

        fun onBind(word: Word) {
            homeItemWordBinding.word.text=word.word
            homeItemWordBinding.translation.text=word.translation

            homeItemWordBinding.root.setOnClickListener {
                onItemClick?.onClick(word)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(HomeItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(wordList!![position])
    }

    override fun getItemCount(): Int =wordList!!.size

    interface OnItemClick{
        fun onClick(word: Word)
    }
}