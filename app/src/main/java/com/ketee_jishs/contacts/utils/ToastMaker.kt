package com.ketee_jishs.contacts.utils

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(string: String?) {
    Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
}