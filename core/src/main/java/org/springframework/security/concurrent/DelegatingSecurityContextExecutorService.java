/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.concurrent;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.util.concurrent.DelegatingContextExecutorService;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * An {@link ExecutorService} which wraps each {@link Runnable} in a
 * {@link org.springframework.util.concurrent.DelegatingContextRunnable} and each {@link Callable} in a
 * {@link org.springframework.util.concurrent.DelegatingContextCallable}.
 *
 * @author Rob Winch
 * @since 3.2
 */
public class DelegatingSecurityContextExecutorService extends
		DelegatingContextExecutorService<SecurityContext> {

	public DelegatingSecurityContextExecutorService(ExecutorService delegateExecutorService, SecurityContext context) {
		super(delegateExecutorService, context, SecurityContextOps.INSTANCE);
	}

	public DelegatingSecurityContextExecutorService(ExecutorService delegate) {
		super(delegate, SecurityContextOps.INSTANCE);
	}
}
