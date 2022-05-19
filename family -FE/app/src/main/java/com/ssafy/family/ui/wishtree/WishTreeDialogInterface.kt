package com.ssafy.family.ui.wishtree

interface WishTreeDialogInterface {

    fun onCreateBtnClicked(content: String)

    fun onUpdateBtnClicked(myWishListId: Int, content: String)

    fun onDeleteBtnClicked(wishTreeId: Int)

}
