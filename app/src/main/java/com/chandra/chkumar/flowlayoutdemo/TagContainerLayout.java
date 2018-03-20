/*
 * Copyright 2015 lujun
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

package com.chandra.chkumar.flowlayoutdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static com.chandra.chkumar.flowlayoutdemo.Utils.dp2px;
import static com.chandra.chkumar.flowlayoutdemo.Utils.sp2px;


/**
 * Author: Chandra
 * Date: 2018-03-16
 */
public class TagContainerLayout extends ViewGroup {

    /**
     * Vertical interval, default 5(dp)
     */
    private int mVerticalInterval;

    /**
     * The list to store the tags color info
     */
    private List<int[]> mColorArrayList;

    /**
     * Horizontal interval, default 5(dp)
     */
    private int mHorizontalInterval;

    /**
     * The sensitive of the ViewDragHelper(default 1.0f, normal)
     */
//    private float mSensitivity = 1.0f;

    /**
     * TagView average height
     */
    private int mChildHeight;

    /**
     * The container layout gravity(default left)
     */
    private int mGravity = Gravity.LEFT;

    /**
     * The max line count of TagContainerLayout
     */
    private int mMaxLines = 0;

    /**
     * The max length for TagView(default max length 23)
     */
    private int mTagMaxLength = 23;

    /**
     * TagView Border width(default 0.5dp)
     */
    private float mTagBorderWidth = 0.5f;

    /**
     * TagView Border radius(default 15.0dp)
     */
    private float mTagBorderRadius = 15.0f;

    /**
     * TagView Text size(default 14sp)
     */
    private float mTagTextSize = 14;

    /**
     * Text direction(support:TEXT_DIRECTION_RTL & TEXT_DIRECTION_LTR, default TEXT_DIRECTION_LTR)
     */
    private int mTagTextDirection = View.TEXT_DIRECTION_LTR;

    /**
     * Horizontal padding for TagView, include left & right padding(left & right padding are equal, default 10dp)
     */
    private int mTagHorizontalPadding = 10;

    /**
     * Vertical padding for TagView, include top & bottom padding(top & bottom padding are equal, default 8dp)
     */
    private int mTagVerticalPadding = 8;

    /**
     * TagView border color(default #88F44336)
     */
    private int mTagBorderColor = Color.parseColor("#88F44336");

    /**
     * TagView background color(default #33F44336)
     */
    private int mTagBackgroundColor = Color.parseColor("#33F44336");

    /**
     * TagView text color(default #FF666666)
     */
    private int mTagTextColor = Color.parseColor("#FF666666");

    /**
     * TagView typeface
     */
    private Typeface mTagTypeface = Typeface.DEFAULT;

    /**
     * Tags
     */
    private List<String> mTags;

    /**
     * TagView drag state(default STATE_IDLE)
     */
    private int mTagViewState = ViewDragHelper.STATE_IDLE;

    /**
     * The distance between baseline and descent(default 2.75dp)
     */
    private float mTagBdDistance = 2.75f;

    /**
     * Whether to support 'letters show with RTL(eg: Android to diordnA)' style(default false)
     */
    private boolean mTagSupportLettersRTL = false;

    private Paint mPaint;

    private RectF mRectF;

    private List<View> mChildViews;

    private int[] mViewPos;

    /**
     * Default interval(dp)
     */
    private static final float DEFAULT_INTERVAL = 5;

    /**
     * Default tag min length
     */
    private static final int TAG_MIN_LENGTH = 3;

    public TagContainerLayout(Context context) {
        this(context, null);
    }

