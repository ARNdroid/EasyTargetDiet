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
 */



import java.util.ArrayList;

import android.database.ContentObserver;
import android.net.Uri;

public final class MockObserverNode {
    private class MockObserverEntry {
        public final ContentObserver observer;
        public final boolean notifyForDescendents;

        public MockObserverEntry(ContentObserver o, boolean n) {
            observer = o;
            notifyForDescendents = n;
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public static final int INSERT_TYPE = 0;
    @SuppressWarnings("UnusedDeclaration")
    public static final int UPDATE_TYPE = 1;
    @SuppressWarnings("UnusedDeclaration")
    public static final int DELETE_TYPE = 2;

    final private String mName;
    final private ArrayList<MockObserverNode> mChildren = new ArrayList<MockObserverNode>();
    final private ArrayList<MockObserverEntry> mObservers = new ArrayList<MockObserverEntry>();

    public MockObserverNode(String name) {
        mName = name;
    }

    private String getUriSegment(Uri uri, int index) {
        if (uri != null) {
            if (index == 0) {
                return uri.getAuthority();
            } else {
                return uri.getPathSegments().get(index - 1);
            }
        } else {
            return null;
        }
    }

    private int countUriSegments(Uri uri) {
        if (uri == null) {
            return 0;
        }
        return uri.getPathSegments().size() + 1;
    }

    public void addObserver(Uri uri, ContentObserver observer,
            boolean notifyForDescendents) {
        addObserver(uri, 0, observer, notifyForDescendents);
    }

    private void addObserver(Uri uri, int index, ContentObserver observer,
            boolean notifyForDescendents) {
        // If this is the leaf node add the observer
        if (index == countUriSegments(uri)) {
            mObservers.add(new MockObserverEntry(observer, notifyForDescendents));
            return;
        }

        // Look to see if the proper child already exists
        String segment = getUriSegment(uri, index);
        if (segment == null) {
            throw new IllegalArgumentException("Invalid Uri (" + uri + ") used for observer");
        }
        @SuppressWarnings("UnusedDeclaration")
        int N = mChildren.size();
        for (MockObserverNode node : mChildren) {
            if (node.mName.equals(segment)) {
                node.addObserver(uri, index + 1, observer, notifyForDescendents);
                return;
            }
        }

        // No child found, create one
        MockObserverNode node = new MockObserverNode(segment);
        mChildren.add(node);
        node.addObserver(uri, index + 1, observer, notifyForDescendents);
    }

    public boolean removeObserver(ContentObserver observer) {
        int size = mChildren.size();
        for (int i = 0; i < size; i++) {
            boolean empty = mChildren.get(i).removeObserver(observer);
            if (empty) {
                mChildren.remove(i);
                i--;
                size--;
            }
        }

        size = mObservers.size();
        for (int i = 0; i < size; i++) {
            MockObserverEntry entry = mObservers.get(i);
            if (entry.observer == observer) {
                mObservers.remove(i);
                break;
            }
        }

        return (mChildren.size() == 0 && mObservers.size() == 0);
    }

    private void notifyMyObservers(boolean leaf, ContentObserver observer,
            boolean selfNotify) {
        @SuppressWarnings("UnusedDeclaration")
        int N = mObservers.size();
        for (MockObserverEntry entry : mObservers) {
            // Don't notify the observer if it sent the notification and isn't interesed
            // in self notifications
            if (entry.observer == observer && !selfNotify) {
                continue;
            }

            // Make sure the observer is interested in the notification
            if (leaf || (!leaf && entry.notifyForDescendents)) {
                entry.observer.onChange(selfNotify);
            }
        }
    }

    public void notifyMyObservers(Uri uri, int index, ContentObserver observer,
            boolean selfNotify) {
        String segment = null;
        int segmentCount = countUriSegments(uri);
        if (index >= segmentCount) {
            // This is the leaf node, notify all observers
            notifyMyObservers(true, observer, selfNotify);
        } else if (index < segmentCount){
            segment = getUriSegment(uri, index);
            // Notify any observers at this level who are interested in descendents
            notifyMyObservers(false, observer, selfNotify);
        }

        @SuppressWarnings("UnusedDeclaration")
        int N = mChildren.size();
        for (MockObserverNode node : mChildren) {
            if (segment == null || node.mName.equals(segment)) {
                // We found the child,
                node.notifyMyObservers(uri, index + 1, observer, selfNotify);
                if (segment != null) {
                    break;
                }
            }
        }
    }
}
