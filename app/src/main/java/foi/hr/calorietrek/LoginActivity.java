package foi.hr.calorietrek;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, ILoginView {

    @BindView(R.id.btn_sign_in) SignInButton btnSignIn;

    LoginControllerImpl loginController = null;
    UserModel userModel = null;

    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginController = new LoginControllerImpl();
        GoogleSignInOptions gso = loginController.GmailLogin();
        mGoogleApiClient = GetGoogleApiClient(gso);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 007)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if(opr.isDone())
        {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }
        else
        {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Log.d(ProfileActivity.class.getSimpleName(), "onConnectionFailed:" + connectionResult);
    }

    @OnClick(R.id.btn_sign_in)
    public void onClick()
    {
        SignIn();
    }

    private GoogleApiClient GetGoogleApiClient(GoogleSignInOptions gso)
    {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        return mGoogleApiClient;
    }

    private void SignIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 007);
    }

    private void handleSignInResult(GoogleSignInResult result)
    {
        if(result.isSuccess())
        {
            LoginSuccessful(result);
        }
        else
        {
            LoginFailed();
        }
    }

    @Override
    public void LoginSuccessful(GoogleSignInResult result)
    {
        GoogleSignInAccount accountData = result.getSignInAccount();
        userModel = new UserModel(accountData.getDisplayName(), accountData.getEmail(), accountData.getPhotoUrl().toString());
        Intent sendData = new Intent(LoginActivity.this, ProfileActivity.class);
        sendData.putExtra("userModel", userModel);
        startActivity(sendData);
    }

    @Override
    public void LoginFailed()
    {

    }
}

