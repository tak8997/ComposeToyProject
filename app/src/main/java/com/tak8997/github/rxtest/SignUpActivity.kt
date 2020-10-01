package com.tak8997.github.rxtest

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_sign_up.*

internal class SignUpActivity : AppCompatActivity() {

    private val viewModel = SignUpViewModel.ViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        with(viewModel.outputs) {
            signupSuccess()
                .subscribeBy {
                    Toast.makeText(
                        this@SignUpActivity,
                        "signup success",
                        Toast.LENGTH_LONG
                    ).show()
                }

            formSubmitting()
                .subscribeBy { btnSignUp.isEnabled = !it }

            formIsValid()
                .subscribeBy { btnSignUp.isEnabled = it }

            errorString()
                .subscribeBy {
                    Toast.makeText(
                        this@SignUpActivity,
                        it,
                        Toast.LENGTH_LONG
                    ).show()
                }
        }

        setupListener()
    }

    private fun setupListener() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(name: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.inputs.name(name.toString())
            }
        })

        etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(email: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.inputs.email(email.toString())
            }
        })

        etPwd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(password: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.inputs.password(password.toString())
            }
        })

        etPwdConfirm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(passwordConfirm: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.inputs.passwordConfirm(passwordConfirm.toString())
            }
        })

        btnSignUp.setOnClickListener {
            viewModel.inputs.signUpClick()
        }
    }
}