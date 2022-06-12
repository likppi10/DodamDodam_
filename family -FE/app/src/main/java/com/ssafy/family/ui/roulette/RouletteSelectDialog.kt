package com.ssafy.family.ui.roulette

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.MemberInfo
import com.ssafy.family.ui.Adapter.RouletteSelectAdapter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class RouletteSelectDialog(var context: Context, var selectedList: HashMap<Long, Boolean>, var memberList: MutableList<MemberInfo>) {

    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener
    private lateinit var familySelectAdapter: RouletteSelectAdapter
    var iterData = selectedList.iterator()

    fun setOnClickListener(listener: OnDialogClickListener) {
        onClickListener = listener
    }

    private fun setRecyclerViewClickListener(){
        familySelectAdapter.itemClickListener = object : RouletteSelectAdapter.ItemClickListener{
            override fun onClick(item: MemberInfo) {
                var size = 0;
                iterData = selectedList.iterator()
                while(iterData.hasNext()){
                    var result = iterData.next()
                    if(result.value){
                        size++
                    }
                }
                if(size>2){
                    selectedList[item.profileId] = selectedList[item.profileId] != true
                    familySelectAdapter.notifyDataSetChanged()
                }else{
                    if(selectedList[item.profileId] == false){
                        selectedList[item.profileId] = true
                        familySelectAdapter.notifyDataSetChanged()
                    }else{
                        Toast.makeText(context,"최소 2인 이상이여야해요", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    fun showDialog() {

        dialog.setContentView(R.layout.select_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Objects.requireNonNull( dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)))
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        familySelectAdapter = RouletteSelectAdapter(context)
        familySelectAdapter.datas = memberList
        familySelectAdapter.selected = selectedList

        val recycle = dialog.findViewById<RecyclerView>(R.id.roulette_family_recycler)
        val cancle = dialog.findViewById<TextView>(R.id.cancle_dialog_text)
        val ok = dialog.findViewById<TextView>(R.id.ok_dialog_text)

        recycle.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            adapter = familySelectAdapter
        }
        setRecyclerViewClickListener()

        cancle.setOnClickListener {
            onClickListener.onClosed()
            dialog.dismiss()
        }

        ok.setOnClickListener {
            onClickListener.onClicked(selectedList)
            dialog.dismiss()
        }

    }

    interface OnDialogClickListener {
        fun onClicked(selectedList: HashMap<Long, Boolean>)
        fun onClosed()
    }

}