package uz.revolution.mydictionary.word.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import uz.revolution.mydictionary.databinding.ItemSpinnerBinding

class SpinnerAdapter(var categoryList: ArrayList<String>) : BaseAdapter() {

    override fun getCount(): Int = categoryList.size

    override fun getItem(p0: Int): String {
        return categoryList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, convertView: View?, parent: ViewGroup?): View {

        val binding =
            ItemSpinnerBinding.inflate(LayoutInflater.from(parent?.context), parent, false)

        binding.categoryName.text = categoryList[p0]

        return binding.root
    }

    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }
}