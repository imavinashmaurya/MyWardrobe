package com.avinash.mywardrobe.ui

import android.content.Context
import android.util.Base64
import android.view.View
import com.avinash.mywardrobe.R
import com.avinash.mywardrobe.data.room.WearData
import com.avinash.mywardrobe.utility.Constant
import com.avinash.mywardrobe.utility.CustomAlertDialog
import com.avinash.mywardrobe.utility.genericRecyclerview.KRecyclerViewHolder
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.row_image.view.*

interface DeleteListener {
    fun onDelete(wearData: WearData)
}

class WearHolder(itemView: View, val deleteListener: DeleteListener?) :
    KRecyclerViewHolder(itemView) {

    override fun setData(context: Context, itemObject: Any) {
        super.setData(context, itemObject)
        if (itemObject is WearData) {
            val imageByteArray = Base64.decode(itemObject.image, Base64.DEFAULT)
            var placeholder = R.drawable.ic_shirt
            if (itemObject.type == Constant.BOTTOM_WEAR) {
                placeholder = R.drawable.ic_trousers
            }
            itemView.tvPos?.text = "${adapterPosition + 1}"
            Glide.with(context).load(imageByteArray).placeholder(placeholder).into(itemView.ivWear)

            itemView.ivDelete?.setOnClickListener {
                CustomAlertDialog(
                    context,
                    context.getString(R.string.deleteMessage),
                    true,
                    object : CustomAlertDialog.AlertDialogButtonClicked {
                        override fun buttonClicked(positive: Boolean) {
                            if (positive) {
                                deleteListener?.onDelete(itemObject)
                            }
                        }
                    })
            }
        }
    }

}