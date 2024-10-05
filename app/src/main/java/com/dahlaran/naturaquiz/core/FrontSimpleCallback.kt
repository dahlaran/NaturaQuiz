package com.dahlaran.naturaquiz.core

import android.content.Context
import androidx.annotation.UiThread
import java.lang.ref.WeakReference

abstract class FrontSimpleCallback(context: Context) {

    val context: WeakReference<Context?> = WeakReference(context)

    @UiThread
    abstract fun onSuccess()

    @UiThread
    abstract fun onFailed(repoError: RepoError)
}
