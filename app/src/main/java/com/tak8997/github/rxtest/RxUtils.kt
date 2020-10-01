package com.tak8997.github.rxtest

import android.util.Log
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.Subject


fun <T1, T2, R> Observable<T1>.takeWhen(
    observable: Observable<T2>,
    biFunction: (T2, T1) -> R
): Observable<R> = compose<R> {
    observable.withLatestFrom(it, BiFunction { t1, t2 -> biFunction.invoke(t1, t2) })
}

fun <T> Observable<T>.handleToError(action: Subject<Throwable>? = null): Observable<T> =
    doOnError { action?.onNext(it) }

fun <T> Observable<T>.neverError(): Observable<T> =
    onErrorResumeNext { _: Throwable -> Observable.empty() }

fun <T> Observable<T>.neverError(action: Subject<Throwable>? = null): Observable<T> =
    handleToError(action).neverError()

fun defaultErrorHandler(): (Throwable) -> Unit = { e -> Log.d("MY_LOG", "error message : ${e.message}") }