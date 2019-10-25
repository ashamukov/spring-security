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

import org.springframework.util.Assert;

/**
 * <p>
 * Wraps a delegate {@link Runnable} with logic for setting up a context {@link C}
 * before invoking the delegate {@link Runnable} and then removing the
 * context {@link C} after the delegate has completed.
 * </p>
 * <p>
 * If there is a context {@link C} that already exists, it will be
 * restored after the {@link #run()} method is invoked.
 * </p>
 *
 * @author Rob Winch
 * @since 3.2
 */
public class DelegatingContextRunnable<C> implements Runnable {

	private final Runnable delegate;

	/**
	 * The context {@link C} that the delegate {@link Runnable} will be
	 * ran as.
	 */
	private final C delegateContext;

	/**
	 * The context {@link C} that was on the {@link ContextOps#getContext}
	 * prior to being set to the delegateContext.
	 */
	private C originalContext;

	private ContextOps<C> contextOps;

	/**
	 * Creates a new {@link org.springframework.util.concurrent.DelegatingContextRunnable} with a specific
	 * context {@link C}.
	 * @param delegate the delegate {@link Runnable} to run with the specified
	 * context {@link C}. Cannot be null.
	 * @param context the context {@link C} to establish for the delegate
	 * {@link Runnable}. Cannot be null.
	 */
	public DelegatingContextRunnable(Runnable delegate,
			C context, ContextOps<C> contextOps) {
		Assert.notNull(delegate, "delegate cannot be null");
		Assert.notNull(context, "context cannot be null");
		this.delegate = delegate;
		this.delegateContext = context;
		this.contextOps = contextOps;
	}

	/**
	 * Creates a new {@link org.springframework.util.concurrent.DelegatingContextRunnable} with the
	 * context {@link C} from the {@link ContextOps#getContext}.
	 * @param delegate the delegate {@link Runnable} to run under the current
	 * context {@link C}. Cannot be null.
	 */
	public DelegatingContextRunnable(Runnable delegate, ContextOps<C> contextOps) {
		Assert.notNull(delegate, "delegate cannot be null");
		this.delegate = delegate;
		this.contextOps = contextOps;
		this.delegateContext = contextOps.getContext();
	}

	@Override
	public void run() {
		this.originalContext = contextOps.getContext();

		try {
			contextOps.setContext(delegateContext);
			delegate.run();
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
	 * Factory method for creating a {@link org.springframework.util.concurrent.DelegatingContextRunnable}.
	 *
	 * @param delegate the original {@link Runnable} that will be delegated to after
	 * establishing a context {@link C} on the  {@link ContextOps#getContext}. Cannot
	 * have null.
	 * @param context the context {@link C} to establish before invoking the
	 * delegate {@link Runnable}. If null, the current context {@link C} from the
	 * {@link ContextOps#getContext} will be used.
	 * @return
	 */
	public static <C> Runnable create(Runnable delegate, C context, ContextOps<C> contextOps) {
		Assert.notNull(delegate, "delegate cannot be  null");
		return context == null ? new DelegatingContextRunnable<C>(delegate, contextOps)
				: new DelegatingContextRunnable<C>(delegate, context, contextOps);
	}
}
