package uz.revolution.mydictionary.settings

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.revolution.mydictionary.R
import uz.revolution.mydictionary.database.AppDatabase
import uz.revolution.mydictionary.database.entities.Category
import uz.revolution.mydictionary.database.entities.Word
import uz.revolution.mydictionary.databinding.AddCategoryDialogBinding
import uz.revolution.mydictionary.databinding.DeleteCategoryDialogBinding
import uz.revolution.mydictionary.databinding.FragmentSettingsBinding
import uz.revolution.mydictionary.settings.adapters.SettingsCategoryAdapter
import uz.revolution.mydictionary.settings.adapters.SettingsWordAdapter

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SettingsFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentSettingsBinding
    private var categoryList: ArrayList<Category>? = null
    private var wordList: ArrayList<Word>? = null
    private var adapter: SettingsCategoryAdapter? = null
    private var wordAdapter: SettingsWordAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)

        setToolBar()
        bottomNavigationClick()
        loadCategoryData()

        return binding.root
    }

    private fun bottomNavigationClick() {
        binding.bottomNavigation.onTabSelected = {
            when (it.id) {
                R.id.category -> {
                    loadCategoryData()
                }
                R.id.words -> {
                    loadWordData()
                }
            }
        }
        binding.bottomNavigation.onTabReselected = {
            when (it.id) {
                R.id.category -> {
                    loadCategoryData()
                }
                R.id.words -> {
                    loadWordData()
                }
            }
        }
    }

    private fun loadWordData() {
        binding.categoryRv.visibility = View.GONE
        binding.wordRv.visibility = View.VISIBLE
        wordList = ArrayList()
        wordList = AppDatabase.get.getDatabase().getDao().getAllWord() as ArrayList
        wordAdapter = SettingsWordAdapter()
        wordAdapter?.setAdapter(wordList!!)
        binding.wordRv.adapter = wordAdapter

        wordAdapter?.onWordPopupClick = object : SettingsWordAdapter.OnWordPopupClick {
            override fun onClick(word: Word, appCompatImageButton: AppCompatImageButton) {
                val popup = PopupMenu(binding.root.context, appCompatImageButton)
                popup.inflate(R.menu.popup_menu)

                popup.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.edit_category -> {
                            val bundle = Bundle()
                            bundle.putSerializable("word", word)
                            findNavController().navigate(R.id.addWord, bundle)
                        }
                        R.id.delete_category -> {
                            deleteWord(word)
                        }
                    }

                    true
                }

                popup.show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun deleteWord(word: Word) {
        val dialog = AlertDialog.Builder(binding.root.context, R.style.RoundedCornersDialog)
        val dialogView = DeleteCategoryDialogBinding.inflate(layoutInflater, null, false)
        dialogView.title.text = "Bu so'zni o'chirmoqchimisiz?"
        dialog.setView(dialogView.root)
        val alertDialog = dialog.create()
        dialogView.cancel.setOnClickListener {
            alertDialog.cancel()
            alertDialog.dismiss()
        }
        dialogView.ok.setOnClickListener {
            AppDatabase.get.getDatabase().getDao().deleteWord(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
            alertDialog.cancel()
            Snackbar.make(
                binding.root,
                "O'chirildi!",
                Snackbar.LENGTH_LONG
            ).show()
            loadWordData()
        }
        alertDialog.show()
    }

    private fun setToolBar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.setOnMenuItemClickListener {
            if (binding.categoryRv.isVisible) {

                val dialog = AlertDialog.Builder(binding.root.context, R.style.RoundedCornersDialog)
                val dialogView = AddCategoryDialogBinding.inflate(layoutInflater, null, false)
                dialog.setView(dialogView.root)
                val alertDialog = dialog.create()
                dialogView.cancel.setOnClickListener {
                    alertDialog.cancel()
                    alertDialog.dismiss()
                }
                dialogView.save.setOnClickListener {
                    val categoryName = dialogView.edittext.text.toString().trim()
                    if (categoryName.isNotEmpty()) {

                        if (checkCategory(categoryName)) {

                            AppDatabase.get.getDatabase().getDao()
                                .insertCategory(Category(categoryName))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe()
                            Snackbar.make(
                                binding.root,
                                "Muvaffaqiyatli qo'shildi!",
                                Snackbar.LENGTH_LONG
                            ).show()
                            alertDialog.cancel()

                            loadCategoryData()

                        } else {
                            Toast.makeText(
                                binding.root.context,
                                "$categoryName nomli kategoriya mavjud!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        Toast.makeText(
                            binding.root.context,
                            "Bo'sh maydonni to'ldiring!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                alertDialog.show()

            } else {
                findNavController().navigate(R.id.addWord)
            }
            true
        }
    }

    private fun loadCategoryData() {
        binding.categoryRv.visibility = View.VISIBLE
        binding.wordRv.visibility = View.GONE
        categoryList = ArrayList()
        categoryList = AppDatabase.get.getDatabase().getDao().getAllCategory() as ArrayList
        adapter = SettingsCategoryAdapter()
        adapter?.setAdapter(categoryList!!)
        binding.categoryRv.adapter = adapter

        adapter?.onPopupClick = object : SettingsCategoryAdapter.OnPopupClick {
            override fun onClick(category: Category, position: Int, button: AppCompatImageButton) {
                val popup = PopupMenu(binding.root.context, button)
                popup.inflate(R.menu.popup_menu)
                popup.setOnMenuItemClickListener {

                    when (it.itemId) {
                        R.id.edit_category -> {
                            editCategory(category)
                        }
                        R.id.delete_category -> {
                            deleteCategory(category)
                        }
                    }

                    true
                }

                popup.show()
            }
        }
    }

    private fun deleteCategory(category: Category) {
        val dialog = AlertDialog.Builder(binding.root.context, R.style.RoundedCornersDialog)
        val dialogView = DeleteCategoryDialogBinding.inflate(layoutInflater, null, false)
        dialog.setView(dialogView.root)
        val alertDialog = dialog.create()
        dialogView.cancel.setOnClickListener {
            alertDialog.cancel()
            alertDialog.dismiss()
        }
        dialogView.ok.setOnClickListener {
            AppDatabase.get.getDatabase().getDao().deleteCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
            alertDialog.cancel()
            Snackbar.make(
                binding.root,
                "O'chirildi!",
                Snackbar.LENGTH_LONG
            ).show()
            loadCategoryData()
        }
        alertDialog.show()
    }

    private fun editCategory(category: Category) {
        val dialog = AlertDialog.Builder(binding.root.context, R.style.RoundedCornersDialog)
        val dialogView = AddCategoryDialogBinding.inflate(layoutInflater, null, false)
        dialog.setView(dialogView.root)
        val alertDialog = dialog.create()
        dialogView.edittext.setText(category.categoryName)
        dialogView.cancel.setOnClickListener {
            alertDialog.cancel()
            alertDialog.dismiss()
        }
        dialogView.save.setOnClickListener {
            val categoryName = dialogView.edittext.text.toString().trim()
            if (categoryName.isNotEmpty()) {

                if (checkCategory(categoryName) == !categoryName.equals(
                        category.categoryName,
                        true
                    )
                ) {

                    AppDatabase.get.getDatabase().getDao()
                        .updateCategory(Category(category.id, categoryName))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
                    Snackbar.make(
                        binding.root,
                        "Muvaffaqiyatli o'zgartirildi!",
                        Snackbar.LENGTH_LONG
                    ).show()
                    alertDialog.cancel()

                    loadCategoryData()

                } else {
                    Toast.makeText(
                        binding.root.context,
                        "$categoryName nomli kategoriya mavjud!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                Toast.makeText(
                    binding.root.context,
                    "Bo'sh maydonni to'ldiring!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        alertDialog.show()
    }

    private fun checkCategory(categoryName: String): Boolean {
        var check = true

        for (i in AppDatabase.get.getDatabase().getDao().getAllCategory().indices) {
            if (categoryName.equals(
                    AppDatabase.get.getDatabase().getDao().getAllCategory()[i].categoryName, true
                )
            ) {
                check = false
                break
            }
        }
        return check
    }

}