package br.com.arndroid.etdiet.util.sk.m217.tests.utils;
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 2012, biegleux version check added in tearDown()
 */

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.IsolatedContext;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockContext;

import java.io.File;

/**
 * Replacement for ProviderTestCase2 that keeps calls to ContentResolver.notifyChanged
 * internal to observers registered with ProviderTestCase3.registerContentObserver
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class ProviderTestCase3<T extends ContentProvider> extends AndroidTestCase {

	public static final String FILENAME_PREFIX = "test.";

	Class<T> mProviderClass;
	String[] mProviderAuthority;

	private IsolatedContext mProviderContext;
	private MockContentResolver2 mResolver;

	private class MockContext2 extends MockContext {

		@Override
		public Resources getResources() {
			return getContext().getResources();
		}

		@Override
		public File getDir(String name, int mode) {
			// name the directory so the directory will be separated from
			// one created through the regular Context
			return getContext().getDir("mockcontext2_" + name, mode);
		}

		@Override
		public String getPackageName() {
			return getContext().getPackageName();
		}

		@Override
		public SharedPreferences getSharedPreferences(String name, int mode) {
			return getContext().getSharedPreferences("mockcontext2_" + name, mode);
		}

		@Override
		public Context getApplicationContext() {
			return this;
		}

		@Override
		public Object getSystemService(String name) {
			return null;
		}
	}

	/**
	 * Constructor.
	 *
	 * @param providerClass The class name of the provider under test
	 * @param providerAuthorities The provider's authority string
	 */
	public ProviderTestCase3(Class<T> providerClass, String... providerAuthorities) {
		mProviderClass = providerClass;
		mProviderAuthority = providerAuthorities;
	}

	private T mProvider;

	/**
	 * Returns the content provider created by this class in the {@link #setUp()} method.
	 * @return T An instance of the provider class given as a parameter to the test case class.
	 */
	public T getProvider() {
		return mProvider;
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mResolver = new MockContentResolver2();
		RenamingDelegatingContext targetContextWrapper = new
				RenamingDelegatingContext(
				new MockContext2(), // The context that most methods are
									// delegated to
				getContext(),		// The context that file methods are delegated to
				FILENAME_PREFIX);
		// The default IsolatedContext has a mock AccountManager that doesn't
		// work for us, so override getSystemService to always return null
		mProviderContext = new IsolatedContext(mResolver, targetContextWrapper) {
			@Override
			public Object getSystemService(String name) {
				return null;
			}
		};

		mProvider = mProviderClass.newInstance();
		mProvider.attachInfo(mProviderContext, null);
		assertNotNull(mProvider);
		for (String auth : mProviderAuthority) {
			mResolver.addProvider(auth, getProvider());
		}
	}

	/**
	 * Tears down the environment for the test fixture.
	 * <p>
	 * Calls {@link android.content.ContentProvider#shutdown()} on the
	 * {@link android.content.ContentProvider} represented by mProvider.
	 */
	@TargetApi(11)
	@Override
	protected void tearDown() throws Exception {
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			mProvider.shutdown();
		}
		super.tearDown();
	}

	/**
	 * Gets the {@link MockContentResolver2} created by this class during initialization. You
	 * must use the methods of this resolver to access the provider under test.
	 *
	 * @return A {@link MockContentResolver2} instance.
	 */
    public MockContentResolver2 getMockContentResolver() {
		return mResolver;
	}

	/**
	 * Gets the {@link IsolatedContext} created by this class during initialization.
	 * @return The {@link IsolatedContext} instance
	 */
	public IsolatedContext getMockContext() {
		return mProviderContext;
	}

	public void registerContentObserver(Uri uri, boolean notifyForDescendents,
			ContentObserver observer) {
		mResolver.safeRegisterContentObserver(uri, notifyForDescendents, observer);
	}

    public void unregisterContentObserver(ContentObserver observer) {
		mResolver.safeUnregisterContentObserver(observer);
	}
}
