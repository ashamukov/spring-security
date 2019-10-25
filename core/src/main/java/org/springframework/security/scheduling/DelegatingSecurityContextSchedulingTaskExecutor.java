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
package org.springframework.security.scheduling;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import java.util.concurrent.Callable;

/**
 * An {@link SchedulingTaskExecutor} which wraps each {@link Runnable} in a
 * {@link org.springframework.util.concurrent.DelegatingContextRunnable} and each {@link Callable} in a
 * {@link org.springframework.util.concurrent.DelegatingContextCallable}.
 *
 * @author Rob Winch
 * @since 3.2
 */
public class DelegatingSecurityContextSchedulingTaskExecutor extends
		DelegatingSecurityContextAsyncTaskExecutor implements SchedulingTaskExecutor {

	/**
	 * Creates a new {@link DelegatingSecurityContextSchedulingTaskExecutor} that uses the
	 * specified {@link SecurityContext}.
	 *
	 * @param delegateSchedulingTaskExecutor the {@link SchedulingTaskExecutor} to
	 * delegate to. Cannot be null.
	 * @param securityContext the {@link SecurityContext} to use for each
	 * {@link org.springframework.util.concurrent.DelegatingContextRunnable} and
	 * {@link org.springframework.util.concurrent.DelegatingContextCallable}
	 */
	public DelegatingSecurityContextSchedulingTaskExecutor(
			SchedulingTaskExecutor delegateSchedulingTaskExecutor,
			SecurityContext securityContext) {
		super(delegateSchedulingTaskExecutor, securityContext);
	}

	/**
	 * Creates a new {@link DelegatingSecurityContextSchedulingTaskExecutor} that uses the
	 * current {@link SecurityContext}.
	 *
	 * @param delegateAsyncTaskExecutor the {@link AsyncTaskExecutor} to delegate to.
	 * Cannot be null.
	 */
	public DelegatingSecurityContextSchedulingTaskExecutor(
			SchedulingTaskExecutor delegateAsyncTaskExecutor) {
		this(delegateAsyncTaskExecutor, null);
	}

	public boolean prefersShortLivedTasks() {
		return getDelegate().prefersShortLivedTasks();
	}

	private SchedulingTaskExecutor getDelegate() {
		return (SchedulingTaskExecutor) getDelegateExecutor();
	}
}
