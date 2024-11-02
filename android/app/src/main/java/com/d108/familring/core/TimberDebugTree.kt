package com.d108.familring.core

import timber.log.Timber

class TimberDebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String? =
        "${element.fileName}:${element.lineNumber} #${element.methodName}"
}
