package com.vikram.locateme.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.vikram.locateme.utils.Constants;
import com.vikram.locateme.utils.TypeFace;

/**
 * Created by M1032130 on 6/16/2017.
 */

public class MyEditText extends EditText {
    private static final String asset = Constants.FONT_NAME;

    public MyEditText(Context context) {
        super(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, asset);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, asset);
    }

    public boolean setCustomFont(Context ctx, String asset) {
        setTypeface(TypeFace.get(ctx, asset));
        return true;
    }
}