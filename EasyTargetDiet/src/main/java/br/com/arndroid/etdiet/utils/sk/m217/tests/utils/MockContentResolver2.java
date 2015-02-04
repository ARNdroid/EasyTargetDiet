package br.com.arndroid.etdiet.utils.sk.m217.tests.utils;
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
 * 2012, biegleux, modified to support observers
 */

import android.database.ContentObserver;
import android.net.Uri;
import android.test.mock.MockContentResolver;

public class MockContentResolver2 extends MockContentResolver {

	private final MockObserverNode mRootNode = new MockObserverNode("");

	@Override
	public void notifyChange(Uri uri, ContentObserver observer,
			boolean syncToNetwork) {
		mRootNode.notifyMyObservers(uri, 0, observer, false);
	}

	public void safeRegisterContentObserver(Uri uri, boolean notifyForDescendants,
			ContentObserver observer) {
		mRootNode.addObserver(uri, observer, notifyForDescendants);
	}

	public void safeUnregisterContentObserver(ContentObserver observer) {
		mRootNode.removeObserver(observer);
	}

}
