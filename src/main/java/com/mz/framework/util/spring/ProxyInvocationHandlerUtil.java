package com.mz.framework.util.spring;

import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;

import java.lang.reflect.Method;

//jdk动态代理
public class ProxyInvocationHandlerUtil implements InvocationHandler {

    //被代理的接口
    private Object obj;

    public void setObj(Object obj) {
        this.obj = obj;
    }

    //生成得到代理类
    public Object getProxy() {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), obj.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //执行前后可动态添加其他方法，如：执行日志；
        //利用反射执行方法
        Object result = method.invoke(obj, args);
        return result;
    }
}
