package com.example.medminder;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

public class HelpButtonCustom extends MaterialButton {

    public HelpButtonCustom(@NonNull Context context) {
        super(context);
        init();
    }

    public HelpButtonCustom(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HelpButtonCustom(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        //these are the default Button attributes
        setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
        setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
        setText("Help");
    }

}