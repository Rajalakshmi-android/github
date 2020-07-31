package com.iamretailer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.POJO.AddressPO;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.DBController;
import com.iamretailer.Common.Validation;
import com.squareup.picasso.Picasso;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import stutzen.co.network.Connection;


public class Login extends Language implements GoogleApiClient.OnConnectionFailedListener {

    TextView register, forget;
    FrameLayout login;
    EditText username, password;
    DBController db;
    String cus_id;
    Validation validation;
    private int from;
    Dialog open;
    LinearLayout fullayout;
    View popup;
    LinearLayout guest;
    LinearLayout order;
    AndroidLogger logger;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    LoginButton loginButton;
    SignInButton signInButton;
    private GoogleSignInClient googleSignInClient;
    LinearLayout facebook, gmail_login;
    private GoogleApiClient mGoogleApiClient;
    int has_ship;
    private ArrayList<Object> addressPOS;
    private ProgressDialog pDialog;
    LinearLayout pass;
    ImageView hide, show;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);


        register = (TextView) findViewById(R.id.registor);


        // Build a GoogleSignInClient with the options specified by gso.

        forget = (TextView) findViewById(R.id.forget_pass);
        login = (FrameLayout) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        fullayout = (LinearLayout) findViewById(R.id.fullayout);
        guest = (LinearLayout) findViewById(R.id.guest);
        order = (LinearLayout) findViewById(R.id.order);
        db = new DBController(Login.this);
        Appconstatants.sessiondata = db.getSession();
        Appconstatants.Lang=db.get_lang_code();
        Appconstatants.CUR=db.getCurCode();
        facebook = (LinearLayout) findViewById(R.id.facebook);
        gmail_login = (LinearLayout) findViewById(R.id.gmail_login);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        pass = (LinearLayout) findViewById(R.id.pass);
        hide = (ImageView) findViewById(R.id.hide);
        show = (ImageView) findViewById(R.id.show);

        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();
        from = getIntent().getIntExtra("from", 0);
        has_ship=getIntent().getIntExtra("has_ship",1);
        if (from == 1 || from == 3 || from == 4) {
            guest.setVisibility(View.GONE);
           // order.setVisibility(View.GONE);
        } else {
            guest.setVisibility(View.VISIBLE);
            //order.setVisibility(View.VISIBLE);
        }

        validation = new Validation();
        String serverClientId = getString(R.string.server_client_id);

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //    .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(serverClientId)
                .requestEmail()
                .requestId().requestIdToken(serverClientId)

                .build();


        googleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        // googleSignInClient.signOut();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });


        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });
        gmail_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Registration.class);
                intent.putExtra("from", from);
                intent.putExtra("has_ship",has_ship);
                startActivityForResult(intent,1);
                finish();
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopup();
                username.setText("");
                password.setText("");

            }
        });
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserProfile(AccessToken.getCurrentAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().trim().equals("")) {
                    username.setError(getResources().getString(R.string.mail_id));
                }
                if (password.getText().toString().trim().equals("")) {
                    password.setError(getResources().getString(R.string.pass_word));
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches()) {
                    username.setError(getResources().getString(R.string.valid_mail));
                }
                if (!username.getText().toString().trim().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches()
                        && !password.getText().toString().trim().isEmpty()) {
                    LoginTask loginTask = new LoginTask();
                    loginTask.execute(username.getText().toString().trim(), password.getText().toString().trim());
                }

            }
        });

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,GuestCheckout.class);
                intent.putExtra("has_ship",has_ship);
                startActivity(intent);
            }
        });

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.iamretailer", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }


        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show.getVisibility() == View.VISIBLE) {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    hide.setVisibility(View.VISIBLE);
                    show.setVisibility(View.GONE);
                } else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    show.setVisibility(View.VISIBLE);
                    hide.setVisibility(View.GONE);
                }

            }
        });



    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class LoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            Log.d("Login", "started");


            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... param) {
            logger.info("Login api" + Appconstatants.login_api);

            String response = null;
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                json.put("email", param[0]);
                json.put("password", param[1]);
                Log.d("Login_format", json.toString());
                Log.d("Login_ses", db.getSession());
                Log.d("Login_ses", Appconstatants.login_api);

                Log.i("tag", "login" + Appconstatants.sessiondata);
                logger.info("Login api req" + json);
                response = connection.sendHttpPostjson(Appconstatants.login_api, json, db.getSession(), Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,Login.this);
                logger.info("Login api resp" + response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {

            pDialog.dismiss();
            Log.i("Login", "Login--?" + resp);
            Log.d("login_ss", resp + "");
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        JSONObject jsonObject = new JSONObject(json.getString("data"));
                        cus_id = jsonObject.isNull("customer_id") ? "" : jsonObject.getString("customer_id");
                        String cus_grp_id = jsonObject.isNull("customer_group_id") ? "" : jsonObject.getString("customer_group_id");
                        String cus_store = jsonObject.isNull("store_id") ? "" : jsonObject.getString("store_id");
                        String cus_lang = jsonObject.isNull("language_id") ? "" : jsonObject.getString("language_id");
                        String cus_f_name = jsonObject.isNull("firstname") ? "" : jsonObject.getString("firstname");
                        String cus_l_name = jsonObject.isNull("lastname") ? "" : jsonObject.getString("lastname");
                        String cus_email = jsonObject.isNull("email") ? "" : jsonObject.getString("email");
                        String cus_mobile = jsonObject.isNull("telephone") ? "" : jsonObject.getString("telephone");
                        String cus_add_id = jsonObject.isNull("address_id") ? "" : jsonObject.getString("address_id");
                        String cus_wish = jsonObject.isNull("wishlist_total") ? "" : jsonObject.getString("wishlist_total");
                        String cus_msg = jsonObject.isNull("message") ? "" : jsonObject.getString("message");
                        int cus_cart_count = jsonObject.getInt("cart_count_products");
                        Toast.makeText(Login.this, "User Logged In", Toast.LENGTH_SHORT).show();
                        db.user_data(cus_id, cus_f_name + " " + cus_l_name, cus_email, cus_mobile);
                        if (from == 2) {
                            Intent intent = new Intent();
                            setResult(3, intent);
                            finish();
                        } else if (from == 1) {
                            finish();
                        } else if (from == 3) {
                            Intent intent = new Intent();
                            setResult(3, intent);
                            finish();

                        } else if (from == 4) {
                            finish();
                        }else if (from == 5) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), MyProfile.class));
                        }


                    } else {
                        pDialog.dismiss();
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(Login.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    pDialog.dismiss();
                    e.printStackTrace();
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    login.performClick();

                                }
                            })
                            .show();
                }

            } else {
                pDialog.dismiss();
                Snackbar.make(fullayout, R.string.error_net, Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                login.performClick();
                            }
                        })
                        .show();
            }
        }
    }
    private class AddressListTask extends AsyncTask<Object, Void, String> {



        @Override
        protected void onPreExecute() {

            Log.d("Add_list", "started");
        }

        protected String doInBackground(Object... param) {
            logger.info("Add_list_api" + Appconstatants.addres_list);

            String response = null;
            Connection connection = new Connection();
            try {
                Log.d("Add_list_api", Appconstatants.addres_list);
                Log.d("Add_list_api", Appconstatants.sessiondata);

                response = connection.connStringResponse(Appconstatants.addres_list, Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,Login.this);
                logger.info("Add_list_resp" + response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            pDialog.dismiss();
            Log.i("Add_list", "Add_list--->  " + resp);
            if (resp != null) {

                try {
                    JSONObject json = new JSONObject(resp);
                    addressPOS = new ArrayList<>();
                    if (json.getInt("success") == 1) {

                        JSONObject object = json.getJSONObject("data");
                        JSONArray array = object.getJSONArray("addresses");
                        if (array.length() > 0) {
                            for (int y = 0; y < array.length(); y++) {
                                JSONObject object1 = array.getJSONObject(y);
                                AddressPO addressPO = new AddressPO();
                                addressPO.setAdd_id(object1.isNull("address_id") ? "" : object1.getString("address_id"));
                                addressPO.setF_name(object1.isNull("firstname") ? "" : object1.getString("firstname"));
                                addressPO.setL_name(object1.isNull("lastname") ? "" : object1.getString("lastname"));
                                addressPO.setPhone(object1.isNull("phone") ? "" : object1.getString("phone"));
                                addressPO.setCompany(object1.isNull("company") ? "" : object1.getString("company"));
                                addressPO.setAdd_1(object1.isNull("address_1") ? "" : object1.getString("address_1"));
                                addressPO.setAdd_2(object1.isNull("address_2") ? "" : object1.getString("address_2"));
                                addressPO.setPost_code(object1.isNull("postcode") ? "" : object1.getString("postcode"));
                                addressPO.setCity(object1.isNull("city") ? "" : object1.getString("city"));
                                addressPO.setZone(object1.isNull("zone") ? "" : object1.getString("zone"));
                                addressPO.setCountry(object1.isNull("country") ? "" : object1.getString("country"));
                                if (object1.getString("address_1").length()>0)
                                addressPOS.add(addressPO);


                            }

                            if (addressPOS.size()>0) {

                                Intent i = new Intent(Login.this, AddressList.class);
                                Bundle nn = new Bundle();
                                nn.putInt("from", 1);
                                nn.putInt("has_ship", has_ship);
                                i.putExtras(nn);
                                startActivity(i);
                                finish();
                            }
                            else
                            {

                                Intent intent = new Intent(Login.this, Address.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("from", 2);
                                bundle.putInt("has_ship",has_ship);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();

                            }


                        } else {
                            Intent intent = new Intent(Login.this, Address.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("from", 2);
                            bundle.putInt("has_ship",has_ship);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                        pDialog.dismiss();



                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(Login.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    login.performClick();

                                }
                            })
                            .show();
                }

            } else {
                Snackbar.make(fullayout, R.string.error_net, Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                login.performClick();
                            }
                        })
                        .show();
            }
        }
    }
    public void showpopup() {
        open = new Dialog(Login.this);
        open.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popup = getLayoutInflater().inflate(R.layout.forgot_password, null);
        TextView gologin = (TextView) popup.findViewById(R.id.gologin);
        FrameLayout submit = (FrameLayout) popup.findViewById(R.id.submit);
        final EditText username = (EditText) popup.findViewById(R.id.username);
       // LinearLayout pass_layout = (LinearLayout) popup.findViewById(R.id.pass_layout);
        //LinearLayout back=(LinearLayout)popup.findViewById(R.id.back);
        final PopupWindow pwindow = new PopupWindow(popup,
                ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT, true);
        Display display = getWindowManager().getDefaultDisplay();
        open.setContentView(popup);
        open.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        open.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams lparam = new WindowManager.LayoutParams();
        lparam.copyFrom(open.getWindow().getAttributes());
        lparam.x = 0;
        lparam.y = 0;
        lparam.gravity = Gravity.FILL;
        open.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        open.show();
        gologin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                open.dismiss();
            }

        });
       /* back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open.dismiss();
            }
        });*/
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (username.getText().toString().length() == 0) {
                    username.setError(getResources().getString(R.string.empty_field));
                } else {
                    if (Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches()) {
                        Log.i("tag", "starting password" + username.getText().toString().trim());
                        PASSWORD password = new PASSWORD();
                        password.execute(username.getText().toString().trim());

                    } else {
                        username.setError(getResources().getString(R.string.invalid_id));
                    }
                }
            }
        });
    }


    private class PASSWORD extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            Log.d("Login", "started");
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... param) {

            logger.info("forget password api " + Appconstatants.forget_pass);

            String response = null;
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                json.put("email", param[0]);
                Log.d("Login_format", json.toString());
                Log.d("session", db.getSession());
                Log.i("tag", "login" + Appconstatants.sessiondata);
                logger.info("forget password api req" + json);
                response = connection.sendHttpPostjson(Appconstatants.forget_pass, json, db.getSession(), Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,Login.this);
                logger.info("forget password api resp" + response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            pDialog.dismiss();
            Log.i("Login", "Login--?" + resp);
            Log.d("login_ss", resp + "");
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Toast.makeText(Login.this, R.string.email_link, Toast.LENGTH_SHORT).show();
                        username.setText("");
                        open.dismiss();
                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(Login.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(popup, R.string.error_msg, Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    PASSWORD password = new PASSWORD();
                                    password.execute(username.getText().toString().trim());

                                }
                            })
                            .show();
                }

            } else {
                Snackbar.make(popup, R.string.error_net, Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                PASSWORD password = new PASSWORD();
                                password.execute(username.getText().toString().trim());

                            }
                        })
                        .show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("erro_124", requestCode + "");
        if (requestCode == 101) {
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // final String token = GoogleAuthUtil.getToken(Login.this, account, "");
                Toast.makeText(Login.this, account.getEmail() + "", Toast.LENGTH_LONG).show();
                Log.d("error_124", "signInResult:failed code=m" + account.getEmail() + "");
                Log.d("error_token", "signInResult:failed code=id" + account.getIdToken() + "");
                Log.d("error_124", "signInResult:failed code=seid" + account.getServerAuthCode() + "");
                //SocialLogin socialLogin = new SocialLogin();
                //socialLogin.execute("google", account.getIdToken(), account.getEmail());


                GETACCES getacces = new GETACCES();
                getacces.execute(account.getServerAuthCode(), account.getEmail());


            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                Log.d("erro_124", "signInResult:failed code=" + e.getStatusCode());
                Toast.makeText(Login.this, "error", Toast.LENGTH_LONG).show();
            }
        }else if(requestCode==1){
            int fromm = data.getExtras().getInt("from");
            if(fromm==2) {
                Intent intent = new Intent(Login.this, Address.class);
                intent.putExtra("from", 2);
                intent.putExtra("has_ship",has_ship);
                startActivity(intent);
                finish();
            }else if(fromm==1){
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else if(fromm==3){
                Intent intent = new Intent(Login.this, MyOrders.class);
                startActivity(intent);
                finish();
            }
            else if (fromm==4)
            {
                finish();
            }else if (fromm==5)
            {
                Intent intent = new Intent(Login.this, MyProfile.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            Log.d("last_name", object.getString("first_name") + "");
                            Log.d("last_name ", object.getString("email") + "-->mail");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("last_name exp", e.toString() + "");
                        }
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");


                            AccessToken token = AccessToken.getCurrentAccessToken();
                            Log.d("Token_1", token.getToken());
                            String facebook_id_token = token.getToken();


                            SocialLogin socialLogin = new SocialLogin();
                            socialLogin.execute("facebook", facebook_id_token, email);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }


    private class SocialLogin extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            Log.d("Login", "started");
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... param) {
            logger.info("Login api" + Appconstatants.login_api);

            String response = null;
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                json.put("provider", param[0]);
                json.put("social_access_token", param[1]);
                json.put("email", param[2]);

                Log.d("Login_format", json.toString());
                Log.d("session", db.getSession());
                Log.d("Login_ses", db.getSession());
                Log.d("Login_ses", Appconstatants.SOCIAL_LOGIN);

                Log.i("tag", "login" + Appconstatants.sessiondata);
                logger.info("Login api req" + json);
                response = connection.sendHttpPostjson(Appconstatants.SOCIAL_LOGIN, json, db.getSession(), Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,Login.this);
                logger.info("Login api resp" + response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            pDialog.dismiss();
            Log.i("Login", "Login--?" + resp);
            Log.d("login_ss", resp + "");
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        JSONObject jsonObject = new JSONObject(json.getString("data"));
                        cus_id = jsonObject.isNull("customer_id") ? "" : jsonObject.getString("customer_id");
                        String cus_grp_id = jsonObject.isNull("customer_group_id") ? "" : jsonObject.getString("customer_group_id");
                        String cus_store = jsonObject.isNull("store_id") ? "" : jsonObject.getString("store_id");
                        String cus_lang = jsonObject.isNull("language_id") ? "" : jsonObject.getString("language_id");
                        String cus_f_name = jsonObject.isNull("firstname") ? "" : jsonObject.getString("firstname");
                        String cus_l_name = jsonObject.isNull("lastname") ? "" : jsonObject.getString("lastname");
                        String cus_email = jsonObject.isNull("email") ? "" : jsonObject.getString("email");
                        String cus_mobile = jsonObject.isNull("telephone") ? "" : jsonObject.getString("telephone");
                        String cus_add_id = jsonObject.isNull("address_id") ? "" : jsonObject.getString("address_id");
                        String cus_wish = jsonObject.isNull("wishlist_total") ? "" : jsonObject.getString("wishlist_total");
                        String cus_msg = jsonObject.isNull("message") ? "" : jsonObject.getString("message");
                        //   int cus_cart_count = jsonObject.getInt("cart_count_products");
                        Toast.makeText(Login.this, "User Logged In", Toast.LENGTH_SHORT).show();
                        db.user_data(cus_id, cus_f_name + " " + cus_l_name, cus_email, cus_mobile);
                        if (from == 2) {
                            Intent intent = new Intent();
                            setResult(3, intent);
                            finish();
                        } else if (from == 1) {
                            finish();
                        } else if (from == 3) {
                            Intent intent = new Intent(Login.this, MyOrders.class);
                            startActivity(intent);
                            finish();
                        } else if (from == 4) {
                            finish();
                        }

                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(Login.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("exp_", e.toString());
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    login.performClick();

                                }
                            })
                            .show();
                }

            } else {
                Snackbar.make(fullayout, R.string.error_net, Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                login.performClick();
                            }
                        })
                        .show();
            }
        }
    }

    private class GETACCES extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;
        String Login_mail="";

        @Override
        protected void onPreExecute() {
            Log.d("Login", "started");
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... param) {
            logger.info("Login api" + Appconstatants.login_api);

            String response = null;
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                json.put("grant_type", "authorization_code");
                json.put("client_id",getResources().getString(R.string.server_client_id));
                json.put("client_secret", "Vbtjumqj20q-Ffre5mLVHPp_");
                json.put("redirect_uri","http://shopzen.stutzen.in");
                json.put("code",param[0]);
                Login_mail=param[1];

                Log.d("Login_format", json.toString());
                Log.d("session", db.getSession());
                Log.d("Login_ses", db.getSession());
                Log.d("Login_ses", Appconstatants.SOCIAL_LOGIN);

                Log.i("tag", "login" + Appconstatants.sessiondata);
                logger.info("Login api req" + json);
                response = connection.sendHttpPostjson("https://accounts.google.com/o/oauth2/token", json,"", Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,Login.this);
                logger.info("Login api resp" + response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            pDialog.dismiss();
            Log.i("Login", "Login--?" + resp);
            Log.d("login_ss", resp + "");
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    Log.d("Tokens_",json.toString()+"");
                    Log.d("Tokens_s",json.getString("access_token"));
                    String access_token=json.getString("access_token");
                    SocialLogin socialLogin=new SocialLogin();
                    socialLogin.execute("google", access_token, Login_mail);

                  /*  if (json.getInt("success") == 1) {
                       *//* JSONObject jsonObject = new JSONObject(json.getString("data"));
                        cus_id = jsonObject.isNull("customer_id") ? "" : jsonObject.getString("customer_id");
                        String cus_grp_id = jsonObject.isNull("customer_group_id") ? "" : jsonObject.getString("customer_group_id");
                        String cus_store = jsonObject.isNull("store_id") ? "" : jsonObject.getString("store_id");
                        String cus_lang = jsonObject.isNull("language_id") ? "" : jsonObject.getString("language_id");
                        String cus_f_name = jsonObject.isNull("firstname") ? "" : jsonObject.getString("firstname");
                        String cus_l_name = jsonObject.isNull("lastname") ? "" : jsonObject.getString("lastname");
                        String cus_email = jsonObject.isNull("email") ? "" : jsonObject.getString("email");
                        String cus_mobile = jsonObject.isNull("telephone") ? "" : jsonObject.getString("telephone");
                        String cus_add_id = jsonObject.isNull("address_id") ? "" : jsonObject.getString("address_id");
                        String cus_wish = jsonObject.isNull("wishlist_total") ? "" : jsonObject.getString("wishlist_total");
                        String cus_msg = jsonObject.isNull("message") ? "" : jsonObject.getString("message");
                        //   int cus_cart_count = jsonObject.getInt("cart_count_products");
                        Toast.makeText(Login.this, "User Logged In", Toast.LENGTH_SHORT).show();
                        db.user_data(cus_id, cus_f_name + " " + cus_l_name, cus_email, cus_mobile);
                        if (from == 2) {
                            Intent intent = new Intent(Login.this, Address.class);
                            intent.putExtra("from", 1);
                            startActivity(intent);
                            finish();
                        } else if (from == 1) {
                            finish();
                        } else if (from == 3) {
                            Intent intent = new Intent(Login.this, MyOrders.class);
                            startActivity(intent);
                            finish();
                        } else if (from == 4) {
                            finish();
                        }*//*

                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(Login.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("exp_", e.toString());
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    login.performClick();

                                }
                            })
                            .show();
                }

            } else {
                Snackbar.make(fullayout, R.string.error_net, Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                login.performClick();
                            }
                        })
                        .show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadyloggedAccount != null) {
            //Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
            googleSignInClient.signOut();
            //onLoggedIn(alreadyloggedAccount);
        } else {
            //Log.d(TAG, "Not logged in");
        }

    }
    public String AccessToken()
    {
        String accessToken = "";
        StringBuilder strBuild = new StringBuilder();

        String authURL = "https://accounts.google.com/o/oauth2/token?";
        String code = "4/SVisuz_x*********************";
        String client_id = "******************e.apps.googleusercontent.com";
        String client_secret = "*******************";
        String redirect_uri = "";
        String grant_type="authorization_code";
        strBuild.append("code=").append(code)
                .append("&client_id=").append(client_id)
                .append("&client_secret=").append(client_secret)
                .append("&redirect_uri=").append(redirect_uri)
                .append("&grant_type=").append(grant_type);
        System.out.println(strBuild.toString());
        try{
            URL obj = new URL(authURL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");

            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Host", "www.googleapis.com");

            //BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            //bw.write(strBuild.toString());
            //bw.close();

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(strBuild.toString());
            wr.flush();
            wr.close();

            //OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            System.out.println(con.getResponseCode());
            System.out.println(con.getResponseMessage());

        } catch (Exception e)
        {
            System.out.println("Error.");
        }
        return "";
    }

}
