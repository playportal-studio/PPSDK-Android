package com.dynepic.ppsdk_android.utils;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class is for utilities that aren't specific to anything else.
 * Most methods are self-explanatory
 */

public class _Utils {
    //ToDo: Remove if not needed
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    private static final Pattern EMOTICONS_REGEX_1 = Pattern.compile("\\p{InEmoticons}");
    private static final Pattern EMOTICONS_REGEX_2 = Pattern.compile("/[\\u2190-\\u21FF]|[\\u2600-\\u26FF]|[\\u2700-\\u27BF]|[\\u3000-\\u303F]|[\\u1F300-\\u1F64F]|[\\u1F680-\\u1F6FF]/g");
    private static final Pattern NEG_REGEX = Pattern.compile("[^A-Za-z]");
    private static Boolean refreshing = false;

    public static boolean isValidEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        Matcher matcherEmoji = EMOTICONS_REGEX_1.matcher(emailStr);
        Matcher matcherEmojiOther = EMOTICONS_REGEX_2.matcher(emailStr);
        return (matcher.find() & !matcherEmoji.find() & matcherEmojiOther.find());
    }

    public static boolean isValidName(String name){

        Matcher negMatcher = NEG_REGEX.matcher(name);
        return(!negMatcher.find() && (name.length()>2));
    }

    public static boolean isValidHandle(String name){
        Matcher matcher = EMOTICONS_REGEX_1.matcher(name);
        Matcher matcherEmojiOther = EMOTICONS_REGEX_2.matcher(name);
        return (name.length()>=3 & !matcher.find() & matcherEmojiOther.find());
    }

    public static boolean isValidAge(int age){
        return(age<=120);
    }

    public static boolean isValidPassword(String passwd){
        final Pattern hasUppercase = Pattern.compile("[A-Z]");
        final Pattern hasLowercase = Pattern.compile("[a-z]");
        final Pattern hasNumber = Pattern.compile("\\d");
        Matcher matcher = EMOTICONS_REGEX_1.matcher(passwd);
        Matcher matcherEmojiOther = EMOTICONS_REGEX_2.matcher(passwd);

        if (passwd.equals("")) {
            return false;
        }
        else if (matcher.find()){
            return false;
        }
        else if (!matcherEmojiOther.find()){
            return false;
        }
        else if (passwd.isEmpty()) {
            return false;
        }
        else if (passwd.length() < 8) {
            return false;
        }
        else if (!hasUppercase.matcher(passwd).find()) {
            return false;
        }
        else if (!hasLowercase.matcher(passwd).find()) {
            return false;
        }
        else if (!hasNumber.matcher(passwd).find()) {
            return false;
        }
        else{
            return true;
        }

    }

    public static boolean isValidPasswordEmpty(String passwd){
        if (passwd == null) {
            return false;
        }
        else if (passwd.isEmpty()) {
            return false;
        }
        else{
            return true;
        }
    }

    public static boolean isValidPasswordSize(String passwd){
        return (passwd.length() > 8);
    }

    public static boolean isValidPasswordCase(String passwd){
        final Pattern hasUppercase = Pattern.compile("[A-Z]");
        final Pattern hasLowercase = Pattern.compile("[a-z]");

        if (!hasUppercase.matcher(passwd).find()) {
            return false;
        }
        else if (!hasLowercase.matcher(passwd).find()) {
            return false;
        }
        else{
            return true;
        }
    }

    public static boolean isValidPasswordNum(String passwd){
        final Pattern hasNumber = Pattern.compile("\\d");
        return (hasNumber.matcher(passwd).find());
    }

    public static boolean isVaildDay(int day){
        return(day<=31 && day>0);
    }

    public static boolean isValidMonth(int month){
        return(month<=12 && month>0);
    }

    public static void showSnackBar(View view, String text){
        final Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public static void showSnackBarIndefinite(View view, String text){
        final Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public static ZonedDateTime dateTimeFromString(String datestring)
    {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            return ZonedDateTime.parse(datestring, formatter);
        }
        catch (DateTimeParseException exc) {
            Log.e("%s is not parsable!%n", datestring);
            ZonedDateTime date = ZonedDateTime.now();
            date.minusHours(4);
            return date;
        }
    }

    public static String stringFromDateTime(ZonedDateTime dateTime)
    {
        try {
            DateTimeFormatter format = DateTimeFormatter.ISO_DATE_TIME;
            return dateTime.format(format);
        }
        catch (DateTimeException exc) {
            Log.e("dateTime input can't be formatted! ", dateTime.toString());
            throw exc;
        }
    }

}

