/*
 *
 * (c) 2003-2012 MuleSoft, Inc. This software is protected under international
 * copyright law. All use of this software is subject to MuleSoft's Master
 * Subscription Agreement (or other Terms of Service) separately entered
 * into between you and MuleSoft. If such an agreement is not in
 * place, you may not use the software.
 */
package org.mule.munit.common.mp;

import org.mule.api.processor.MessageProcessor;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

/**
 * <p/>
 * This is the CGLIB callback that intercepts the getObject of the {@link org.springframework.beans.factory.FactoryBean}
 * interface. Instead of creating the real object we create a Wrapper which is also a CGLIB proxy of the real Object class
 * <p/>
 *
 * @author Mulesoft Inc.
 * @since 3.4.0
 */
public class WrapperMunitMessageProcessorInterceptor extends MunitMessageProcessorInterceptor
{

    /**
     * <p>
     * The real message processor to be called
     * </p>
     */
    private MessageProcessor realMp;

    public WrapperMunitMessageProcessorInterceptor(MessageProcessor realMp)
    {
        this.realMp = realMp;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable
    {
        Class<?> declaringClass = method.getDeclaringClass();
        if (MessageProcessor.class.isAssignableFrom(declaringClass) && method.getName().equals("process"))
        {
            return process(obj, args, proxy);
        }

        return invokeSuper(obj, args, proxy, method);
    }

    private Object invokeSuper(Object obj, Object[] args, MethodProxy proxy, Method method) throws Throwable
    {
        try
        {
            if(!method.isAccessible()) {
                method.setAccessible(true);
            }
            return method.invoke(realMp, args);
        }
        catch (Throwable e)
        {
            return invokeSuper(obj, args, proxy);
        }

    }

    @Override
    protected Object invokeSuper(Object obj, Object[] args, MethodProxy proxy) throws Throwable
    {
        return proxy.invoke(realMp, args);
    }
}
