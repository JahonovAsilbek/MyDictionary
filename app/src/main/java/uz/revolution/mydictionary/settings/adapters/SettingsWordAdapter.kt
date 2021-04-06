package uz.revolution.mydictionary.settings.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import uz.revolution.mydictionary.database.entities.Word
import uz.revolution.mydictionary.databinding.SettignsItemWordBinding

class SettingsWordAdapter : RecyclerView.Adapter<SettingsWordAdapter.VH>() {

    private var wordList: ArrayList<Word>? = null
    var onWordPopupClick: OnWordPopupClick? = null

    fun setAdapter(wordList: ArrayList<Word>) {
        this.wordList = wordList
    }

    inner class VH(var settignsItemWordBinding: SettignsItemWordBinding) :
        RecyclerView.ViewHolder(settignsItemWordBinding.root) {

        fun onBind(word: Word) {
            settignsItemWordBinding.word.text = word.word
            settignsItemWordBinding.translation.text = word.translation

            settignsItemWordBinding.wordDots.setOnClickListener {
                onWordPopupClick?.onClick(word, settignsItemWordBinding.wordDots)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            SettignsItemWordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(wordList!![position])
    }

    override fun getItemCount(): Int = wordList!!.size

    interface OnWordPopupClick {
        fun onClick(word: Word, appCompatImageButton: AppCompatImageButton)
    }
}