package com.tak8997.github.composetoyproject

import android.util.Patterns
import java.util.*

class StringUtils {

    companion object {

        fun isEmail(str: CharSequence): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(str).matches()
        }

        fun isEmpty(str: String?): Boolean {
            return str == null || str.trim { it <= ' ' }.isEmpty()
        }

        fun isPresent(str: String?): Boolean {
            return !isEmpty(str)
        }

        fun isValidPassword(str: String?): Boolean {
            return !isEmpty(str) && str!!.length > 5
        }

        /**
         * Returns a string with only the first character capitalized.
         */
        fun sentenceCase(str: String): String {
            return if (str.length <= 1) str.toUpperCase(Locale.getDefault()) else str.substring(
                0,
                1
            ).toUpperCase(Locale.getDefault()) + str.substring(1)
                .toLowerCase(Locale.getDefault())
        }

        /**
         * Returns a string with no leading or trailing whitespace.
         */
        fun trim(str: String): String {
            return str.replace('\u00A0', ' ').trim { it <= ' ' }.replace(" +".toRegex(), " ")
        }

        /**
         * Returns a string wrapped in parentheses.
         */
        fun wrapInParentheses(str: String): String {
            return "($str)"
        }
    }
}