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
package org.springframework.util.concurrent;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.concurrent.Callable;

/**
 * An internal support class that wraps {@link Callable} with
 * {@link org.springframework.util.concurrent.DelegatingContextCallable} and {@link Runnable} with
 * {@link org.springframework.util.concurrent.DelegatingContextRunnable}
 *
 * @author Rob Winch
 * @since 3.2
 */
public abstract class AbstractDelegatingContextSupport<C> {

	@Nullable
	protected final C context;

	@NonNull
	private final ContextOps<C> contextOps;

	/**
	 * Creates a new {@link AbstractDelegatingContextSupport} that uses the
	 * specified context {@link C}.
	 *
	 * @param context the context {@link C} to use for each
	 * {@link org.springframework.util.concurrent.DelegatingContextRunnable} and each
	 * {@link org.springframework.util.concurrent.DelegatingContextCallable} or null to default to the current
	 * context {@link C}.
	 */
	public AbstractDelegatingContextSupport(C context, ContextOps<C> contextOps) {
		this.context = context;
		this.contextOps = contextOps;
	}

	public final Runnable wrap(Runnable delegate) {
		return contextOps.wrap(delegate, context);
	}

	public final <T> Callable<T> wrap(Callable<T> delegate) {
		return contextOps.wrap(delegate, context);
	}
}
