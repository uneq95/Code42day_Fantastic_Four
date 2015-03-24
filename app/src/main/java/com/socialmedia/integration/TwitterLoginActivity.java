package com.socialmedia.integration;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class TwitterLoginActivity extends ActionBarActivity {


    WebView webView;
    private CommonsHttpOAuthConsumer httpOauthConsumer;
    private OAuthProvider httpOauthprovider;
    public final static  String consumerKey="nWJ0hHyrAHVrIxLH8rdMdFE5K";
    public final static  String consumerSecret="pSe10229p5sjlTlsy2lsyKqbJR1k763ZV9OdsqBjBm1urr1N9n";
    //public final static String AccessToken="3103374554-RcBNMFe1YEo4nNDPOMBztwa5i9dvY7GO40sqe70";
    public final static String AccessTokenSecret="6ZMNOuy64rK9P8rjxUokHfltUPWoJV6d9Moahlt3mPXal";
//    public final static  String consumerKey="i5j21IzIx7RFPjjjm8Mp3TX4O";
//    public final static  String consumerSecret="yUoWvdOhKqF0x6BhGUly4o1EtkzSLIluf0Rii0V6feu9YldiFm";

    public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
    public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
    public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";


    private final String CALLBACKURL = "app://twitter";
    //private final String CALLBACKURL = "https://127.0.0.1";
    static final String IEXTRA_OAUTH_VERIFIER = "oauth_verifier";
    ProgressDialog  progressDialog;
    String authUrl;
    Twitter twitter;
    private RequestToken requestToken;
    String x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_login);
        webView=(WebView)findViewById(R.id.webView);
        webView.requestFocus(View.FOCUS_DOWN);

        //progressDialog=ProgressDialog.show(this,"",this.getString(R.string.loading),true);
        webView.setWebViewClient(new WebViewClient(){


            @Override
            public void onPageFinished(WebView view, String url) {
                if(progressDialog!=null&&progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }



            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url!=null){
                    LoadPageAsyncTask newLogin=new LoadPageAsyncTask(url);
                    newLogin.execute();

//                    try{
//                        Uri uri = Uri.parse(url);
//                        System.out.println(uri.toString());
////                        String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
//                        String verifier = uri.getQueryParameter("oauth_verifier");
//                        //String verifier = uri.getQueryParameter(IEXTRA_OAUTH_VERIFIER);
//                        System.out.println("verifier: "+verifier);
//                        AccessToken accessToken1 = twitter.getOAuthAccessToken(requestToken, x);
//                        Toast.makeText(TwitterLoginActivity.this,"call back coming from "+url.toString(),Toast.LENGTH_LONG).show();
//                        String userKey = accessToken1.getToken();
//                        String userSecret = accessToken1.getTokenSecret();
//                        SharedPreferences settings = getBaseContext().getSharedPreferences("your_app_prefs", 0);
//                        SharedPreferences.Editor editor = settings.edit();
//                        Toast.makeText(getApplicationContext(),"userKey: "+userKey,Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getApplicationContext(),"userSecret: "+userSecret,Toast.LENGTH_SHORT).show();
//                        System.out.println("token : " + userKey);
//                        editor.putString("user_key", userKey);
//                        editor.putString("user_secret", userSecret);
//                        editor.commit();
//                        System.out.println(userKey);
//                        System.out.println(userSecret);
//                    }catch(Exception e){
//                        e.printStackTrace();
//                    }
                }
//                if (url != null && url.toString().startsWith(CALLBACKURL)) {
//
//                    Uri uri = Uri.parse(url);
//                    System.out.println(uri.toString());
//                    String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
//
//                    try {
//                        // this will populate token and token_secret in consumer
//                        System.out.println("entering");
//                        httpOauthprovider.retrieveAccessToken(httpOauthConsumer, verifier);
//                        System.out.println("entered");
//                        String userKey = httpOauthConsumer.getToken();
//                        String userSecret = httpOauthConsumer.getTokenSecret();
//
//                        // Save user_key and user_secret in user preferences and return
//
//                        SharedPreferences settings = getBaseContext().getSharedPreferences("your_app_prefs", 0);
//                        SharedPreferences.Editor editor = settings.edit();
//                        Toast.makeText(getApplicationContext(),"userKey: "+userKey,Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getApplicationContext(),"userSecret: "+userSecret,Toast.LENGTH_SHORT).show();
//                        System.out.println("token : " + userKey);
//                        editor.putString("user_key", userKey);
//                        editor.putString("user_secret", userSecret);
//                        editor.commit();
//                        System.out.println(userKey);
//                        System.out.println(userSecret);
//
//                    } catch (Exception e) {
//
//                    }
//                } else {
//                    // Do something if the callback comes from elsewhere
//                    try{
//                        Uri uri = Uri.parse(url);
//                        System.out.println(uri.toString());
//                        //String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
//                        String verifier = uri.getQueryParameter(IEXTRA_OAUTH_VERIFIER);
//                        System.out.println("verifier: "+verifier);
//                        AccessToken accessToken1 = twitter.getOAuthAccessToken(requestToken, verifier);
//
//                        Toast.makeText(TwitterLoginActivity.this,"call back coming from "+url.toString(),Toast.LENGTH_LONG).show();
//                        //httpOauthprovider.retrieveAccessToken(httpOauthConsumer, verifier);
//                        System.out.println("entered");
//                        String userKey = accessToken1.getToken();
//                        String userSecret = accessToken1.getTokenSecret();
//
////                        String userKey = httpOauthConsumer.getToken();
////                        String userSecret = httpOauthConsumer.getTokenSecret();
//
//                        // Save user_key and user_secret in user preferences and return
//
//                        SharedPreferences settings = getBaseContext().getSharedPreferences("your_app_prefs", 0);
//                        SharedPreferences.Editor editor = settings.edit();
//                        Toast.makeText(getApplicationContext(),"userKey: "+userKey,Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getApplicationContext(),"userSecret: "+userSecret,Toast.LENGTH_SHORT).show();
//                        System.out.println("token : " + userKey);
//                        editor.putString("user_key", userKey);
//                        editor.putString("user_secret", userSecret);
//                        editor.commit();
//                        System.out.println(userKey);
//                        System.out.println(userSecret);
//                    }catch(Exception e){
//                        e.printStackTrace();
//                    }
//                }
                return true;
            }

        });

    }

    public void tweet(View v)
    {
      //startOauth();
       // webView.loadUrl(authUrl);
      new  PostRequestAsyncTask().execute();
    }

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



    private class PostRequestAsyncTask extends AsyncTask<String, Void, Boolean> {


        String authorisationUrl;
        @Override
        protected void onPreExecute(){
            progressDialog= ProgressDialog.show(TwitterLoginActivity.this, "", TwitterLoginActivity.this.getString(R.string.loading), true);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
//            try {
//                System.out.println("go");
//                httpOauthConsumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
//                httpOauthprovider = new DefaultOAuthProvider(REQUEST_URL,ACCESS_URL,AUTHORIZE_URL);
//                authUrl = httpOauthprovider.retrieveRequestToken(httpOauthConsumer, CALLBACKURL);
//                System.out.println("so");
//
//                // Open the browser
//               // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
//                System.out.println("yo");
//            } catch (Exception e) {
//               //Toast.makeText(TwitterLoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                System.out.println(e.getMessage());
//                System.out.println("Error communicating");
//            }
            ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
            configurationBuilder.setOAuthConsumerKey(consumerKey);
            configurationBuilder.setOAuthConsumerSecret(consumerSecret);
            Configuration configuration = configurationBuilder.build();
            twitter = new TwitterFactory(configuration).getInstance();
            try {
                requestToken = twitter.getOAuthRequestToken(CALLBACKURL);
                System.out.println("request tocke:" +requestToken);
                x=requestToken.getToken();
                System.out.println(x);
                authUrl=Uri.parse(requestToken.getAuthenticationURL()).toString();
                System.out.println("authurl: "+authUrl);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean status){
            if(progressDialog!=null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            System.out.println("loading page in wbview");
            webView.loadUrl(authUrl);
        }

    };

    private class LoadPageAsyncTask extends AsyncTask<String, Void, Boolean> {


        String url;
        LoadPageAsyncTask(String url){
            this.url=url;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(TwitterLoginActivity.this, "", TwitterLoginActivity.this.getString(R.string.loading), true);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try{
                Uri uri = Uri.parse(url);
                System.out.println(uri.toString());
//                        String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
                String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
                //String verifier = uri.getQueryParameter(IEXTRA_OAUTH_VERIFIER);

                System.out.println("verifier: "+verifier);
                AccessToken accessToken1 = twitter.getOAuthAccessToken(requestToken, verifier);
                Toast.makeText(TwitterLoginActivity.this,"call back coming from "+url.toString(),Toast.LENGTH_LONG).show();
                String userKey = accessToken1.getToken();
                String userSecret = accessToken1.getTokenSecret();
                SharedPreferences settings = getBaseContext().getSharedPreferences("your_app_prefs", 0);
                SharedPreferences.Editor editor = settings.edit();
                Toast.makeText(getApplicationContext(),"userKey: "+userKey,Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"userSecret: "+userSecret,Toast.LENGTH_SHORT).show();
                System.out.println("token : " + userKey);
                editor.putString("user_key", userKey);
                editor.putString("user_secret", userSecret);
                editor.commit();
                System.out.println(userKey);
                System.out.println(userSecret);
            }catch(Exception e){
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean status) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            System.out.println("loading page in wbview");


        }

    };



}
