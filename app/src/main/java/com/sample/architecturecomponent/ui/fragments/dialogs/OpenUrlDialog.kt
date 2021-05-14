package com.sample.architecturecomponent.ui.fragments.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.managers.extensions.showBrowser
import com.sample.architecturecomponent.ui.fragments.base.BaseDialogFragment

class OpenUrlDialog : BaseDialogFragment(), DialogInterface.OnClickListener {

    private val url: String by lazy {
        OpenUrlDialogArgs.fromBundle(requireArguments()).url
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext()).apply {
            setTitle(R.string.text_open_url)
            setMessage(url)
            setPositiveButton(R.string.text_open, this@OpenUrlDialog)
            setNegativeButton(R.string.text_cancel, this@OpenUrlDialog)
        }.create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when (which) {
            Dialog.BUTTON_NEGATIVE -> navigate.navigateUp()
            Dialog.BUTTON_POSITIVE -> requireContext().showBrowser(url)
        }
    }

}