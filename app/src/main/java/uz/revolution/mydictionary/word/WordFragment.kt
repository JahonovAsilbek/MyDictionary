package uz.revolution.mydictionary.word

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.revolution.mydictionary.R
import uz.revolution.mydictionary.database.AppDatabase
import uz.revolution.mydictionary.database.entities.Word
import uz.revolution.mydictionary.databinding.FragmentWordBinding

private const val ARG_PARAM1 = "word"
private const val ARG_PARAM2 = "param2"

class WordFragment : Fragment() {

    private var word: Word? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            word = it.getSerializable(ARG_PARAM1) as Word?
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentWordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWordBinding.inflate(layoutInflater, container, false)

        setToolbar()
        loadDataToView()
        likeClick()


        return binding.root
    }

    private fun likeClick() {
        binding.liked.setOnClickListener {
            if (AppDatabase.get.getDatabase().getDao().getWordByID(word?.id!!).liked==true) {
                AppDatabase.get.getDatabase().getDao().updateWord(
                    Word(
                        word?.id,
                        word?.categoryID,
                        word?.word,
                        word?.translation,
                        word?.imagePath,
                        false
                    )
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
                binding.liked.setImageResource(R.drawable.ic_liked)
            } else {
                AppDatabase.get.getDatabase().getDao().updateWord(
                    Word(
                        word?.id,
                        word?.categoryID,
                        word?.word,
                        word?.translation,
                        word?.imagePath,
                        true
                    )
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
                binding.liked.setImageResource(R.drawable.ic_heart2)
            }
        }
    }

    private fun loadDataToView() {
        binding.word.text=word?.word
        binding.translation.text=word?.translation
        if (word?.imagePath != null) {
            binding.wordImage.setImageURI(Uri.parse(word?.imagePath))
        }
        if (word?.liked == true) {
            binding.liked.setImageResource(R.drawable.ic_heart2)
        } else {
            binding.liked.setImageResource(R.drawable.ic_liked)
        }
    }

    private fun setToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}