/*
 * Copyright 2002-2019 the original author or authors.
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
import org.springframework.util.concurrent.ContextOps;

import java.util.concurrent.Callable;

public class SecurityContextOps implements ContextOps<SecurityContext> {
	public static final SecurityContextOps INSTANCE = new SecurityContextOps();

	public SecurityContext getContext() {
		return SecurityContextHolder.getContext();
	}

	public void setContext(SecurityContext context) {
		SecurityContextHolder.setContext(context);
	}

	public SecurityContext createEmptyContext() {
		return SecurityContextHolder.createEmptyContext();
	}

	public void clearContext() {
		SecurityContextHolder.clearContext();
	}

	@Override
	public Runnable wrap(Runnable delegate, SecurityContext context) {
		return DelegatingSecurityContextRunnable.create(delegate, context, this);
	}

	@Override
	public <T> Callable<T> wrap(Callable<T> delegate, SecurityContext context) {
		return DelegatingSecurityContextCallable.create(delegate, context, this);
	}
}
