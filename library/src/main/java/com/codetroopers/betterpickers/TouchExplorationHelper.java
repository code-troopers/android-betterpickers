/*
 * Copyright (C) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.codetroopers.betterpickers;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import java.util.LinkedList;
import java.util.List;

public abstract class TouchExplorationHelper<T> extends AccessibilityNodeProviderCompat
        /* Removed for backwards compatibility to GB implements View.OnHoverListener*/ {

    /**
     * Virtual node identifier value for invalid nodes.
     */
    public static final int INVALID_ID = Integer.MIN_VALUE;

    private final Rect mTempScreenRect = new Rect();
    private final Rect mTempParentRect = new Rect();
    private final Rect mTempVisibleRect = new Rect();
    private final int[] mTempGlobalRect = new int[2];

    private final AccessibilityManager mManager;

    private View mParentView;
    private int mFocusedItemId = INVALID_ID;
    private T mCurrentItem = null;

    /**
     * Constructs a new touch exploration helper.
     *
     * @param context The parent context.
     */
    public TouchExplorationHelper(Context context, View parentView) {
        mManager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        mParentView = parentView;
    }

    /**
     * @return The current accessibility focused item, or {@code null} if no item is focused.
     */
    public T getFocusedItem() {
        return getItemForId(mFocusedItemId);
    }

    /**
     * Clears the current accessibility focused item.
     */
    public void clearFocusedItem() {
        final int itemId = mFocusedItemId;
        if (itemId == INVALID_ID) {
            return;
        }

        performAction(itemId, AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS, null);
    }

    /**
     * Requests accessibility focus be placed on the specified item.
     *
     * @param item The item to place focus on.
     */
    public void setFocusedItem(T item) {
        final int itemId = getIdForItem(item);
        if (itemId == INVALID_ID) {
            return;
        }

        performAction(itemId, AccessibilityNodeInfoCompat.ACTION_ACCESSIBILITY_FOCUS, null);
    }

    /**
     * Invalidates cached information about the parent view. <p> You <b>must</b> call this method after adding or
     * removing items from the parent view. </p>
     */
    public void invalidateParent() {
        mParentView.sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED);
    }

    /**
     * Invalidates cached information for a particular item. <p> You <b>must</b> call this method when any of the
     * properties set in {@link #populateNodeForItem(Object, AccessibilityNodeInfoCompat)} have changed. </p>
     */
    public void invalidateItem(T item) {
        sendEventForItem(item, AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED);
    }

    /**
     * Populates an event of the specified type with information about an item and attempts to send it up through the
     * view hierarchy.
     *
     * @param item The item for which to send an event.
     * @param eventType The type of event to send.
     * @return {@code true} if the event was sent successfully.
     */
    public boolean sendEventForItem(T item, int eventType) {
        if (!mManager.isEnabled()) {
            return false;
        }

        final AccessibilityEvent event = getEventForItem(item, eventType);
        final ViewGroup group = (ViewGroup) mParentView.getParent();

        return group.requestSendAccessibilityEvent(mParentView, event);
    }

    @Override
    public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int virtualViewId) {
        if (virtualViewId == View.NO_ID) {
            return getNodeForParent();
        }

        final T item = getItemForId(virtualViewId);
        if (item == null) {
            return null;
        }

        final AccessibilityNodeInfoCompat node = AccessibilityNodeInfoCompat.obtain();
        populateNodeForItemInternal(item, node);
        return node;
    }

    @Override
    public boolean performAction(int virtualViewId, int action, Bundle arguments) {
        if (virtualViewId == View.NO_ID) {
            return ViewCompat.performAccessibilityAction(mParentView, action, arguments);
        }

        final T item = getItemForId(virtualViewId);
        if (item == null) {
            return false;
        }

        boolean handled = false;

        switch (action) {
            case AccessibilityNodeInfoCompat.ACTION_ACCESSIBILITY_FOCUS:
                if (mFocusedItemId != virtualViewId) {
                    mFocusedItemId = virtualViewId;
                    sendEventForItem(item, AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED);
                    handled = true;
                }
                break;
            case AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS:
                if (mFocusedItemId == virtualViewId) {
                    mFocusedItemId = INVALID_ID;
                    sendEventForItem(item, AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    handled = true;
                }
                break;
        }

        handled |= performActionForItem(item, action, arguments);

        return handled;
    }

    /* Removed for backwards compatibility to GB
    @Override
    public boolean onHover(View view, MotionEvent event) {
        if (!AccessibilityManagerCompat.isTouchExplorationEnabled(mManager)) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_HOVER_ENTER:
            case MotionEvent.ACTION_HOVER_MOVE:
                final T item = getItemAt(event.getX(), event.getY());
                setCurrentItem(item);
                return true;
            case MotionEvent.ACTION_HOVER_EXIT:
                setCurrentItem(null);
                return true;
        }

        return false;
    }*/

    private void setCurrentItem(T item) {
        if (mCurrentItem == item) {
            return;
        }

        if (mCurrentItem != null) {
            sendEventForItem(mCurrentItem, AccessibilityEvent.TYPE_VIEW_HOVER_EXIT);
        }

        mCurrentItem = item;

        if (mCurrentItem != null) {
            sendEventForItem(mCurrentItem, AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
        }
    }

    private AccessibilityEvent getEventForItem(T item, int eventType) {
        final AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
        final AccessibilityRecordCompat record = new AccessibilityRecordCompat(event);
        final int virtualDescendantId = getIdForItem(item);

        // Ensure the client has good defaults.
        event.setEnabled(true);

        // Allow the client to populate the event.
        populateEventForItem(item, event);

        if (event.getText().isEmpty() && TextUtils.isEmpty(event.getContentDescription())) {
            throw new RuntimeException(
                    "You must add text or a content description in populateEventForItem()");
        }

        // Don't allow the client to override these properties.
        event.setClassName(item.getClass().getName());
        event.setPackageName(mParentView.getContext().getPackageName());
        record.setSource(mParentView, virtualDescendantId);

        return event;
    }

    private AccessibilityNodeInfoCompat getNodeForParent() {
        final AccessibilityNodeInfoCompat info = AccessibilityNodeInfoCompat.obtain(mParentView);
        ViewCompat.onInitializeAccessibilityNodeInfo(mParentView, info);

        final LinkedList<T> items = new LinkedList<T>();
        getVisibleItems(items);

        for (T item : items) {
            final int virtualDescendantId = getIdForItem(item);
            info.addChild(mParentView, virtualDescendantId);
        }

        return info;
    }

    private AccessibilityNodeInfoCompat populateNodeForItemInternal(
            T item, AccessibilityNodeInfoCompat node) {
        final int virtualDescendantId = getIdForItem(item);

        // Ensure the client has good defaults.
        node.setEnabled(true);

        // Allow the client to populate the node.
        populateNodeForItem(item, node);

        if (TextUtils.isEmpty(node.getText()) && TextUtils.isEmpty(node.getContentDescription())) {
            throw new RuntimeException(
                    "You must add text or a content description in populateNodeForItem()");
        }

        // Don't allow the client to override these properties.
        node.setPackageName(mParentView.getContext().getPackageName());
        node.setClassName(item.getClass().getName());
        node.setParent(mParentView);
        node.setSource(mParentView, virtualDescendantId);

        if (mFocusedItemId == virtualDescendantId) {
            node.addAction(AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
        } else {
            node.addAction(AccessibilityNodeInfoCompat.ACTION_ACCESSIBILITY_FOCUS);
        }

        node.getBoundsInParent(mTempParentRect);
        if (mTempParentRect.isEmpty()) {
            throw new RuntimeException("You must set parent bounds in populateNodeForItem()");
        }

        // Set the visibility based on the parent bound.
        if (intersectVisibleToUser(mTempParentRect)) {
            node.setVisibleToUser(true);
            node.setBoundsInParent(mTempParentRect);
        }

        // Calculate screen-relative bound.
        mParentView.getLocationOnScreen(mTempGlobalRect);
        final int offsetX = mTempGlobalRect[0];
        final int offsetY = mTempGlobalRect[1];
        mTempScreenRect.set(mTempParentRect);
        mTempScreenRect.offset(offsetX, offsetY);
        node.setBoundsInScreen(mTempScreenRect);

        return node;
    }

    /**
     * Computes whether the specified {@link android.graphics.Rect} intersects with the visible portion of its parent
     * {@link android.view.View}. Modifies {@code localRect} to contain only the visible portion.
     *
     * @param localRect A rectangle in local (parent) coordinates.
     * @return Whether the specified {@link android.graphics.Rect} is visible on the screen.
     */
    private boolean intersectVisibleToUser(Rect localRect) {
        // Missing or empty bounds mean this view is not visible.
        if ((localRect == null) || localRect.isEmpty()) {
            return false;
        }

        // Attached to invisible window means this view is not visible.
        if (mParentView.getWindowVisibility() != View.VISIBLE) {
            return false;
        }

        // An invisible predecessor or one with alpha zero means
        // that this view is not visible to the user.
        Object current = this;
        while (current instanceof View) {
            final View view = (View) current;
            // We have attach info so this view is attached and there is no
            // need to check whether we reach to ViewRootImpl on the way up.
            if ((view.getAlpha() <= 0) || (view.getVisibility() != View.VISIBLE)) {
                return false;
            }
            current = view.getParent();
        }

        // If no portion of the parent is visible, this view is not visible.
        if (!mParentView.getLocalVisibleRect(mTempVisibleRect)) {
            return false;
        }

        // Check if the view intersects the visible portion of the parent.
        return localRect.intersect(mTempVisibleRect);
    }

    public AccessibilityDelegateCompat getAccessibilityDelegate() {
        return mDelegate;
    }

    private final AccessibilityDelegateCompat mDelegate = new AccessibilityDelegateCompat() {
        @Override
        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(view, event);
            event.setClassName(view.getClass().getName());
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(view, info);
            info.setClassName(view.getClass().getName());
        }

        @Override
        public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View host) {
            return TouchExplorationHelper.this;
        }
    };

    /**
     * Performs an accessibility action on the specified item. See {@link AccessibilityNodeInfoCompat#performAction(int,
     * android.os.Bundle)}. <p> The helper class automatically handles focus management resulting from {@link
     * AccessibilityNodeInfoCompat#ACTION_ACCESSIBILITY_FOCUS} and {@link AccessibilityNodeInfoCompat#ACTION_CLEAR_ACCESSIBILITY_FOCUS},
     * so typically a developer only needs to handle actions added manually in the {{@link #populateNodeForItem(Object,
     * AccessibilityNodeInfoCompat)} method. </p>
     *
     * @param item The item on which to perform the action.
     * @param action The accessibility action to perform.
     * @param arguments Arguments for the action, or optionally {@code null}.
     * @return {@code true} if the action was performed successfully.
     */
    protected abstract boolean performActionForItem(T item, int action, Bundle arguments);

    /**
     * Populates an event with information about the specified item. <p> At a minimum, a developer must populate the
     * event text by doing one of the following: <ul> <li>appending text to {@link android.view.accessibility.AccessibilityEvent#getText()}</li>
     * <li>populating a description with {@link android.view.accessibility.AccessibilityEvent#setContentDescription(CharSequence)}</li>
     * </ul> </p>
     *
     * @param item The item for which to populate the event.
     * @param event The event to populate.
     */
    protected abstract void populateEventForItem(T item, AccessibilityEvent event);

    /**
     * Populates a node with information about the specified item. <p> At a minimum, a developer must: <ul> <li>populate
     * the event text using {@link AccessibilityNodeInfoCompat#setText(CharSequence)} or {@link
     * AccessibilityNodeInfoCompat#setContentDescription(CharSequence)} </li> <li>set the item's parent-relative bounds
     * using {@link AccessibilityNodeInfoCompat#setBoundsInParent(android.graphics.Rect)} </ul>
     *
     * @param item The item for which to populate the node.
     * @param node The node to populate.
     */
    protected abstract void populateNodeForItem(T item, AccessibilityNodeInfoCompat node);

    /**
     * Populates a list with the parent view's visible items. <p> The result of this method is cached until the
     * developer calls {@link #invalidateParent()}. </p>
     *
     * @param items The list to populate with visible items.
     */
    protected abstract void getVisibleItems(List<T> items);

    /**
     * Returns the item under the specified parent-relative coordinates.
     *
     * @param x The parent-relative x coordinate.
     * @param y The parent-relative y coordinate.
     * @return The item under coordinates (x,y).
     */
    protected abstract T getItemAt(float x, float y);

    /**
     * Returns the unique identifier for an item. If the specified item does not exist, returns {@link #INVALID_ID}. <p>
     * This result of this method must be consistent with {@link #getItemForId(int)}. </p>
     *
     * @param item The item whose identifier to return.
     * @return A unique identifier, or {@link #INVALID_ID}.
     */
    protected abstract int getIdForItem(T item);

    /**
     * Returns the item for a unique identifier. If the specified item does not exist, returns {@code null}.
     *
     * @param id The identifier for the item to return.
     * @return An item, or {@code null}.
     */
    protected abstract T getItemForId(int id);
}
