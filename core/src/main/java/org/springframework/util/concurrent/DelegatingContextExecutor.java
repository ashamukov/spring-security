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

import org.springframework.util.Assert;

import java.util.concurrent.Executor;

/**
 * An {@link Executor} which wraps each {@link Runnable} in a
 * {@link org.springframework.util.concurrent.DelegatingContextRunnable}.
 *
 * @author Rob Winch
 * @since 3.2
 */
public abstract class DelegatingContextExecutor<C> extends
		AbstractDelegatingContextSupport<C> implements Executor {
	private final Executor delegate;

	/**
	 * Creates a new {@link DelegatingContextExecutor} that uses the specified
	 * context {@link C}.
	 *
	 * @param delegateExecutor the {@link Executor} to delegate to. Cannot be null.
	 * @param context the context {@link C} to use for each
	 * {@link org.springframework.util.concurrent.DelegatingContextRunnable} or null to default to the current
	 * context {@link C}
	 */
	public DelegatingContextExecutor(Executor delegateExecutor,
			C context, ContextOps<C> contextOps) {
		super(context, contextOps);
		Assert.notNull(delegateExecutor, "delegateExecutor cannot be null");
		this.delegate = delegateExecutor;
	}

	/**
	 * Creates a new {@link DelegatingContextExecutor} that uses the current
	 * context {@link C} from the {@link ContextOps#getContext} at the time the task
	 * is submitted.
	 *
	 * @param delegate the {@link Executor} to delegate to. Cannot be null.
	 */
	public DelegatingContextExecutor(Executor delegate, ContextOps<C> contextOps) {
		this(delegate, null, contextOps);
	}

	public final void execute(Runnable task) {
		task = wrap(task);
		delegate.execute(task);
	}

	protected final Executor getDelegateExecutor() {
		return delegate;
	}
}
