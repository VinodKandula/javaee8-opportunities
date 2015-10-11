/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.supertribe.interceptors.interim.stack;

import org.apache.openejb.util.Join;
import org.junit.Assert;
import org.junit.Test;

import javax.interceptor.InvocationContext;
import java.util.ArrayList;
import java.util.List;

public class InterceptorStackTest {

    @Test
    public void test() throws Exception {
        final ArrayList<Interceptor> interceptors = new ArrayList<>();

        interceptors.add(new Interceptor(new DefaultInterceptorOne(), DefaultInterceptorOne.class.getMethod("businessMethodInterceptor", InvocationContext.class)));
        interceptors.add(new Interceptor(new DefaultInterceptorTwo(), DefaultInterceptorTwo.class.getMethod("businessMethodInterceptor", InvocationContext.class)));
        interceptors.add(new Interceptor(new ClassLevelInterceptorSuperClassOne(), ClassLevelInterceptorSuperClassOne.class.getMethod("businessMethodInterceptor", InvocationContext.class)));
        interceptors.add(new Interceptor(new ClassLevelInterceptorSuperClassTwo(), ClassLevelInterceptorSuperClassTwo.class.getMethod("businessMethodInterceptor", InvocationContext.class)));
        interceptors.add(new Interceptor(new ClassLevelInterceptorOne(), ClassLevelInterceptorOne.class.getMethod("businessMethodInterceptor", InvocationContext.class)));
        interceptors.add(new Interceptor(new ClassLevelInterceptorTwo(), ClassLevelInterceptorTwo.class.getMethod("businessMethodInterceptor", InvocationContext.class)));
        interceptors.add(new Interceptor(new MethodLevelInterceptorOne(), MethodLevelInterceptorOne.class.getMethod("businessMethodInterceptor", InvocationContext.class)));
        interceptors.add(new Interceptor(new MethodLevelInterceptorTwo(), MethodLevelInterceptorTwo.class.getMethod("businessMethodInterceptor", InvocationContext.class)));
        interceptors.add(new Interceptor(new FullyInterceptedBean(), FullyInterceptedBean.class.getMethod("beanClassBusinessMethodInterceptor", InvocationContext.class)));

        final InterceptorStack stack = new InterceptorStack(new FullyInterceptedBean(), FullyInterceptedBean.class.getMethod("businessMethod", int.class, String.class), interceptors);

        final List<String> invoke = (List<String>) stack.invoke(42, "Answer");

        final List<String> expected = new ArrayList<String>();
        expected.add("DefaultInterceptorOne");
        expected.add("DefaultInterceptorTwo");
        expected.add("ClassLevelInterceptorSuperClassOne");
        expected.add("ClassLevelInterceptorSuperClassTwo");
        expected.add("ClassLevelInterceptorOne");
        expected.add("ClassLevelInterceptorTwo");
        expected.add("MethodLevelInterceptorOne");
        expected.add("MethodLevelInterceptorTwo");
        expected.add("beanClassBusinessMethodInterceptor");
        expected.add("businessMethod");
        expected.add("42, Answer");

        Assert.assertEquals(Join.join("\n", invoke), Join.join("\n", expected));
    }


}