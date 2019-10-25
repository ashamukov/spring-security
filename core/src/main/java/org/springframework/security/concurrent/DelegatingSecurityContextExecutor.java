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
import org.springframework.util.concurrent.DelegatingContextExecutor;

import java.util.concurrent.Executor;

/**
 * An {@link Executor} which wraps each {@link Runnable} in a
 * {@link org.springframework.util.concurrent.DelegatingContextRunnable}
 *
 * @author Rob Winch
 * @since 3.2
 */
public class DelegatingSecurityContextExecutor extends
		DelegatingContextExecutor<SecurityContext> {

	public DelegatingSecurityContextExecutor(Executor delegateExecutor, SecurityContext context) {
		super(delegateExecutor, context, SecurityContextOps.INSTANCE);
	}

	public DelegatingSecurityContextExecutor(Executor delegate) {
		super(delegate, SecurityContextOps.INSTANCE);
	}
}
