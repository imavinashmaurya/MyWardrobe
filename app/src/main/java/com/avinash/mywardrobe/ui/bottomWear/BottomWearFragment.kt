package com.avinash.mywardrobe.ui.bottomWear

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.avinash.mywardrobe.R
import com.avinash.mywardrobe.data.room.WearData
import com.avinash.mywardrobe.data.viemodel.WearViewModel
import com.avinash.mywardrobe.ui.DeleteListener
import com.avinash.mywardrobe.ui.ImageOptionBottomSheet
import com.avinash.mywardrobe.ui.OnImageOptionListener
import com.avinash.mywardrobe.ui.WearHolder
import com.avinash.mywardrobe.utility.Constant
import com.avinash.mywardrobe.utility.CustomToast
import com.avinash.mywardrobe.utility.genericRecyclerview.KRecyclerViewAdapter
import com.avinash.mywardrobe.utility.genericRecyclerview.KRecyclerViewHolder
import com.avinash.mywardrobe.utility.genericRecyclerview.KRecyclerViewHolderCallBack
import com.avinash.mywardrobe.utility.genericRecyclerview.KRecyclerViewItemClickListener
import com.avinash.mywardrobe.utility.getBase64
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.fragment_bottom_wear.*

/**
 * A simple [Fragment] subclass.
 * Use the [BottomWearFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BottomWearFragment : Fragment(), View.OnClickListener, OnImageOptionListener, DeleteListener {
    private var wearViewModel: WearViewModel? = null
    private var bottomWearList: ArrayList<WearData>? = ArrayList()
    var adapter: KRecyclerViewAdapter? = null
    var previous = 0

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment BottomWearFragment.
         */
        @JvmStatic
        fun newInstance() = BottomWearFragment()
        const val TAG = "BottomWearFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_wear, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        initViewModel()
        setUpRecyclerView()
        observeData()
    }

    private fun setupUI() {
        fbAddBottomWear?.setOnClickListener(this)
    }

    private fun initViewModel() {
        wearViewModel = ViewModelProvider(this).get(WearViewModel::class.java)
    }

    private fun observeData() {
        var total = 0
        wearViewModel?.getBottomWear()?.observe(viewLifecycleOwner, Observer { itListTopWear ->
            bottomWearList?.clear()
            bottomWearList?.addAll(itListTopWear)
            adapter?.notifyDataSetChanged()
            if (bottomWearList?.size != total) {
                bottomWearList?.size?.let {
                    total = it
                    scroll(it - 1)
                }
            }
            emptyState()
        })

        wearViewModel?.getShuffleEvent()?.observe(viewLifecycleOwner, Observer {
            //bottomWearList?.shuffle()
            //adapter?.notifyDataSetChanged()
            showError()
            checkForRandom()
        })
    }

    private fun checkForRandom() {
        bottomWearList?.size?.let {
            val random = (0..it).random()
            if (previous != random || it == 1) {
                scroll(random)
            } else {
                checkForRandom()
            }
            previous = random

        }
    }

    private fun emptyState() {
        bottomWearList?.size?.let {
            if (it > 0) {
                ivBottom?.visibility = View.GONE
            } else {
                ivBottom?.visibility = View.VISIBLE

            }
        }
    }

    private fun scroll(pos: Int) {
        if (pos >= 0) {
            rvBottom?.smoothScrollToPosition(pos)
        }
    }

    private fun showError() {
        bottomWearList?.size?.let {
            if (it < 1) {
                context?.let {
                    CustomToast().setupErrorToast(
                        it,
                        it.getString(R.string.bottomWearEmptyList)
                    )
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvBottom?.layoutManager = manager
        adapter = bottomWearList?.let {
            KRecyclerViewAdapter(requireContext(), it, object :
                KRecyclerViewHolderCallBack {
                override fun onCreateViewHolder(@NonNull parent: ViewGroup): KRecyclerViewHolder {
                    val layoutView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.row_image, parent, false)
                    return WearHolder(layoutView, this@BottomWearFragment)
                }

                override fun onHolderDisplayed(
                    @NonNull holder: KRecyclerViewHolder,
                    position: Int
                ) {
                    Log.i("onHolderDisplayed", "Holder Displayed At: $position")
                }
            }, KRecyclerViewItemClickListener { holder, itemObject, itemPosition ->
            })
        }
        rvBottom?.adapter = adapter
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rvBottom)

        rvBottom?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount: Int = manager.childCount
                val totalItemCount: Int = manager.itemCount
                val firstVisibleItemPosition: Int = manager.findFirstVisibleItemPosition()
                val lastItem = firstVisibleItemPosition + visibleItemCount
                if (firstVisibleItemPosition >= 0) {
                    bottomWearList?.get(firstVisibleItemPosition)?.let {
                        wearViewModel?.getCurrentBottomWear()?.postValue(it)
                    }
                }
            }
        })
    }

    /**
     * Show image picker.
     * @param camera If true, uses camera else opens gallery.
     */
    private fun pickImage(camera: Boolean) {
        val imagePicker = ImagePicker.with(this)
        if (camera) {
            imagePicker.cameraOnly()
        } else {
            imagePicker.galleryOnly()
        }
        imagePicker.cropSquare()
        imagePicker.compress(1000)

        imagePicker.start { resultCode, data ->
            if (resultCode == Activity.RESULT_OK) {
                ImagePicker.getFilePath(data)?.let { filePath ->
                    val base64 = getBase64(filePath)
                    val wearData = WearData(image = base64, type = Constant.BOTTOM_WEAR)
                    wearViewModel?.insertWearData(wearData)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fbAddBottomWear -> {
                val bottomSheetFragment =
                    ImageOptionBottomSheet.newInstance(this)
                bottomSheetFragment.isCancelable = false
                activity?.supportFragmentManager?.let {
                    bottomSheetFragment.show(
                        it,
                        bottomSheetFragment.tag
                    )
                }
            }
        }
    }

    override fun onImageOptionSelected(option: String) {
        when (option) {
            ImageOptionBottomSheet.CAMERA -> {
                pickImage(true)
            }
            ImageOptionBottomSheet.GALLERY -> {
                pickImage(false)
            }
        }
    }

    override fun onDelete(wearData: WearData) {
        wearViewModel?.deleteWearData(wearData)
    }
}