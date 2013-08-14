package org.hk.training.toilet.util;

import android.widget.EditText;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Validation {

    private class ValidationPair {
        public final EditText editText;
        public final Validate validate;

        public ValidationPair(EditText editText, Validate validate) {
            this.editText = editText;
            this.validate = validate;
        }

        public boolean isError() {
            return !this.validate.result();
        }
    }

    private ArrayList<ValidationPair> validationPairList = Lists.newEmptyArrayList();

    public Validate add(final EditText editText, final String val) {
        Validate v = new Validate(editText, val);
        validationPairList.add(new ValidationPair(editText, v));
        return v;
    }

    public ArrayList<EditText> getEditTextList(){
        ArrayList<EditText> result = Lists.newEmptyArrayList();
        for(ValidationPair p : validationPairList){
            result.add(p.editText);
        }
        return result;
    }

    public void requestFocus() {
        for (ValidationPair p : validationPairList) {
            if (p.isError()) {
                p.editText.requestFocus();
                break;
            }
        }
    }

    public boolean hasError() {
        for (ValidationPair p : validationPairList) {
            if (p.isError()) {
                return true;
            }
        }
        return false;
    }

    public class Validate {
        private final EditText editText;
        private final String target;
        private final ArrayList<String> errorMessageList = Lists.newEmptyArrayList();

        public Validate(final EditText editText, final String val) {
            this.editText = editText;
            this.target = val.toString().trim();
        }

        public Validate blank(String message) {
            if (target == null || target.trim().length() == 0) {
                errorMessageList.add(message);
            }
            return this;
        }

        public Validate mail(String message){
            if(target != null){
                try {
                    new InternetAddress(target, true);
                } catch (AddressException e) {
                    errorMessageList.add(message);
                }
            }
            return this;
        }

        public Validate equal(String message, String src){
            if(target != null){
                if(!target.equals(src)){
                    errorMessageList.add(message);
                }
            }
            return this;
        }

        public Validate integer(String message) {
            if (target != null && target.trim().length() != 0 && !IntegerUtil.isInt(target)) {
                errorMessageList.add(message);
            }
            return this;
        }

        public Validate decimal(String message, int decimalPoint) {
            if (target != null && target.trim().length() != 0 && IntegerUtil.isDouble(target)) {
                if (target.indexOf(".") != -1) {
                    String[] str = target.split("\\.");
                    if (str[1].length() != decimalPoint) {
                        errorMessageList.add(message);
                    }
                }
            }
            return this;
        }

        public Validate range(String message, int start, int end) {
            if (target != null && target.trim().length() != 0 && IntegerUtil.isInt(target)) {
                int i = IntegerUtil.toInt(target, 0);
                if (start > i || i > end) {
                    errorMessageList.add(message);
                }
            }
            return this;
        }

        public Validate decimalPoint(String message, int point){
            if (target != null && target.trim().length() != 0 ) {
                BigDecimal val = new BigDecimal(target);
                if(val.scale() > point){
                    errorMessageList.add(message);
                }
            }
            return this;
        }

        public Validate range(String message, double start, double end) {
            if (target != null && target.trim().length() != 0 && IntegerUtil.isDouble(target)) {
                double i = IntegerUtil.toDouble(target, 0);
                if (start > i || i > end) {
                    errorMessageList.add(message);
                }
            }
            return this;
        }

        public Validate minLength() {
            return this;
        }

        public Validate maxLength(String message, int max) {
            if (target != null && target.trim().length() != 0) {
                if (target.length() > max) {
                    errorMessageList.add(message);
                }
            }
            return this;
        }

        public void errorToEditText() {
            if (errorMessageList.isEmpty() || errorMessageList.size() == 0) {
                editText.setError(null);
            } else {
                editText.setError(this.firstError());
            }
        }

        public boolean result() {
            return errorMessageList.size() == 0;
        }

        public String firstError() {
            return errorMessageList.get(0);
        }
    }
}
