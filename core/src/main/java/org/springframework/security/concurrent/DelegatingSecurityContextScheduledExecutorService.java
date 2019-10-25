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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.concurrent.DelegatingContextScheduledExecutorService;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;

/**
 * An {@link ScheduledExecutorService} which wraps each {@link Runnable} in a
 * {@link org.springframework.util.concurrent.DelegatingContextRunnable} and each {@link Callable} in a
 * {@link org.springframework.util.concurrent.DelegatingContextCallable}.
 *
 * @author Rob Winch
 * @since 3.2
 */
public final class DelegatingSecurityContextScheduledExecutorService extends
		DelegatingContextScheduledExecutorService {
	/**
	 * Creates a new {@link DelegatingSecurityContextScheduledExecutorService} that uses
	 * the specified {@link SecurityContext}.
	 *
	 * @param delegateScheduledExecutorService the {@link ScheduledExecutorService} to
	 * delegate to. Cannot be null.
	 * @param securityContext the {@link SecurityContext} to use for each
	 * {@link org.springframework.util.concurrent.DelegatingContextRunnable} and each
	 * {@link org.springframework.util.concurrent.DelegatingContextCallable}.
	 */
	public DelegatingSecurityContextScheduledExecutorService(
			ScheduledExecutorService delegateScheduledExecutorService,
			SecurityContext securityContext) {
		super(delegateScheduledExecutorService, securityContext, SecurityContextOps.INSTANCE);
	}

	/**
	 * Creates a new {@link DelegatingSecurityContextScheduledExecutorService} that uses
	 * the current {@link SecurityContext} from the {@link SecurityContextHolder}.
	 *
	 * @param delegate the {@link ScheduledExecutorService} to delegate to. Cannot be
	 * null.
	 */
	public DelegatingSecurityContextScheduledExecutorService(
			ScheduledExecutorService delegate) {
		super(delegate, SecurityContextOps.INSTANCE);
	}
}
