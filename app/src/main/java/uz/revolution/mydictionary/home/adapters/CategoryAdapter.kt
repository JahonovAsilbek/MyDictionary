package uz.revolution.mydictionary.home.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import uz.revolution.mydictionary.database.entities.Word
import uz.revolution.mydictionary.home.CategoryItemFragment
import uz.revolution.mydictionary.models.CategoryData

class CategoryAdapter(var data: ArrayList<CategoryData>, fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    override fun getCount(): Int = data.size


    override fun getItem(position: Int): Fragment {
        return CategoryItemFragment.newInstance(data[position].wordList as ArrayList, "")
    }
}