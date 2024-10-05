package com.dahlaran.naturaquiz.core

import android.content.Context

class FrontSimpleCallbackImpl(context: Context) : FrontSimpleCallback(context) {

    override fun onSuccess() {
        // Do nothing
    }

    override fun onFailed(repoError: RepoError) {
        context.get()?.let {
            repoError.showUsingCodeOnly(it)
        }
    }
}