package com.avinash.mywardrobe.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.avinash.mywardrobe.R
import com.avinash.mywardrobe.data.room.WearData
import com.avinash.mywardrobe.data.viemodel.WearViewModel
import kotlinx.android.synthetic.main.activity_wardrobe.*

class WardrobeActivity : AppCompatActivity(), View.OnClickListener {
    private var wearViewModel: WearViewModel? = null
    private var total = 0
    private var top = 0
    private var bottom = 0
    private var currentTopWear: WearData? = null
    private var currentBottomWear: WearData? = null
    private var favFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wardrobe)
        setupUI()
        initViewModel()
        observeData()
        setFavUI()
    }

    private fun setupUI() {
        fbRandom?.setOnClickListener(this)
        fbFav?.setOnClickListener(this)
    }

    private fun initViewModel() {
        wearViewModel = ViewModelProvider(this).get(WearViewModel::class.java)
    }

    private fun observeData() {
        wearViewModel?.getCount()?.observe(this@WardrobeActivity, Observer {
            total = it
            changeTitle()
        })
        wearViewModel?.getTopCount()?.observe(this@WardrobeActivity, Observer {
            top = it
            changeTitle()
        })
        wearViewModel?.getBottomCount()?.observe(this@WardrobeActivity, Observer {
            bottom = it
            changeTitle()
        })
        wearViewModel?.getCurrentTopWear()?.observe(this@WardrobeActivity, Observer {
            currentTopWear = it
            setFavUI()
        })
        wearViewModel?.getCurrentBottomWear()?.observe(this@WardrobeActivity, Observer {
            currentBottomWear = it
            setFavUI()
        })
    }

    private fun setFavUI() {
        currentTopWear?.let { itTop ->
            currentBottomWear?.let { itBottom ->
                if (itTop.pairingIdList?.contains(itBottom.id)!!) {
                    favFlag = true
                    fbFav?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fav))
                } else {
                    favFlag =false
                    fbFav?.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_fav_unselected
                        )
                    )
                }
            }
        }
    }

    private fun addFav() {
        currentTopWear?.let { itTop ->
            currentBottomWear?.let { itBottom ->
                itBottom.id?.let {
                    itTop.pairingIdList?.add(it)
                    wearViewModel?.updateWearData(itTop)
                }
            }
        }
    }
    private fun removeFav() {
        currentTopWear?.let { itTop ->
            currentBottomWear?.let { itBottom ->
                itBottom.id?.let {
                    itTop.pairingIdList?.remove(it)
                    wearViewModel?.updateWearData(itTop)
                }
            }
        }
    }

    private fun changeTitle() {
        title = "MyWardrobe - Total:${total} - Top:${top} - Bottom:${bottom}"
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fbFav -> {
                if(favFlag){
                    removeFav()
                }else{
                    addFav()
                }
                setFavUI()
            }
            R.id.fbRandom -> {
                wearViewModel?.getShuffleEvent()?.postValue(true)
            }
        }
    }
}