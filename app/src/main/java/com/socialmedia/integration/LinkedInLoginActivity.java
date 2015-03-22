package com.socialmedia.integration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;


public class LinkedInLoginActivity extends ActionBarActivity {

    // consumer or api key
    private static final String API_KEY="781djxt8zrkj0k";
    //consumer secret or secret key
    private static final String SECRET_KEY="6LzRurMQLjYjmEVj";
    //OAuth 1.0a User Token
    private static final String OAuthUserToken="a2aa6940-beae-4266-b096-042fa7c10c53";
    //OAuth 1.0a User Secret
    private static final String OAuthUserSecret="c2c8c507-4b9f-4ec0-8fed-1bd535840ee8";

    private static final String STATE="YKC1T6H2yP4zE3Z";
    private static final String REDIRECT_URI="https://127.0.0.1";
    private static final String SCOPES = "r_fullprofile%20r_emailaddress%20r_network%20w_messages%20rw_company_admin%20w_share%20rw_nus%20r_basicprofile%20rw_groups%20r_network%20r_contactinfo";

    private static final String AUTHORIZATION_URL = "https://www.linkedin.com/uas/oauth2/authorization";
    private static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken";
    private static final String SECRET_KEY_PARAM = "client_secret";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String RESPONSE_TYPE_VALUE ="code";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String STATE_PARAM = "state";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";
    private static final String SCOPE_PARAM = "scope";

    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";


    WebView webView;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linked_in_login);
        webView=(WebView)findViewById(R.id.webView);
        webView.requestFocus(View.FOCUS_DOWN);
        progressDialog = ProgressDialog.show(this, "", this.getString(R.string.loading), true);

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl) {
                if(authorizationUrl.startsWith(REDIRECT_URI)){
                    Uri uri = Uri.parse(authorizationUrl);
                    //We take from the url the authorizationToken and the state token. We have to check that the state token returned by the Service is the same we sent.
                    //If not, that means the request may be a result of CSRF and must be rejected.
                    String stateToken = uri.getQueryParameter(STATE_PARAM);
                    if(stateToken==null || !stateToken.equals(STATE)){
                        return true;
                    }

                    //If the user doesn't allow authorization to our application, the authorizationToken Will be null.
                    String authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE);
                    if(authorizationToken==null){
                        return true;
                    }

                    //Generate URL for requesting Access Token
                    String accessTokenUrl = getAccessTokenUrl(authorizationToken);
                    //We make the request in a AsyncTask
                    new PostRequestAsyncTask().execute(accessTokenUrl);

                }else{
                    //Default behaviour
                    webView.loadUrl(authorizationUrl);
                }
                return true;

            }
        });
        String authUrl = getAuthorizationUrl();
        webView.loadUrl(authUrl);
    }


    private static String getAccessTokenUrl(String authorizationToken){
        return ACCESS_TOKEN_URL
                +QUESTION_MARK
                +GRANT_TYPE_PARAM+EQUALS+GRANT_TYPE
                +AMPERSAND
                +RESPONSE_TYPE_VALUE+EQUALS+authorizationToken
                +AMPERSAND
                +CLIENT_ID_PARAM+EQUALS+API_KEY
                +AMPERSAND
                +REDIRECT_URI_PARAM+EQUALS+REDIRECT_URI
                +AMPERSAND
                +SECRET_KEY_PARAM+EQUALS+SECRET_KEY;
    }
    /**
     * Method that generates the url for get the authorization token from the Service
     * @return Url
     */
    private static String getAuthorizationUrl(){
        return AUTHORIZATION_URL
                +QUESTION_MARK+RESPONSE_TYPE_PARAM+EQUALS+RESPONSE_TYPE_VALUE
                +AMPERSAND+CLIENT_ID_PARAM+EQUALS+API_KEY
                +AMPERSAND  +SCOPE_PARAM +EQUALS +SCOPES
                +AMPERSAND+STATE_PARAM+EQUALS+STATE
                +AMPERSAND+REDIRECT_URI_PARAM+EQUALS+REDIRECT_URI;
    }



    private class PostRequestAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute(){
            progressDialog= ProgressDialog.show(LinkedInLoginActivity.this, "", LinkedInLoginActivity.this.getString(R.string.loading),true);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            if(urls.length>0){
                String url = urls[0];
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpost = new HttpPost(url);
                try{
                    HttpResponse response = httpClient.execute(httpost);
                    if(response!=null){
                        //If status is OK 200
                        if(response.getStatusLine().getStatusCode()==200){
                            String result = EntityUtils.toString(response.getEntity());
                            //Convert the string result to a JSON Object
                            JSONObject resultJson = new JSONObject(result);
                            //Extract data from JSON Response
                            int expiresIn = resultJson.has("expires_in") ? resultJson.getInt("expires_in") : 0;
                            System.out.println("result :"+resultJson);
                            String accessToken = resultJson.has("access_token") ? resultJson.getString("access_token") : null;
                            Log.e("Tokenm", ""+accessToken);
                            if(expiresIn>0 && accessToken!=null){
                                Log.i("Authorize", "This is the access Token: " + accessToken + ". It will expires in " + expiresIn + " secs");

                                //Calculate date of expiration
                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.SECOND, expiresIn);
                                long expireDate = calendar.getTimeInMillis();

                                ////Store both expires in and access token in shared preferences
                                SharedPreferences preferences = LinkedInLoginActivity.this.getSharedPreferences("user_info", 0);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putLong("expires", expireDate);
                                editor.putString("accessToken", accessToken);
                                editor.commit();

                                return true;
                            }
                        }
                    }
                }catch(IOException e){
                    Log.e("Authorize","Error Http response "+e.getLocalizedMessage());
                }
                catch (ParseException e) {
                    Log.e("Authorize","Error Parsing Http response "+e.getLocalizedMessage());
                } catch (JSONException e) {
                    Log.e("Authorize","Error Parsing Http response "+e.getLocalizedMessage());
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean status){
            if(progressDialog!=null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            if(status){
                //If everything went Ok, change to another activity.
                Intent startProfileActivity = new Intent(LinkedInLoginActivity.this, LinkedInProfileVew.class);
                LinkedInLoginActivity.this.startActivity(startProfileActivity);
            }
        }

    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
