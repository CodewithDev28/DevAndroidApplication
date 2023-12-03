package com.example.layout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SigninFragment extends Fragment {
    private Button signinbtn;
    private Utility utility;
    private EditText mobilenumber, password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        signinbtn = view.findViewById(R.id.signin_button);
        mobilenumber = view.findViewById(R.id.signin_mobilenumber);
        password = view.findViewById(R.id.signin_password);

        utility = new Utility();

        if(utility.getLoginStatus(getContext())){
            // User is already logged in. So navigate to Main Activity
            utility.sendUserToNextActivty(getContext(), MainActivity.class);
            getActivity().finish();
            return view;
        }

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utility.verification(mobilenumber.getText().toString(), password.getText().toString(), new Utility.VerificationCallback() {
                    @Override
                    public void onVerificationResult(boolean userExists) {
                        if(userExists){
                            utility.viewToast(getContext(), "Welcome Back");
                            utility.sendUserToNextActivty(getContext(), MainActivity.class);
                            utility.setLoginStatus(getContext(), true);
                            getActivity().finish();
                        }
                        else{
                            utility.viewToast(getContext(), "User Not Found");
                        }
                    }
                });
            }
        });

        return view;
    }

}