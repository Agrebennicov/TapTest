package com.example.taptest.util

import java.util.regex.Matcher
import java.util.regex.Pattern

class FrameParser {
    private val patter = Pattern.compile("")

    fun getFramesFromRawHtml(rawHtml: String): List<String> {
        // TODO make a proper parser
        return mutableListOf()
//        val tagValues: MutableList<String> = ArrayList()
//        val matcher: Matcher = patter.matcher(rawHtml)
//        while (matcher.find()) {
//            matcher.group(1)?.let { tagValues.add(it) }
//        }
//        return tagValues.distinct()
    }
}