package com.dinaredition.doitsimple2.authorization;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.dinaredition.doitsimple2.Person;
import com.dinaredition.doitsimple2.R;
import com.dinaredition.doitsimple2.database.ConnectToRemoteDb;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//Отвечает за регистрацию пользователя в бд
// и возвращает ID зарегестрированного пользователя для дальнейшей авторизации
//Представление фрагмента содержит элементы пользовательского интерфейса,
// с которыми будет взаимодействовать пользователь.

//Контроллер
public class RegistrationFragment  extends BaseFragment {
    protected int mPersonId;
    private final String PERSON_ID = "person_id";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPersonId = -1;
    }


    //Взаимодействие с интерфейсом и сама регистрация
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment, container, false);

        Button RegisterButton = view.findViewById(R.id.register_button);
        final EditText Login = view.findViewById(R.id.PersonLogin);
        final EditText Pass = view.findViewById(R.id.PersonLogin);
        final EditText Surname = view.findViewById(R.id.surname_text);
        final EditText Name = view.findViewById(R.id.name_text);

        final ConnectToRemoteDb connectToRemoteDb = new ConnectToRemoteDb();
        RegisterButton.setOnClickListener(view1 -> {

            //Если хоть одно из полей пустое вывести соотвествующее окно
            if (Login.getText().toString().isEmpty() || Pass.getText().toString().isEmpty() ||
                    Surname.getText().toString().isEmpty() || Name.getText().toString().isEmpty())

                Toast.makeText(getActivity(),"Пусто", Toast.LENGTH_SHORT).show();

            else {

                try{
                    Connection conn = ConnectToRemoteDb.Connect();

                    if(conn == null) makeMessage("Нет соединения с БД");
                    else if (!isOnline(getActivity())) makeMessage("Отсутсвует интернет соединение"); //????
                    else {
                        String tmpPass = Pass.getText().toString();
                        String qry = "INSERT INTO person (pName,pSurname,pLogin, pPassword) " +
                                "VALUE (" + getCovStr(Name.getText().toString()) + "," + getCovStr(Surname.getText().toString())  + " , "
                                + getCovStr(Login.getText().toString()) + " , " + getCovStr(tmpPass) + ")";

                        Statement stmt = conn.createStatement();
                        stmt.executeUpdate(qry);


                        //Фиксация зарегестрированного пользователя в приложении(Чтобы
                        // дополнительно не проводить авторизацию)
                        String qry1 = "SELECT IDp FROM person WHERE pLogin =" +
                                getCovStr(Login.getText().toString()) +
                                " AND pPassword = " + getCovStr(tmpPass);

                        ResultSet resultSet = stmt.executeQuery(qry1);
                        resultSet.next();
                        Person.get(getActivity()).setPerson(
                                resultSet.getInt("IDp"),
                                Name.getText().toString(),
                                Surname.getText().toString(),
                                0,
                                0,
                                0
                        );

                    }

                } catch (SQLException e) {

                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
                    e.printStackTrace();

                } finally {

                    Toast.makeText(getActivity(), "Успешно добавлено!", Toast.LENGTH_SHORT).show();

                }

            }
        });



        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle personId = new Bundle();
        personId.putInt(PERSON_ID, mPersonId);
    }

}
