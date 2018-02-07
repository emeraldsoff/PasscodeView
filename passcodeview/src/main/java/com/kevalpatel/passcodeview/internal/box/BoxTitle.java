/*
 * Copyright 2017 Keval Patel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kevalpatel.passcodeview.internal.box;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.kevalpatel.passcodeview.R;
import com.kevalpatel.passcodeview.Utils;
import com.kevalpatel.passcodeview.internal.BasePasscodeView;
import com.kevalpatel.passcodeview.internal.Constants;

/**
 * Created by Keval Patel on 09/04/17.
 * This {@link Box} is to display title and passcode indicator. The number of the passcode indicator
 * will depend on the length of the passcode indicator.
 *
 * @author 'https://github.com/kevalpatel2106'
 */

public final class BoxTitle extends Box {
    private static final String DEF_TITLE_TEXT = "Enter pattern";

    @ColorInt
    private int mTitleColor;                        //Title text color
    private String mTitle;                          //Title color

    private Paint mTitlePaint;                      //Solid indicator color

    private Rect mBounds;

    public BoxTitle(@NonNull BasePasscodeView view) {
        super(view);
    }

    @Override
    public void init() {
        //NO OP
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setDefaults() {
        mTitle = DEF_TITLE_TEXT;
        mTitleColor = Utils.getColorCompat(getContext(), R.color.lib_key_default_color);
    }

    @Override
    public void onAuthenticationFail() {
        //TODO handle failure
    }

    @Override
    public void onAuthenticationSuccess() {
        //TODO handle success
    }

    @Override
    public void reset() {
        //TODO Reset the view.
    }

    @Override
    public void drawView(@NonNull Canvas canvas) {
        if (mTitle == null) return;

        //Write title text
        canvas.drawText(mTitle,
                mBounds.exactCenterX(),
                mBounds.top - (int) getContext().getResources().getDimension(R.dimen.lib_divider_vertical_margin),
                mTitlePaint);
    }

    /**
     * |------------------------|=|
     * |                        | |
     * |                        | |
     * |         TITLE          | | => 2 * {@link R.dimen#lib_divider_vertical_margin} px up from the bottom of the indicators.
     * |------------------------|=| => {@link Constants#KEY_BOARD_TOP_WEIGHT} of the total height.
     * |                        | |
     * |                        | |
     * |                        | | => Keypad height. ({@link BoxKeypad#measureView(Rect)})
     * |                        | |
     * |                        | |
     * |------------------------|=| => {@link Constants#KEY_BOARD_BOTTOM_WEIGHT} of the total weight if the fingerprint is available. Else it touches to the bottom of the main view.
     * |                        | | => Section for fingerprint. If the fingerprint is enabled. Otherwise keyboard streaches to the bottom of the root view.
     * |------------------------|=|
     * Don't change until you know what you are doing. :-)
     *
     * @param rootViewBounds {@link Rect} bounds of the main view.
     */
    @Override
    public void measureView(@NonNull Rect rootViewBounds) {
        //Title box bounds
        mBounds = new Rect();
        mBounds.left = rootViewBounds.left;
        mBounds.right = rootViewBounds.right;
        mBounds.bottom = rootViewBounds.top
                + (int) (rootViewBounds.height() * Constants.KEY_BOARD_TOP_WEIGHT
                - 2 * getContext().getResources().getDimension(R.dimen.lib_divider_vertical_margin));
        mBounds.top = (int) (mBounds.bottom - mTitlePaint.getTextSize());
    }

    @Override
    public void preparePaint() {
        //Set title paint
        mTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTitlePaint.setColor(mTitleColor);
        mTitlePaint.setTextAlign(Paint.Align.CENTER);
        mTitlePaint.setTextSize(getContext().getResources().getDimension(R.dimen.lib_title_text_size));
    }

    @Override
    public void parseTypeArr(@NonNull AttributeSet typedArray) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(typedArray,
                R.styleable.PatternView, 0, 0);

        try { //Parse title params
            mTitle = a.hasValue(R.styleable.PatternView_titleText) ?
                    a.getString(R.styleable.PatternView_titleText) : DEF_TITLE_TEXT;
            mTitleColor = a.getColor(R.styleable.PatternView_titleTextColor,
                    getContext().getResources().getColor(R.color.lib_key_default_color));
        } finally {
            a.recycle();
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    @ColorInt
    public int getTitleColor() {
        return mTitleColor;
    }

    public void setTitleColor(@ColorInt int titleColor) {
        this.mTitleColor = titleColor;
        preparePaint();
    }
}