package com.adanitownship.driver.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.adanitownship.driver.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Objects;


public class FincasysDialog extends AlertDialog implements View.OnClickListener {

    public static final int DEFAULT_TYPE = 0;
    public static final int ERROR_TYPE = 1;
    public static final int SUCCESS_TYPE = 2;
    public static final int WARNING_TYPE = 3;
    public static final int DELETE_TYPE = 4;
    public static final int ALERT_TYPE = 5;
    public static final int LOGOUT_TYPE = 6;
    TextView tvTitleText, tvContentText;
    Button btnCancel, btnConfirm, btnNeutral;
    String strTitleText, strContentText, strCancelText, strConfirmText, strNeutralText;
    Context context;
    CircularImageView ivSuccess, ivDefault, ivError, ivWarning, ivDelete, ivAlert, ivLogout;
    View dialogView;
    private boolean hideCancelButton, hideConfirmButton, showNeutralButton, closeFromCancel;
    private boolean showTitleText, showContentText;
    private Drawable confirmColor, cancelColor, customImgDrawable;
    private FincasysDialogListener cancelClickListener;
    private FincasysDialogListener confirmClickListener;
    private FincasysDialogListener neutralClickListener;
    private int alertType = DEFAULT_TYPE;

   /* public FincasysDialog(@NonNull Context context) {
        super(context, R.style.alert_dialog_light);
        this.context = context;
    }*/

    public FincasysDialog(@NonNull Context context, int alertType) {
        super(context, R.style.FullScreenDialogStyleTrans);
        this.alertType = alertType;

    }

    public FincasysDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

        //setCancelable(true);
        //setCanceledOnTouchOutside(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.CENTER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);
        dialogView = Objects.requireNonNull(getWindow()).getDecorView().findViewById(android.R.id.content);
        tvTitleText = findViewById(R.id.tvTitleText);
        tvContentText = findViewById(R.id.tvContentText);
        ivWarning = findViewById(R.id.ivWarning);
        ivError = findViewById(R.id.ivError);
        ivDefault = findViewById(R.id.ivDefault);
        ivDelete = findViewById(R.id.ivDelete);
        ivSuccess = findViewById(R.id.ivSuccess);
        ivLogout = findViewById(R.id.ivLogout);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnNeutral = findViewById(R.id.btnNeutral);

        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnNeutral.setOnClickListener(this);

