package com.example.layout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignupFragment extends Fragment {
    private Button signupbtn;
    private Utility utility;
    private EditText name, mobilenumber, password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        signupbtn = view.findViewById(R.id.signup_button);
        name = view.findViewById(R.id.signup_name);
        mobilenumber = view.findViewById(R.id.signup_mobilenumber);
        password = view.findViewById(R.id.signup_password);

        utility = new Utility();

        if (utility.getLoginStatus(getContext())) {
            // User is already logged in. So navigate to Main Activity
            utility.sendUserToNextActivty(getContext(), MainActivity.class);
            getActivity().finish();
            return view;
        }

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utility.dataPresence(mobilenumber.getText().toString(), new Utility.DataPresenceCallback() {
                    @Override
                    public void onDataPresenceResult(boolean exists) {
                        if(!exists){
                            String username = name.getText().toString();
                            String userpassword = password.getText().toString();
                            String usermobilenumber = mobilenumber.getText().toString();

                            if(validation(username, userpassword, usermobilenumber)){
                                utility.sendUserToNextActivty(getContext(), MainActivity.class);
                                utility.pushData(getContext(), username, usermobilenumber, userpassword);
                                utility.setLoginStatus(getContext(), true);
                                getActivity().finish();
                                utility.viewToast(getContext(), getString(R.string.loginsuccess));
                            }
                        }
                        else{
                            utility.viewToast(getContext(), getString(R.string.userexists));
                        }
                    }
                });
            }
        });
        return view;
    }

    private boolean validation(String username, String userpassword, String usermobilenumber) {
        if (username.isEmpty()) {
            name.setError(getString(R.string.validname));
            return false;
        }
        if (userpassword.length() < 6) {
            password.setError(getString(R.string.validpassword));
            return false;
        }
        if (usermobilenumber.length() != 10) {
            mobilenumber.setError(getString(R.string.validmobilenumber));
            return false;
        }
        return true;
    }

}