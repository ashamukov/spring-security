/*
 * Copyright 2002-2018 the original author or authors.
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

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import java.util.concurrent.Callable;

/**
 * <p>
 * Wraps a delegate {@link Callable} with logic for setting up a
 * context {@link C} before invoking the delegate {@link Callable} and
 * then removing the context {@link C} after the delegate has completed.
 * </p>
 * <p>
 * If there is a context {@link C} that already exists, it will be
 * restored after the {@link #call()} method is invoked.
 * </p>
 *
 * @author Rob Winch
 * @since 3.2
 */
public class DelegatingContextCallable<C, V> implements Callable<V> {

	private final Callable<V> delegate;


	/**
	 * The context {@link C} that the delegate {@link Callable} will be
	 * ran as.
	 */
	private final C delegateContext;

	/**
	 * The context {@link C} that was on the {@link ContextOps#getContext()}
	 * prior to being set to the delegateContext.
	 */
	private C originalContext;

	private ContextOps<C> contextOps;

	/**
	 * Creates a new {@link org.springframework.util.concurrent.DelegatingContextCallable} with a specific
	 * context {@link C}.
	 * @param delegate the delegate {@link org.springframework.util.concurrent.DelegatingContextCallable} to run with
	 * the specified context {@link C}. Cannot be null.
	 * @param context the context {@link C} to establish for the delegate
	 * {@link Callable}. Cannot be null.
	 */
	public DelegatingContextCallable(Callable<V> delegate,
			C context, ContextOps<C> contextOps) {
		Assert.notNull(delegate, "delegate cannot be null");
		Assert.notNull(context, "context cannot be null");
		this.delegate = delegate;
		this.delegateContext = context;
		this.contextOps = contextOps;
	}

	/**
	 * Creates a new {@link org.springframework.util.concurrent.DelegatingContextCallable} with the
	 * context {@link C} from the {@link ContextOps#getContext()}.
	 * @param delegate the delegate {@link Callable} to run under the current
	 * context {@link C}. Cannot be null.
	 */
	public DelegatingContextCallable(Callable<V> delegate, ContextOps<C> contextOps) {
		Assert.notNull(delegate, "delegate cannot be null");
		this.delegate = delegate;
		this.delegateContext = contextOps.getContext();
		this.contextOps = contextOps;
	}

	@Override
	public V call() throws Exception {
		this.originalContext = contextOps.getContext();

		try {
			contextOps.setContext(delegateContext);
			return delegate.call();
		}
		finally {
			C emptyContext = contextOps.createEmptyContext();
			if (emptyContext.equals(originalContext)) {
				contextOps.clearContext();
			} else {
				contextOps.setContext(originalContext);
			}
			this.originalContext = null;
		}
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

	/**
	 * Creates a {@link org.springframework.util.concurrent.DelegatingContextCallable} and with the given
	 * {@link Callable} and context {@link C}, but if the context is null
	 * will defaults to the current context {@link C} on the
	 *  {@link ContextOps#getContext}
	 *
	 * @param delegate the delegate {@link org.springframework.util.concurrent.DelegatingContextCallable} to run with
	 * the specified context {@link C}. Cannot be null.
	 * @param context the context {@link C} to establish for the delegate
	 * {@link Callable}. If null, defaults to {@link SecurityContextHolder#getContext()}
	 * @return
	 */
	public static <C, V> Callable<V> create(Callable<V> delegate, C context, ContextOps<C> contextOps) {
		return context == null ? new DelegatingContextCallable<C, V>(delegate, contextOps)
				: new DelegatingContextCallable<C, V>(delegate, context, contextOps);
	}
}
