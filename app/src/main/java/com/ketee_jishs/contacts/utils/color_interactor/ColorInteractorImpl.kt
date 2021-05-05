package com.ketee_jishs.contacts.utils.color_interactor

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.ketee_jishs.contacts.R

class ColorInteractorImpl (context: Context) : ColorInteractor {
    @RequiresApi(Build.VERSION_CODES.M)
    override val inputtedTextColor = context.getColor(R.color.color_primary)
}