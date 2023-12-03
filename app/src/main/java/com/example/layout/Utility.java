package com.example.layout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Utility {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String LOGIN_STATUS_KEY = "loginstatus";
    private FirebaseFirestore root = FirebaseFirestore.getInstance();

    public void sendUserToNextActivty(Context context, Class<?> targetActivity){
        Intent intent = new Intent(context, targetActivity);
        context.startActivity(intent);
    }

    public void setLoginStatus(Context context, boolean isLoggedIn){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(LOGIN_STATUS_KEY, isLoggedIn);
        editor.apply();
    }

    public boolean getLoginStatus(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(LOGIN_STATUS_KEY, false);
    }

    public void viewToast(Context context, String message){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_toast, null);

        Toast toast = new Toast(context);
        toast.setView(view);

        TextView textView = view.findViewById(R.id.custom_toast_text);
        textView.setText(message);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }
    public void pushData(Context context, String name, String mobilenumber, String password){
        Map<String, String> map = new HashMap<>();
        map.put("Name", name);
        map.put("MobileNumber", mobilenumber);
        map.put("Password", password);

        DocumentReference documentReference = root.collection("Users").document(mobilenumber);
        documentReference.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    viewToast(context, context.getString(R.string.loginsuccess));
                }
                else{
                    viewToast(context, context.getString(R.string.loginfail));
                }
            }
        });
    }
    public interface DataPresenceCallback{
        void onDataPresenceResult(boolean exists);
    }
    public void dataPresence(String mobileNumber, DataPresenceCallback callback){
        root.collection("Users").whereEqualTo("MobileNumber", mobileNumber).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    boolean exists = !task.getResult().isEmpty();
                    callback.onDataPresenceResult(exists);
                }
                else{
                    callback.onDataPresenceResult(false);
                }
            }
        });
    }
    public interface VerificationCallback{
        void onVerificationResult(boolean userExists);
    }
    public void verification(String mobilenumber, String password, VerificationCallback callback){
        root.collection("Users")
                .whereEqualTo("MobileNumber", mobilenumber)
                .whereEqualTo("Password", password)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            boolean userExists = !task.getResult().isEmpty();
                            callback.onVerificationResult(userExists);
                        }
                        else {
                            callback.onVerificationResult(false);
                        }
                    }
                });
    }
}