    public TagContainerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagContainerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AndroidTagView,
                defStyleAttr, 0);
        mVerticalInterval = (int) attributes.getDimension(R.styleable.AndroidTagView_vertical_interval,
                dp2px(context, DEFAULT_INTERVAL));
        mHorizontalInterval = (int) attributes.getDimension(R.styleable.AndroidTagView_horizontal_interval,
                dp2px(context, DEFAULT_INTERVAL));
        mTagBdDistance = attributes.getDimension(R.styleable.AndroidTagView_tag_bd_distance,
                dp2px(context, mTagBdDistance));
        mGravity = attributes.getInt(R.styleable.AndroidTagView_container_gravity, mGravity);
        mMaxLines = attributes.getInt(R.styleable.AndroidTagView_container_max_lines, mMaxLines);
        mTagMaxLength = attributes.getInt(R.styleable.AndroidTagView_tag_max_length, mTagMaxLength);
        mTagBorderWidth = attributes.getDimension(R.styleable.AndroidTagView_tag_border_width,
                dp2px(context, mTagBorderWidth));
        mTagBorderRadius = attributes.getDimension(
                R.styleable.AndroidTagView_tag_corner_radius, dp2px(context, mTagBorderRadius));
        mTagHorizontalPadding = (int) attributes.getDimension(
                R.styleable.AndroidTagView_tag_horizontal_padding,
                dp2px(context, mTagHorizontalPadding));
        mTagVerticalPadding = (int) attributes.getDimension(
                R.styleable.AndroidTagView_tag_vertical_padding, dp2px(context, mTagVerticalPadding));
        mTagTextSize = attributes.getDimension(R.styleable.AndroidTagView_tag_text_size,
                sp2px(context, mTagTextSize));
        mTagBorderColor = attributes.getColor(R.styleable.AndroidTagView_tag_border_color,
                mTagBorderColor);
        mTagBackgroundColor = attributes.getColor(R.styleable.AndroidTagView_tag_background_color,
                mTagBackgroundColor);
        mTagTextColor = attributes.getColor(R.styleable.AndroidTagView_tag_text_color, mTagTextColor);
        mTagTextDirection = attributes.getInt(R.styleable.AndroidTagView_tag_text_direction, mTagTextDirection);
        mTagSupportLettersRTL = attributes.getBoolean(R.styleable.AndroidTagView_tag_support_letters_rlt,
                mTagSupportLettersRTL);
        attributes.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectF = new RectF();
        mChildViews = new ArrayList<View>();
        setWillNotDraw(false);
        setTagMaxLength(mTagMaxLength);
        setTagHorizontalPadding(mTagHorizontalPadding);
        setTagVerticalPadding(mTagVerticalPadding);

        if (isInEditMode()) {
            addTag("sample tag");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        final int childCount = getChildCount();
        int lines = childCount == 0 ? 0 : getChildLines(childCount);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        if (childCount == 0) {
            setMeasuredDimension(0, 0);
        } else if (heightSpecMode == MeasureSpec.AT_MOST
                || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension(widthSpecSize, (mVerticalInterval + mChildHeight) * lines
                    - mVerticalInterval + getPaddingTop() + getPaddingBottom());
        } else {
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF.set(0, 0, w, h);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount;
        if ((childCount = getChildCount()) <= 0) {
            return;
        }
        int availableW = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int curRight = getMeasuredWidth() - getPaddingRight();
        int curTop = getPaddingTop();
        int curLeft = getPaddingLeft();
        int sPos = 0;
        mViewPos = new int[childCount * 2];

        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                int width = childView.getMeasuredWidth();
                if (mGravity == Gravity.RIGHT) {
                    if (curRight - width < getPaddingLeft()) {
                        curRight = getMeasuredWidth() - getPaddingRight();
                        curTop += mChildHeight + mVerticalInterval;
                    }
                    mViewPos[i * 2] = curRight - width;
                    mViewPos[i * 2 + 1] = curTop;
                    curRight -= width + mHorizontalInterval;
                } else if (mGravity == Gravity.CENTER) {
                    if (curLeft + width - getPaddingLeft() > availableW) {
                        int leftW = getMeasuredWidth() - mViewPos[(i - 1) * 2]
                                - getChildAt(i - 1).getMeasuredWidth() - getPaddingRight();
                        for (int j = sPos; j < i; j++) {
                            mViewPos[j * 2] = mViewPos[j * 2] + leftW / 2;
                        }
                        sPos = i;
                        curLeft = getPaddingLeft();
                        curTop += mChildHeight + mVerticalInterval;
                    }
                    mViewPos[i * 2] = curLeft;
                    mViewPos[i * 2 + 1] = curTop;
                    curLeft += width + mHorizontalInterval;

                    if (i == childCount - 1) {
                        int leftW = getMeasuredWidth() - mViewPos[i * 2]
                                - childView.getMeasuredWidth() - getPaddingRight();
                        for (int j = sPos; j < childCount; j++) {
                            mViewPos[j * 2] = mViewPos[j * 2] + leftW / 2;
                        }
                    }
                } else {
                    if (curLeft + width - getPaddingLeft() > availableW) {
                        curLeft = getPaddingLeft();
                        curTop += mChildHeight + mVerticalInterval;
                    }
                    mViewPos[i * 2] = curLeft;
                    mViewPos[i * 2 + 1] = curTop;
                    curLeft += width + mHorizontalInterval;
                }
            }
        }

        // layout all child views
        for (int i = 0; i < mViewPos.length / 2; i++) {
            View childView = getChildAt(i);
            childView.layout(mViewPos[i * 2], mViewPos[i * 2 + 1],
                    mViewPos[i * 2] + childView.getMeasuredWidth(),
                    mViewPos[i * 2 + 1] + mChildHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setStyle(Paint.Style.STROKE);
    }

    private int getChildLines(int childCount) {
        int availableW = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int lines = 1;
        for (int i = 0, curLineW = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int dis = childView.getMeasuredWidth() + mHorizontalInterval;
            int height = childView.getMeasuredHeight();
            mChildHeight = i == 0 ? height : Math.min(mChildHeight, height);
            curLineW += dis;
            if (curLineW - mHorizontalInterval > availableW) {
                lines++;
                curLineW = dis;
            }
        }

        return mMaxLines <= 0 ? lines : mMaxLines;
    }

    private int[] onUpdateColorFactory() {
        int[] colors;
        colors = new int[]{mTagBackgroundColor, mTagBorderColor, mTagTextColor};
        return colors;
    }

    private void onSetTag() {
        if (mTags == null) {
            throw new RuntimeException("NullPointer exception!");
        }
        removeAllTags();
        if (mTags.size() == 0) {
            return;
        }
        for (int i = 0; i < mTags.size(); i++) {
            onAddTag(mTags.get(i), mChildViews.size());
        }
        postInvalidate();
    }

    private void onAddTag(String text, int position) {
        if (position < 0 || position > mChildViews.size()) {
            throw new RuntimeException("Illegal position!");
        }
        TagView tagView = new TagView(getContext(), text);
        initTagView(tagView, position);
        mChildViews.add(position, tagView);
        if (position < mChildViews.size()) {
            for (int i = position; i < mChildViews.size(); i++) {
                mChildViews.get(i).setTag(i);
            }
        } else {
            tagView.setTag(position);
        }
        addView(tagView, position);
    }

    private void initTagView(TagView tagView, int position) {
        int[] colors;
        if (mColorArrayList != null && mColorArrayList.size() > 0) {
            if (mColorArrayList.size() == mTags.size() &&
                    mColorArrayList.get(position).length >= 3) {
                colors = mColorArrayList.get(position);
            } else {
                throw new RuntimeException("Illegal color list!");
            }
        } else {
            colors = onUpdateColorFactory();
        }

        tagView.setTagBackgroundColor(colors[0]);
        tagView.setTagBorderColor(colors[1]);
        tagView.setTagTextColor(colors[2]);
        tagView.setTagMaxLength(mTagMaxLength);
        tagView.setTextDirection(mTagTextDirection);
        tagView.setTypeface(mTagTypeface);
        tagView.setBorderWidth(mTagBorderWidth);
        tagView.setBorderRadius(mTagBorderRadius);
        tagView.setTextSize(mTagTextSize);
        tagView.setHorizontalPadding(mTagHorizontalPadding);
        tagView.setVerticalPadding(mTagVerticalPadding);
        tagView.setBdDistance(mTagBdDistance);
        tagView.setTagSupportLettersRTL(mTagSupportLettersRTL);
    }

    private int ceilTagBorderWidth() {
        return (int) Math.ceil(mTagBorderWidth);
    }

    /**
     * Get current drag view state.
     *
     * @return
     */
    public int getTagViewState() {
        return mTagViewState;
    }

    /**
     * Set tags
     *
     * @param tags
     */
    public void setTags(List<String> tags) {
        mTags = tags;
        onSetTag();
    }

    /**
     * Inserts the specified TagView into this ContainerLayout at the end.
     *
     * @param text
     */
    public void addTag(String text) {
        addTag(text, mChildViews.size());
    }

    /**
     * Inserts the specified TagView into this ContainerLayout at the specified location.
     * The TagView is inserted before the current element at the specified location.
     *
     * @param text
     * @param position
     */
    public void addTag(String text, int position) {
        onAddTag(text, position);
        postInvalidate();
    }

    /**
     * Remove all TagViews.
     */
    public void removeAllTags() {
        mChildViews.clear();
        removeAllViews();
        postInvalidate();
    }


    /**
     * Set the TagView text max length(must greater or equal to 3).
     *
     * @param maxLength
     */
    public void setTagMaxLength(int maxLength) {
        mTagMaxLength = maxLength < TAG_MIN_LENGTH ? TAG_MIN_LENGTH : maxLength;
    }

    /**
     * Set TagView horizontal padding.
     *
     * @param padding
     */
    public void setTagHorizontalPadding(int padding) {
        int ceilWidth = ceilTagBorderWidth();
        this.mTagHorizontalPadding = padding < ceilWidth ? ceilWidth : padding;
    }

    /**
     * Set TagView vertical padding.
     *
     * @param padding
     */
    public void setTagVerticalPadding(int padding) {
        int ceilWidth = ceilTagBorderWidth();
        this.mTagVerticalPadding = padding < ceilWidth ? ceilWidth : padding;
    }
}
