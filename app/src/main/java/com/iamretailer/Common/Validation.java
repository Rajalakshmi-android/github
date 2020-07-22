package com.iamretailer.Common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation
{
    public static boolean isValidMail(String email) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();

        if(!check) {
            return check;
        }
        else {

            return check;
        }
    }




    public static boolean validateName( String firstName )
    {

        CharSequence inputStr = firstName;
        String seq="^[a-zA-Z\\s]*$";
        Pattern pattern = Pattern.compile(seq);
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches())
        {
            //if pattern matches
            return true;
        }
        else
        {
            return false;//if pattern does not matches
        }
    }



    public boolean isValidPhone(String phone)
    {
        boolean check=false;
        String regexStr = "^[0-9]{10}$";
        if(!Pattern.matches(regexStr, phone))
        {
            if(phone.length() < 6 || phone.length() > 13)
            {
                check = false;

            }
            else
            {
                check = true;

            }
        }
        else
        {
            check=false;
        }
        return check;
    }





}
