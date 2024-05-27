package com.adanitownship.driver.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.adanitownship.driver.R;

@SuppressLint("AppCompatCustomView")
public class FincasysTextView extends TextView {

    String customFont;

    public FincasysTextView(Context context) {
        super(context);
    }

    public FincasysTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context, attrs);
    }

    public FincasysTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context, attrs);
    }

    public void setTextWithMarquee(String str) {
        this.setText(str);
        this.setSingleLine(true);
        this.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        this.setHorizontallyScrolling(true);
        this.setLines(1);
        this.setMarqueeRepeatLimit(-1);
        this.setSelected(true);
    }
    public void SetFont(Context context,String fontName){
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "font/" + fontName + ".ttf");
        setTypeface(tf);
    }

    public void SetFontSize(float size){
        setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
    }



    private void style(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomFontTextView);
        int cf = a.getInteger(R.styleable.CustomFontTextView_fontName, 0);
        int fontName = 0;
        switch (cf) {
            case 1:
                fontName = R.string.OpenSans_Bold;
                break;
            case 2:
                fontName = R.string.OpenSans_BoldItalic;
                break;
            case 3:
                fontName = R.string.OpenSans_ExtraBold;
                break;
            case 4:
                fontName = R.string.OpenSans_ExtraBoldItalic;
                break;
            case 5:
                fontName = R.string.OpenSans_Italic;
                break;
            case 6:
                fontName = R.string.OpenSans_Light;
                break;
            case 7:
                fontName = R.string.OpenSans_LightItalic;
                break;
            case 8:
                fontName = R.string.OpenSans_Regular;
                break;
            case 9:
                fontName = R.string.OpenSans_SemiBold;
                break;
            case 10:
                fontName = R.string.OpenSans_SemiBoldItalic;
                break;
            case 11:
                fontName = R.string.modern;
                break;
            case 12:
                fontName = R.string.courier_bold;
                break;
            case 13:
                fontName = R.string.roboto_black;
                break;
            case 14:
                fontName = R.string.sf_bold;
                break;
            default:
                fontName = R.string.OpenSans_Regular;
                break;
        }

        try {
            customFont = getResources().getString(fontName);

            Typeface tf = Typeface.createFromAsset(context.getAssets(),
                    "font/" + customFont + ".ttf");
            setTypeface(tf);
            a.recycle();
        } catch (Resources.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}