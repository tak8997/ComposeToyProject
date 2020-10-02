package com.tak8997.github.composetoyproject

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    @Test
    fun addition_isCorrect() {
        val users = listOf(
            User(
                listOf(
                    UserName(
                        listOf("name00", "name01", "name02", "name03", "name04")
                    ),
                    UserName(
                        listOf("name10", "name11", "name12", "name13", "name14")
                    )
                )
            )
        )

        users
            .flatMap { it.userNames }
            .flatMap { it.names }
            .map {
                println(it)
                checkOneZero(it)
            }
            .let {
                println(it)
            }

//            .let {
//                println(it)
//                checkZeroZero(it)
//            }
//            .apply {
//                println(this)
//                checkOneZero(this)
//            }
    }

    private fun checkOneZero(name: String): Boolean {
        return name.contains("00")
    }

    private fun checkZeroZero(name: String): Boolean {
        return name == "name00"
    }
}

data class User(
    val userNames: List<UserName>
)

data class UserName(
    val names: List<String>
)