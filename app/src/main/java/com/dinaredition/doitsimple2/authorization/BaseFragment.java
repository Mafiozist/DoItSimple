package com.dinaredition.doitsimple2.authorization;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

//Класс с реализованными методами, которые используются в обоих фрагментах
public class BaseFragment extends Fragment {

    public String getCovStr(String str){
        return "'"+str+"'";
    }

    public void makeMessage(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
    
    public String getHashPass(String str) {
        SecureRandom random = new SecureRandom();
        byte[]salt = new byte[512];
        byte[]salt2 = new byte[512];
        random.nextBytes(salt);
        random.nextBytes(salt2);
        MessageDigest md = null;
        try {
            //Выбран данный тип шифрования т.к. существует мнение о его безопасности
            md = MessageDigest.getInstance("SHA-512");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(salt);
        //md.update(salt2);
        byte[]hashedPassword = md.digest(str.getBytes(StandardCharsets.UTF_8));
        return hashedPassword.toString();
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
