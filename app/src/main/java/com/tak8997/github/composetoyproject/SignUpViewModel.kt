package com.tak8997.github.composetoyproject

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject


internal interface SignUpViewModel {

    interface Inputs {
        fun email(email: String)
        fun name(name: String)
        fun password(pwd: String)
        fun passwordConfirm(pwdConfirm: String)
        fun signUpClick()
    }

    interface Outputs {
        fun errorString(): LiveData<String>
        fun formIsValid(): LiveData<Boolean>
        fun formSubmitting(): Observable<Boolean>
        fun signupSuccess(): Observable<Unit>
    }

    class ViewModel(
        private val apiClient: ApiClient = MockApiClient(),
        private val currentUserType: CurrentUserType = CurrentUser()
    ) {
        private val name = PublishSubject.create<String>()
        private val email = PublishSubject.create<String>()
        private val password = PublishSubject.create<String>()
        private val passwordConfirm = PublishSubject.create<String>()
        private val signupClick = PublishSubject.create<Unit>()

        val inputs = object : Inputs {
            override fun name(name: String) {
                this@ViewModel.name.onNext(name)
            }

            override fun email(email: String) {
                this@ViewModel.email.onNext(email)
            }

            override fun password(pwd: String) {
                this@ViewModel.password.onNext(pwd)
            }

            override fun passwordConfirm(pwdConfirm: String) {
                this@ViewModel.passwordConfirm.onNext(pwdConfirm)
            }

            override fun signUpClick() {
                this@ViewModel.signupClick.onNext(Unit)
            }
        }

        private lateinit var errorString: LiveData<String>
        private val signupSuccess = PublishSubject.create<Unit>()
        private val formSubmitting = BehaviorSubject.create<Boolean>()
        private val formIsValid = MutableLiveData<Boolean>()

        val outputs = object : Outputs {
            override fun errorString(): LiveData<String> {
                return errorString
            }

            override fun formIsValid(): LiveData<Boolean> {
                return formIsValid
            }

            override fun formSubmitting(): Observable<Boolean> {
                return formSubmitting
            }

            override fun signupSuccess(): Observable<Unit> {
                return signupSuccess
            }
        }

        private val disposables = CompositeDisposable()
        private val signupError = PublishSubject.create<ErrorEnvelope>()

        init {
            val signUpData = Observables.combineLatest(
                name, email, password, passwordConfirm, ::SignUpData
            )

            signUpData
                .map { it.isValid() }
                .subscribeBy(
                    onNext = {
                        Log.d("MY_LOG","it : ${it}")
                        formIsValid.value = it },
                    onError = defaultErrorHandler()
                )
                .addTo(disposables)

            signUpData
                .takeWhen(signupClick) { _, x -> x }
                .flatMap { submit(it) }
                .subscribeBy(
                    onNext = { success(it) },
                    onError = defaultErrorHandler()
                )
                .addTo(disposables)

            signupError
                .subscribeBy { Log.d("MY_LOG", "send Event : [signup error]") }
                .addTo(disposables)

            errorString = signupError
                .takeUntil(signupSuccess)
                .map(ErrorEnvelope::errorMessage)
                .toFlowable(BackpressureStrategy.BUFFER)
                .toLiveData()

            signupSuccess
                .subscribeBy { Log.d("MY_LOG", "send Event : [signup success]") }
                .addTo(disposables)

            signupClick
                .subscribeBy { Log.d("MY_LOG", "send event : [signup clicked]") }
                .addTo(disposables)
        }

        private fun submit(data: SignUpData): Observable<UserEnvelope> {

            return apiClient.signup(data.name, data.email, data.pwd, data.pwdConfirm)
                .doOnError {
                    ErrorEnvelope.fromThrowable(it)?.let { errorEnv ->
                        signupError.onNext(errorEnv)
                    }
                }
                .onErrorResumeNext { _: Throwable -> Observable.empty() }
                .doOnSubscribe { formSubmitting.onNext(true) }
                .doAfterTerminate { formSubmitting.onNext(false) }
        }

        private fun success(userEnvelope: UserEnvelope) {
            currentUserType.login(userEnvelope)
            signupSuccess.onNext(Unit)
        }

        data class SignUpData(
            val name: String,
            val email: String,
            val pwd: String,
            val pwdConfirm: String
        ) {
            fun isValid(): Boolean {
                return name.isNotEmpty()
                        && StringUtils.isEmail(email)
                        && pwd.length >= 2
                        && pwdConfirm.length >= 2
            }
        }
    }
}