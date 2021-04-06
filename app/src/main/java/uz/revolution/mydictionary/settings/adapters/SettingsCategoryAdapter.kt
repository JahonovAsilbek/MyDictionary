package uz.revolution.mydictionary.settings.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import uz.revolution.mydictionary.database.entities.Category
import uz.revolution.mydictionary.databinding.SettingsItemCategoryBinding

class SettingsCategoryAdapter : RecyclerView.Adapter<SettingsCategoryAdapter.VH>() {

    private var categoryList: ArrayList<Category>? = null
    var onPopupClick: OnPopupClick? = null

    fun setAdapter(categoryList: ArrayList<Category>) {
        this.categoryList = categoryList
    }

    inner class VH(var settingsItemCategoryBinding: SettingsItemCategoryBinding) :
        RecyclerView.ViewHolder(settingsItemCategoryBinding.root) {

        fun onBind(category: Category, position: Int) {
            settingsItemCategoryBinding.categoryName.text = category.categoryName
            settingsItemCategoryBinding.categoryPopup.setOnClickListener {
                onPopupClick?.onClick(category, position, settingsItemCategoryBinding.categoryPopup)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            SettingsItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(categoryList!![position], position)
    }

    override fun getItemCount(): Int = categoryList!!.size

    interface OnPopupClick {
        fun onClick(category: Category, position: Int, appCompatImageButton: AppCompatImageButton)
    }
}