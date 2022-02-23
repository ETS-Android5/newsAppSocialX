package com.example.socialx;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;

public class signInFragment extends Fragment {
    private FirebaseUser currentUser;
    private TextView userSignInRegisterNow,userSignInForgetPass;
    private EditText userSignInEmail,userSignInpassword;
    private ProgressDialog signInProgress;
    private FirebaseAuth userAuth;
    private ConstraintLayout userSignInBtn;
    private final static int RC_SIGN_IN=123;
    private ImageView userSignInGoogle,userSignInfaceBook;
    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_sign_in, container, false);

        userSignInpassword=v.findViewById(R.id.sign_in_user_password);
        userSignInEmail=v.findViewById(R.id.sign_in_user_email);
        userSignInGoogle=v.findViewById(R.id.sign_in_user_google_account);
        signInProgress = new ProgressDialog(getContext());
        mAuth = FirebaseAuth.getInstance();
        userAuth=FirebaseAuth.getInstance();
        currentUser=userAuth.getCurrentUser();


        mAuth = FirebaseAuth.getInstance();
        userSignInBtn=v.findViewById(R.id.sign_in_user_login_btn);
        userSignInRegisterNow=v.findViewById(R.id.sign_in_user_register_now);
        userSignInForgetPass=v.findViewById(R.id.sign_in_user_forget_pass);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        userSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        userSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        return v;
    }

    private void login(){
        String UserEmailSignIn= userSignInEmail.getText().toString();
        String UserPassSignIn= userSignInpassword.getText().toString();
        if(TextUtils.isEmpty(UserEmailSignIn)){
            Toast.makeText(getContext(),"Please enter the email",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(UserPassSignIn)){
            Toast.makeText(getContext(),"Please enter the correct password",Toast.LENGTH_SHORT).show();
        }
        else{
            signInProgress.setTitle("Signing In");
            signInProgress.setMessage("Please Wait...");
            signInProgress.setCanceledOnTouchOutside(true);
            signInProgress.show();
            userAuth.signInWithEmailAndPassword(UserEmailSignIn,UserPassSignIn).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        signInProgress.dismiss();
                        Toast.makeText(getContext(),"Logged in Successfully",Toast.LENGTH_LONG).show();
                        sendUserToMainActivity();
                    }
                    else{
                        String errormess=task.getException().toString();
                        Toast.makeText(getContext(),"Error :"+ errormess,Toast.LENGTH_LONG).show();
                        signInProgress.dismiss();
                    }
                }
            });
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        if (currentUser != null) {
            sendUserToMainActivity();
        }
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d("TAG", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            sendUserToMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("TAG", "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }

    private void sendUserToMainActivity() {
        Intent mainintent= new Intent(getContext(),MainActivity.class);
        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        getActivity().finish();
    }
}