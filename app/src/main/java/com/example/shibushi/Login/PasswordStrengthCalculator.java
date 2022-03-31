package com.example.shibushi.Login;

import android.text.Editable;
import android.text.TextWatcher;
import androidx.lifecycle.MutableLiveData;

import com.example.shibushi.R;

import java.util.regex.Pattern;

public class PasswordStrengthCalculator implements TextWatcher {
    MutableLiveData<String> strengthLevel = new MutableLiveData<>();
    MutableLiveData<Integer> strengthColor = new MutableLiveData<>();

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence != null){
            calculateStrength(charSequence);
        }
    }

    private void calculateStrength(CharSequence password){
        if(password.length()>10&&hasSpecialCharacter(password)){
            strengthLevel.setValue("STRONG");
            strengthColor.setValue(R.color.strong);
        }
        else if(password.length()>7&&(hasLowerCase(password)||hasUpperCase(password))&&hasDigit(password)){
            strengthLevel.setValue("MEDIUM");
            strengthColor.setValue(R.color.medium);
        }
        else{
            strengthLevel.setValue("WEAK");
            strengthColor.setValue(R.color.weak);
        }
    }

    private boolean hasLowerCase(CharSequence password){
        Pattern pattern = Pattern.compile("[a-z]");
        return pattern.matcher(password).find();
    }

    private boolean hasUpperCase(CharSequence password){
        Pattern pattern = Pattern.compile("[A-Z]");
        return pattern.matcher(password).find();
    }

    private boolean hasDigit(CharSequence password){
        Pattern pattern = Pattern.compile("[0-9]");
        return pattern.matcher(password).find();
    }

    private boolean hasSpecialCharacter(CharSequence password){
        Pattern pattern = Pattern.compile("[!@#$%*,.<>?~`]");
        return pattern.matcher(password).find();
    }
}
