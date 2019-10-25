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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * An {@link ExecutorService} which wraps each {@link Runnable} in a
 * {@link org.springframework.util.concurrent.DelegatingContextRunnable} and each {@link Callable} in a
 * {@link org.springframework.util.concurrent.DelegatingContextCallable}.
 *
 * @author Rob Winch
 * @since 3.2
 */
public abstract class DelegatingContextExecutorService<C> extends
		DelegatingContextExecutor<C> implements ExecutorService {
	/**
	 * Creates a new {@link DelegatingContextExecutorService} that uses the
	 * specified context {@link C}.
	 *
	 * @param delegateExecutorService the {@link ExecutorService} to delegate to. Cannot
	 * be null.
	 * @param context the context {@link C} to use for each
	 * {@link org.springframework.util.concurrent.DelegatingContextRunnable} and each
	 * {@link org.springframework.util.concurrent.DelegatingContextCallable}.
	 */
	public DelegatingContextExecutorService(
			ExecutorService delegateExecutorService, C context, ContextOps<C> contextOps) {
		super(delegateExecutorService, context, contextOps);
	}

	/**
	 * Creates a new {@link DelegatingContextExecutorService} that uses the
	 * current context {@link C} from the {@link ContextOps#getContext}.
	 *
	 * @param delegate the {@link ExecutorService} to delegate to. Cannot be
	 * null.
	 */
	public DelegatingContextExecutorService(ExecutorService delegate, ContextOps<C> contextOps) {
		super(delegate, contextOps);
	}

	public final void shutdown() {
		getDelegate().shutdown();
	}

	public final List<Runnable> shutdownNow() {
		return getDelegate().shutdownNow();
	}

	public final boolean isShutdown() {
		return getDelegate().isShutdown();
	}

	public final boolean isTerminated() {
		return getDelegate().isTerminated();
	}

	public final boolean awaitTermination(long timeout, TimeUnit unit)
			throws InterruptedException {
		return getDelegate().awaitTermination(timeout, unit);
	}

	public final <T> Future<T> submit(Callable<T> task) {
		task = wrap(task);
		return getDelegate().submit(task);
	}

	public final <T> Future<T> submit(Runnable task, T result) {
		task = wrap(task);
		return getDelegate().submit(task, result);
	}

	public final Future<?> submit(Runnable task) {
		task = wrap(task);
		return getDelegate().submit(task);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final List invokeAll(Collection tasks) throws InterruptedException {
		tasks = createTasks(tasks);
		return getDelegate().invokeAll(tasks);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final List invokeAll(Collection tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		tasks = createTasks(tasks);
		return getDelegate().invokeAll(tasks, timeout, unit);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final Object invokeAny(Collection tasks) throws InterruptedException,
			ExecutionException {
		tasks = createTasks(tasks);
		return getDelegate().invokeAny(tasks);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final Object invokeAny(Collection tasks, long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		tasks = createTasks(tasks);
		return getDelegate().invokeAny(tasks, timeout, unit);
	}

	private <T> Collection<Callable<T>> createTasks(Collection<Callable<T>> tasks) {
		if (tasks == null) {
			return null;
		}
		List<Callable<T>> results = new ArrayList<>(tasks.size());
		for (Callable<T> task : tasks) {
			results.add(wrap(task));
		}
		return results;
	}

	private ExecutorService getDelegate() {
		return (ExecutorService) getDelegateExecutor();
	}
}
