package com.dinaredition.doitsimple2.authorization;

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

//Задача данного фрагмента является в возвращении ID пользователя
// в случаях успешной авторизации

//Контроллер
public class AuthorizationFragment extends BaseFragment {
    private EditText mLoginTextEdit;
    private EditText mPassTextEdit;
    private Button mAuth;

    private static final int REQUEST_PERSON_ID = -1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.authorization_fragment, container, false);

        mAuth = view.findViewById(R.id.authorization_button);
        mLoginTextEdit = view.findViewById(R.id.login_authorization);
        mPassTextEdit = view.findViewById(R.id.pass_authorization);

        mAuth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String name = null;
                String surname = null;

                //Проверка на пустые поля(Возможно позже нужно перевести все эти проверки в отдельную функцию)
                if(mLoginTextEdit.getText().toString().isEmpty() || mPassTextEdit.getText().toString().isEmpty())
                    Toast.makeText(getActivity(), R.string.empty_warning,Toast.LENGTH_SHORT).show();

                else {
                    String qry = "SELECT IDp FROM person WHERE pLogin = " +
                            getCovStr(mLoginTextEdit.getText().toString()) +
                            " AND pPassword = " +
                            getCovStr(mPassTextEdit.getText().toString());

                    try {

                        Connection conn = ConnectToRemoteDb.Connect();
                        if(conn == null) makeMessage("БД failed");
                        else {

                            Statement statement = conn.createStatement();
                            ResultSet resultSet = statement.executeQuery(qry);

                            if (!resultSet.next()) makeMessage("Пользователь не найден");
                            else {

                               int tmpId= resultSet.getInt("IDp");
                               String mainInfoQry = "SELECT * FROM fullpersoninformation WHERE IDp = " + tmpId;
                               ResultSet infoRes = statement.executeQuery(mainInfoQry);
                               infoRes.next();

                               name = infoRes.getString("pName");
                               surname = infoRes.getString("pSurname");

                               Person.get(getActivity()).setPerson(
                                       tmpId,
                                       name,
                                       surname,
                                       infoRes.getInt("tCount"),
                                       infoRes.getInt("tCompleted"),
                                       infoRes.getInt("tFailed")
                               );
                            }
                        }

                    } catch (SQLException ex) {
                        makeMessage(ex.toString());

                    } catch (Exception ex){
                        ex.printStackTrace();
                    } finally {
                        makeMessage("Авторизация прошла успешно! \n Здравствуйте " + name + " " +surname +".");
                    }




                }

            }
        });

        return view;
    }
}
