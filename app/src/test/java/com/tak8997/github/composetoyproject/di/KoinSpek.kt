package com.tak8997.github.composetoyproject.di

import com.tak8997.github.composetoyproject.StringUtils
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.Root

class KoinRoot(val root: Root) : Root by root

abstract class KoinSpek(koinSpec: KoinRoot.() -> Unit) : Spek({
    koinSpec.invoke(KoinRoot(this))
})
