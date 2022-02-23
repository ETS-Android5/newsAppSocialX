package com.example.socialx;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;


public class signUpFragment extends Fragment {

    private EditText regUserName,regUserEmail,regUserPhone,regUserPassword;
    private CountryCodePicker regUserCountryCode;
    private FirebaseAuth UserAuth;
    private DatabaseReference dataRef;
    private ConstraintLayout regUserBtn;
    private ProgressDialog LoadingBar;
    private CheckBox regUserAcceptAgreement;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View  v=inflater.inflate(R.layout.fragment_sign_up, container, false);

        regUserName=v.findViewById(R.id.sign_Up_user_name);
        regUserEmail=v.findViewById(R.id.sign_Up_user_email);
        regUserPhone=v.findViewById(R.id.sign_Up_user_phone);
        regUserPassword=v.findViewById(R.id.sign_Up_user_password);

        regUserCountryCode=v.findViewById(R.id.sign_Up_user_country_code);
        regUserBtn=v.findViewById(R.id.sign_Up_user_regbtn);
        LoadingBar = new ProgressDialog(getContext());
        regUserAcceptAgreement=v.findViewById(R.id.sign_Up_user_check_box);

        regUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
        UserAuth=FirebaseAuth.getInstance();


        return v;
    }

    private void CreateAccount(){

        String UserRegName=regUserName.getText().toString().trim();
        String UserRegEmail=regUserEmail.getText().toString().trim();
        String UserRegPhone=regUserPhone.getText().toString().trim();
        String UserRegPassword=regUserPassword.getText().toString().trim();
        String UserRegCountryCode=regUserCountryCode.getSelectedCountryCode();
        dataRef= FirebaseDatabase.getInstance().getReference();


        if(TextUtils.isEmpty(UserRegName)){
            Toast.makeText(getContext(),"Please Enter User Name",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(UserRegPhone))
        {
            Toast.makeText(getContext(),"Please Enter User Phone",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(UserRegEmail))
        {
            Toast.makeText(getContext(),"Please Enter User Email",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(UserRegPassword))
        {
            Toast.makeText(getContext(),"Please Enter User Password",Toast.LENGTH_LONG).show();
        }
        else {
            if(regUserAcceptAgreement.isChecked())
            {
                LoadingBar.setTitle("Creating New Account");
                LoadingBar.setMessage("Please Wait While Account Is Being Created...");
                LoadingBar.setCanceledOnTouchOutside(true);
                LoadingBar.show();
                UserAuth.createUserWithEmailAndPassword(UserRegEmail, UserRegPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String currentUserId = UserAuth.getCurrentUser().getUid();

                            HashMap<String, Object> userHashMap=new HashMap();
                            userHashMap.put("userName",UserRegName);
                            userHashMap.put("userEmail",UserRegEmail);
                            userHashMap.put("userPhone",UserRegPhone);
                            userHashMap.put("userCountryCode",UserRegCountryCode);

                            dataRef.child("users").child(currentUserId).updateChildren(userHashMap);
                            sendusertoMainActivity();
                            Toast.makeText(getContext(), "Account Created Successfully", Toast.LENGTH_LONG).show();

                            LoadingBar.dismiss();
                        } else {
                            String errormess = task.getException().toString();
                            Toast.makeText(getContext(), "Error : " + errormess, Toast.LENGTH_LONG).show();
                            LoadingBar.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed To Create Account!!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else
            {
                Toast.makeText(getContext(), "Please Accept the agreement!!", Toast.LENGTH_LONG).show();

            }

        }
    }
    private void sendusertoMainActivity(){
        Intent mainintent= new Intent(getContext(),MainActivity.class);
        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        getActivity().finish();
    }
}