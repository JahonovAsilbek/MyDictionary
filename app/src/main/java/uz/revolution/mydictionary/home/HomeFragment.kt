package uz.revolution.mydictionary.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.revolution.mydictionary.R
import uz.revolution.mydictionary.database.AppDatabase
import uz.revolution.mydictionary.database.dao.DictionaryDao
import uz.revolution.mydictionary.database.entities.Category
import uz.revolution.mydictionary.database.entities.Word
import uz.revolution.mydictionary.databinding.FragmentHomeBinding
import uz.revolution.mydictionary.home.adapters.CategoryAdapter
import uz.revolution.mydictionary.home.adapters.HomeItemAdapter
import uz.revolution.mydictionary.models.CategoryData

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private val TAG = "AAAA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        getDao = AppDatabase.get.getDatabase().getDao()
        setHasOptionsMenu(true)
    }

    lateinit var binding: FragmentHomeBinding
    private var getDao: DictionaryDao? = null
    private var data: ArrayList<CategoryData>? = null
    private var adapter: CategoryAdapter? = null
    private var likedWordList: ArrayList<Word>? = null
    private var bottomBarPosition = 0
    private var viewPagerPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)


        loadAllData()
        bottomBarClick()
        editClick()


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        when (bottomBarPosition) {
            0 -> {
                loadAllData()
                binding.viewPager.currentItem = viewPagerPosition
            }
            1 -> loadLikedData()
        }
        binding.bottomNavigation.selectTabAt(bottomBarPosition, true)
    }

    private fun loadAllData() {
        binding.likedRv.visibility = View.GONE
        binding.tabLayout.visibility = View.VISIBLE
        binding.viewPager.visibility = View.VISIBLE
        loadData()
        loadAdapter()
    }

    private fun loadLikedData() {
        bottomBarPosition = 1
        binding.viewPager.visibility = View.GONE
        binding.tabLayout.visibility = View.GONE
        binding.likedRv.visibility = View.VISIBLE
        likedWordList = ArrayList()
        loadData()

        for (i in 0 until data!!.size) {
            for (j in 0 until data!![i].wordList.size) {
                if (data!![i].wordList[j].liked == true) {
                    likedWordList?.add(data!![i].wordList[j])
                }
            }
        }
        val adapter = HomeItemAdapter()
        adapter.setAdapter(likedWordList!!)
        binding.likedRv.adapter = adapter

        adapter.onItemClick = object : HomeItemAdapter.OnItemClick {
            override fun onClick(word: Word) {
                val bundle = Bundle()
                bundle.putSerializable("word", word)
                findNavController().navigate(R.id.wordFragment, bundle)
                bottomBarPosition = binding.bottomNavigation.selectedIndex
            }
        }
    }

    private fun bottomBarClick() {
        binding.bottomNavigation.onTabSelected = {
            when (it.id) {
                R.id.main -> {
                    loadAllData()
                    bottomBarPosition = 0
                }
                R.id.liked -> {
                    loadLikedData()
                    bottomBarPosition = 1
                }
            }
        }
        binding.bottomNavigation.onTabReselected = {
            when (it.id) {
                R.id.main -> {
                    loadAllData()
                    bottomBarPosition = 0
                }
                R.id.liked -> {
                    loadLikedData()
                    bottomBarPosition = 1
                }
            }
        }
    }

    private fun editClick() {
        binding.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.home_edit) {
                val bundle = Bundle()
                bundle.putString("string", "bir narsa")
                findNavController().navigate(R.id.settingsFragment, bundle)
            }
            true
        }
    }

    private fun loadAdapter() {
        adapter = data?.let { CategoryAdapter(it, childFragmentManager) }
        binding.viewPager.adapter = adapter
        setTabs()

        binding.viewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                viewPagerPosition = position
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }

    private fun setTabs() {
        binding.tabLayout.setViewPager(binding.viewPager, getTabLayoutTitles())
        binding.tabLayout.currentTab = binding.tabLayout.currentTab
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_edit_btn, menu)
    }

    private fun loadData() {
        data = ArrayList()

        if (getDao!!.getAllCategory().isEmpty()) {
            getDao!!.insertCategory(Category("Technology"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
            getDao!!.insertCategory(Category("Sport"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
            getDao!!.insertCategory(Category("News"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
            getDao!!.insertCategory(Category("LifeStyle"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
            getDao!!.insertCategory(Category("Nature"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
            getDao!!.insertCategory(Category("People"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
            getDao!!.insertCategory(Category("Film"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
            getDao!!.insertCategory(Category("Business"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        }

        for (i in getDao!!.getAllCategory().indices) {
            data?.add(
                CategoryData(
                    getDao!!.getAllCategory()[i],
                    getDao!!.getAllCategory()[i].id?.let { getDao!!.getWordByCategoryID(it) } as ArrayList
                )
            )
        }

        Log.d(TAG, "loadData: ${data?.size}")

    }

    private fun getTabLayoutTitles(): Array<String?> {
        val array = arrayOfNulls<String>(data!!.size)
        for (i in 0 until data!!.size) {
            array[i] = data!![i].category.categoryName.toString()
        }
        return array
    }
}