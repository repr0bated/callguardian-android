package com.example.callguardian

import com.example.callguardian.methods.MethodRegistry
import java.io.File

/** Unit tests read the real shipped schemas from src/main/assets/schemas. */
object TestAssets {
    val dir: File = listOf(File("src/main/assets/schemas"), File("app/src/main/assets/schemas")).first { it.isDirectory }
    fun read(path: String): String = File(dir, path).readText()
    val registry by lazy { MethodRegistry(::read) }
}
