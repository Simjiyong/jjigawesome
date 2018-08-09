package kr.co.company.jjigawesome;

import android.text.TextUtils;
import android.widget.EditText;

/**
 * Created by BHY on 2018. 7. 20..
 */

public class ValidateForm {
    static String EMAIL_REGEX = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
    static String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";

    public static boolean checkForm(EditText... editTexts){
        boolean valid =true;

        for(int i=0;i<editTexts.length;i++) {
            String str = editTexts[i].getText().toString();
            if (TextUtils.isEmpty(str)) {
                editTexts[i].setError("입력해주세요.");
                valid = false;
            } else {
                editTexts[i].setError(null);
            }
        }
        return valid;
    }

}