        setTitleText(strTitleText);
        setContentText(strContentText);
        setCancelText(strCancelText);
        setConfirmText(strConfirmText);
        setNeutralText(strNeutralText);
        changeAlertType(alertType, true);
    }

    private void changeAlertType(int alertType, boolean fromCreate) {
        this.alertType = alertType;
        switch (alertType) {
            case ERROR_TYPE:
                ivError.setVisibility(View.VISIBLE);
                break;
            case SUCCESS_TYPE:
                ivSuccess.setVisibility(View.VISIBLE);
                break;
            case WARNING_TYPE:
                ivWarning.setVisibility(View.VISIBLE);
                break;
            case DELETE_TYPE:
                ivDelete.setVisibility(View.VISIBLE);
                break;
            case ALERT_TYPE:
                ivAlert.setVisibility(View.VISIBLE);
                break;
            case DEFAULT_TYPE:
                ivDefault.setVisibility(View.VISIBLE);
                break;
            case LOGOUT_TYPE:
                ivLogout.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void changeAlertType(int alertType) {
        changeAlertType(alertType, false);
    }

    public FincasysDialog setTitleText(String text) {

        strTitleText = text;
        if (tvTitleText != null && strTitleText != null) {
            showTitleText();
            tvTitleText.setText(strTitleText);
        }
        return this;
    }

    private void showTitleText() {
        showTitleText = true;
        if (tvTitleText != null) {
            tvTitleText.setVisibility(View.VISIBLE);
        }
    }

    public FincasysDialog setContentText(String text) {

        strContentText = text;
        if (tvContentText != null && strContentText != null) {
            showContentText();
            tvContentText.setText(strContentText);
        }
        return this;
    }

    private void showContentText() {
        showContentText = true;
        if (tvContentText != null) {
            tvContentText.setVisibility(View.VISIBLE);
        }
    }

    public FincasysDialog setCancelText(String text) {
        strCancelText = text;
        if (btnCancel != null && strCancelText != null) {
            btnCancel.setText(strCancelText);
            hideCancelButton(false);
        }
        return this;
    }

    public FincasysDialog hideCancelButton(boolean isHide) {
        hideCancelButton = isHide;
        if (btnCancel != null) {
            btnCancel.setVisibility(hideCancelButton ? View.GONE : View.VISIBLE);
        }
        return this;
    }

    public FincasysDialog setConfirmText(String text) {
        strConfirmText = text;
        if (btnConfirm != null && strConfirmText != null) {
            btnConfirm.setText(strConfirmText);
            hideConfirmButton(false);
        }
        return this;
    }

    public FincasysDialog hideConfirmButton(boolean isHide) {
        hideConfirmButton = isHide;
        if (btnConfirm != null) {
            btnConfirm.setVisibility(hideConfirmButton ? View.GONE : View.VISIBLE);
        }

        return this;
    }

    public FincasysDialog setNeutralText(String text) {
        strNeutralText = text;
        if (btnNeutral != null && strNeutralText != null) {
            btnNeutral.setText(strNeutralText);
            showNeutralButton(true);
        }
        return this;
    }

    public FincasysDialog showNeutralButton(boolean isShow) {
        showNeutralButton = isShow;
        if (btnNeutral != null) {
            btnNeutral.setVisibility(showNeutralButton ? View.VISIBLE : View.GONE);
        }
        return this;
    }


    private FincasysDialog setConfirmButtonColor(Drawable background) {
        confirmColor = background;
        if (btnConfirm != null && confirmColor != null) {
            btnConfirm.setBackground(confirmColor);
        }
        return this;
    }

    private FincasysDialog setCancelButtonColor(Drawable background) {
        cancelColor = background;
        if (btnCancel != null && cancelColor != null) {
            btnCancel.setBackground(cancelColor);
        }
        return this;
    }

   /* private FincasysDialog setImageBackgroundColor(int background) {
        imageBackgroundColor = background;
        if (ivCustomImage != null) {
            ivCustomImage.setBackgroundColor(imageBackgroundColor);
        }
        return this;
    }*/

    /*public FincasysDialog setCustomImage(int resourceId) {
        return setCustomImage(getContext().getResources().getDrawable(resourceId));
    }*/

    /*private FincasysDialog setCustomImage(Drawable drawable) {
        customImgDrawable = drawable;
        if (ivCustomImage != null && customImgDrawable != null) {
            ivCustomImage.setVisibility(View.VISIBLE);
            ivCustomImage.setImageDrawable(customImgDrawable);
        }
        return this;
    }*/

    public FincasysDialog setCancelClickListener(FincasysDialogListener listener) {
        cancelClickListener = listener;
        return this;
    }

    public FincasysDialog setConfirmClickListener(FincasysDialogListener listener) {
        confirmClickListener = listener;
        return this;
    }

    public FincasysDialog setNeutralClickListener(FincasysDialogListener listener) {
        neutralClickListener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCancel) {
            if (cancelClickListener != null) {
                cancelClickListener.onClick(FincasysDialog.this);
            } else {
                dismiss();
            }
        } else if (v.getId() == R.id.btnConfirm) {
            if (confirmClickListener != null) {
                confirmClickListener.onClick(FincasysDialog.this);
            } else {
                dismiss();
            }
        } else if (v.getId() == R.id.btnNeutral) {
            if (neutralClickListener != null) {
                neutralClickListener.onClick(FincasysDialog.this);
            } else {
                dismiss();
            }
        }
    }

    public interface FincasysDialogListener {
        void onClick(FincasysDialog fincasysDialog);
    }
}
