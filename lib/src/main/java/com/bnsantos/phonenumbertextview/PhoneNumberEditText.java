package com.bnsantos.phonenumbertextview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.google.i18n.phonenumbers.AsYouTypeFormatter;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Locale;

/**
 * Created by bruno on 24/11/15.
 */
public class PhoneNumberEditText extends EditText {
    private PhoneNumberUtil phoneNumberUtil;
    private Phonenumber.PhoneNumber phoneNumber;
    private String region;

    public PhoneNumberEditText(Context context) {
        super(context);
        initRegion(context);
    }

    public PhoneNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRegion(context);
    }

    public PhoneNumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRegion(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PhoneNumberEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initRegion(context);
    }

    private void initRegion(Context context){
        this.phoneNumberUtil = PhoneNumberUtil.getInstance();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int countryCode = 0;
        if(telephonyManager!=null){
            region = telephonyManager.getSimCountryIso().toUpperCase();
            countryCode = this.phoneNumberUtil.getCountryCodeForRegion(this.region);
        }

        if(countryCode==0){
            this.region = Locale.getDefault().getCountry().toUpperCase();
            countryCode = this.phoneNumberUtil.getCountryCodeForRegion(this.region);
        }
        this.phoneNumber = new Phonenumber.PhoneNumber();
        setInputType(InputType.TYPE_CLASS_PHONE);
        addTextChangedListener(textWatcher);
        setText("+" + countryCode);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        private AsYouTypeFormatter asYouTypeFormatter;
        private String formattedPhone;

        private boolean selfChange = false;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.i("before_BRUNO", s.toString() + "-" + count);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            phoneNumber.setRawInput(s.toString());
            /*Log.i("on_BRUNO", s.toString() + "-" + count);
            if(selfChange){
                return;
            }

            if(asYouTypeFormatter==null){
                asYouTypeFormatter = phoneNumberUtil.getAsYouTypeFormatter(region);
            }

            phoneNumber.setRawInput()

            if(count>0){
                CharSequence newInput = s.subSequence(start, start + count);
                int length = newInput.length();
                for (int i=0;i<length;i++){
                    formattedPhone = asYouTypeFormatter.inputDigit(newInput.charAt(i));
                }
            }else if(before>0){
                asYouTypeFormatter.clear();
            }*/
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!selfChange&&phoneNumberUtil.isValidNumber(phoneNumber)){
                selfChange = true;
                int selectionEnd = getSelectionEnd();
                setText(phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));
                setSelection(selectionEnd<length()?selectionEnd:length());
            }else{
                selfChange = false;
            }
            /*if(!selfChange){
                int selectionEnd = getSelectionEnd();
                selfChange = true;
                setText(formattedPhone);
                setSelection(selectionEnd<length()?selectionEnd:length());
            }else{
                selfChange = false;
            }*/
                /*if(!selfChange){
                    int selectionEnd = getSelectionEnd();
                    selfChange = true;
                    AsYouTypeFormatter asYouTypeFormatter = phoneNumberUtil.getAsYouTypeFormatter(region);
                    String phone = "";
                    for(int i=0;i<s.length();i++){
                        phone = asYouTypeFormatter.inputDigit(s.charAt(i));
                    }
                    setText(phone.trim());
                    setSelection(selectionEnd);
                }else{
                    selfChange = false;
                }*/
        }
    };

    public String formatNational(){
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(getText().toString(), region);
            return "+" + phoneNumber.getCountryCode() + " " + phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        } catch (NumberParseException e) {
            e.printStackTrace();
            return getText().toString();
        }
    }

    public boolean isValid(){
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(getText().toString(), region);
            return phoneNumberUtil.isValidNumber(phoneNumber);
        } catch (NumberParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
