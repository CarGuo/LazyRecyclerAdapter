package com.shuyu.apprecycler.chat.detail.dagger;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by guoshuyu on 2017/9/5.
 * 限定作用域后，dagger才会DoubleCheck等去缓存，保证作用域内的单例
 * 这样才可以实现一个注入依赖于被注入得了另外一个实例
 */
@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ChatDetailSingleton {
}
