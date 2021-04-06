package uz.revolution.mydictionary.word

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.revolution.mydictionary.database.AppDatabase
import uz.revolution.mydictionary.database.entities.Word
import uz.revolution.mydictionary.databinding.FragmentAddWordBinding
import uz.revolution.mydictionary.word.adapters.SpinnerAdapter
import java.io.File
import java.io.FileOutputStream

private const val ARG_PARAM1 = "word"
private const val ARG_PARAM2 = "param2"

class AddWord : Fragment() {

    private var word: Word? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            word = it.getSerializable(ARG_PARAM1) as Word?
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentAddWordBinding
    private var uri: Uri? = null
    lateinit var wordList: ArrayList<Word>
    private var absolutePath: String? = null
    private var requestCOde = 1
    private var categoryList: ArrayList<String>? = null

    private val TAG = "AAAA"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddWordBinding.inflate(layoutInflater, container, false)

        setToolbar()
        loadData()

        if (word != null) {
            loadDataToView()
        }

        imageClick()
        saveClick()
        cancelClick()

        return binding.root
    }

    private fun loadDataToView() {
        binding.imageWord.setImageURI(Uri.parse(word?.imagePath))
        binding.spinner.setSelection(getSpinnerPosition(word!!))
        binding.word.setText(word?.word)
        binding.translation.setText(word?.translation)
        absolutePath = word?.imagePath
    }

    private fun getSpinnerPosition(word: Word): Int {
        var position = 0
        for (i in 0 until categoryList!!.size) {
            if (categoryList!![i].equals(word.categoryID?.let {
                    AppDatabase.get.getDatabase().getDao().getCategoryNameByID(
                        it
                    )
                })) {
                position = i
                break
            }
        }
        return position
    }

    private fun cancelClick() {
        binding.cancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun saveClick() {
        binding.save.setOnClickListener {
            val categoryName = categoryList?.get(binding.spinner.selectedItemPosition)
            val word = binding.word.text.toString().trim()
            val translation = binding.translation.text.toString().trim()

            if (categoryName!!.isNotEmpty() && word.isNotEmpty() && translation.isNotEmpty() && absolutePath != null) {

                if (this.word == null) {

                    AppDatabase.get.getDatabase().getDao().insertWord(
                        Word(
                            getCategoryID(categoryName),
                            word,
                            translation,
                            absolutePath,
                            false
                        )
                    ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()

                    Snackbar.make(binding.root, "Muvaffaqiyatli qo'shildi!", Snackbar.LENGTH_LONG)
                        .show()
                    findNavController().popBackStack()
                } else {
                    AppDatabase.get.getDatabase().getDao().updateWord(
                        Word(
                            this.word!!.id,
                            getCategoryID(categoryName),
                            word,
                            translation,
                            absolutePath,
                            false
                        )
                    ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()

                    Snackbar.make(binding.root, "Muvaffaqiyatli o'zgartirildi!", Snackbar.LENGTH_LONG)
                        .show()
                    findNavController().popBackStack()
                }

            } else {
                Snackbar.make(binding.root, "Barcha maydonlarni to'ldiring!", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun getCategoryID(categoryName: String): Int {
        return AppDatabase.get.getDatabase().getDao().getCategoryIdByName(categoryName)
    }

    private fun loadData() {
        wordList = ArrayList()
        wordList = AppDatabase.get.getDatabase().getDao().getAllWord() as ArrayList

        categoryList = ArrayList()
        categoryList?.add("")

        for (i in AppDatabase.get.getDatabase().getDao().getAllCategory().indices) {
            AppDatabase.get.getDatabase().getDao().getAllCategory()[i].categoryName?.let {
                categoryList?.add(
                    it
                )
            }
        }

        val adapter = SpinnerAdapter(categoryList!!)
        binding.spinner.adapter = adapter
    }

    private fun imageClick() {
        binding.imageWord.setOnClickListener {
            askPermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
                //all permissions already granted or just granted

                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(intent, requestCOde)

                Log.d("AAAA", "imageClick: image clicked")

            }.onDeclined { e ->
                if (e.hasDenied()) {

                    AlertDialog.Builder(binding.root.context)
                        .setMessage("Qurilma xotirasiga ruxsat zarur")
                        .setPositiveButton("OK") { dialog, which ->
                            e.askAgain();
                        } //ask again
                        .setNegativeButton("Bekor qilish") { dialog, which ->
                            dialog.dismiss();
                        }
                        .show()
                }

                if (e.hasForeverDenied()) {

                    // you need to open setting manually if you really need it
                    e.goToSettings();
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCOde && resultCode == Activity.RESULT_OK) {
            uri = data?.data ?: return
            binding.imageWord.setImageURI(uri)
            val openInputStream = requireActivity().contentResolver?.openInputStream(uri!!)
            val file: File?
            if (wordList.isEmpty()) {
                file = File(requireActivity().filesDir, "image0.jpg")
            } else {
                file =
                    File(requireActivity().filesDir, "image${wordList[wordList.size - 1].id}.jpg")
            }
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
            absolutePath = file.absolutePath
            Log.d("AAAA", "onActivityResult: $absolutePath")
        }
    }

    private fun setToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

}