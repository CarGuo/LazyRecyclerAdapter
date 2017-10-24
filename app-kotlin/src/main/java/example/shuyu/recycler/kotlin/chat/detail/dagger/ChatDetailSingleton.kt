package example.shuyu.recycler.kotlin.chat.detail.dagger


import javax.inject.Scope

/**
 * Created by guoshuyu on 2017/9/5.
 * 限定作用域后，dagger才会DoubleCheck等去缓存，保证作用域内的单例
 * 这样才可以实现一个注入依赖于被注入得了另外一个实例
 *
 * Singleton 一般约定在全局单例下
 */
@MustBeDocumented
@Scope
@Retention(AnnotationRetention.RUNTIME)
public annotation class ChatDetailSingleton
