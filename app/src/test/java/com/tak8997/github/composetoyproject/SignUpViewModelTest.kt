package com.tak8997.github.composetoyproject

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class SignUpViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val viewModel = SignUpViewModel.ViewModel()

    @Test
    fun verifyFormValidation() {
        val result: Observer<Boolean> = mock()
        viewModel.outputs.formIsValid().observeForever(result)

        viewModel.inputs.name("kyle.lee")
        verifyZeroInteractions(result)

        viewModel.inputs.email("kyle.lee@gmail")
        verifyZeroInteractions(result)

        viewModel.inputs.password("1234")
        verifyZeroInteractions(result)

        viewModel.inputs.passwordConfirm("1234")
        verify(result).onChanged(false)

        viewModel.inputs.email("kyle.lee@gmail.com")
        verify(result).onChanged(true)
    }
}