package uz.revolution.mydictionary.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.revolution.mydictionary.R
import uz.revolution.mydictionary.database.entities.Word
import uz.revolution.mydictionary.databinding.FragmentCategoryItemBinding
import uz.revolution.mydictionary.home.adapters.HomeItemAdapter

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CategoryItemFragment : Fragment() {

    private var wordList: ArrayList<Word>? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            wordList = it.getSerializable(ARG_PARAM1) as ArrayList<Word>
            param2 = it.getString(ARG_PARAM2)
        }
        adapter = HomeItemAdapter()
    }

    lateinit var binding: FragmentCategoryItemBinding
    private var adapter: HomeItemAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryItemBinding.inflate(layoutInflater, container, false)

        setAdapter()
        itemClick()

        return binding.root
    }

    private fun itemClick() {
        // works when clicked item of rv

        adapter?.onItemClick = object : HomeItemAdapter.OnItemClick {
            override fun onClick(word: Word) {
                val bundle = Bundle()
                bundle.putSerializable("word", word)
                findNavController().navigate(R.id.wordFragment, bundle)
            }
        }
    }

    private fun setAdapter() {
        // set data to adapter

        wordList?.let { adapter?.setAdapter(it) }
        binding.innerRv.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(wordList: ArrayList<Word>, param2: String) =
            CategoryItemFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, wordList)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}