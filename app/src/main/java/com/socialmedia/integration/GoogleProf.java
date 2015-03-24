package com.socialmedia.integration;

/**
 * Created by nairit on 24/3/15.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by ritesh_kumar on 22-Mar-15.
 */
public class GoogleProf extends Activity {

    private static final String PROFILE_URL = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=";
    //private static final String PROFILE_URL = "https://api.linkedin.com/v1/people-search:(people:(id,first-name,last-name,headline,picture-url,industry,positions:(id,title,summary,start-date,end-date,is-current,company:(id,name,type,size,industry,ticker)),educations:(id,school-name,field-of-study,start-date,end-date,degree,activities,notes)),num-results)?first-name=parameter&last-name=parameter";
    private static final String OAUTH_ACCESS_TOKEN_PARAM ="oauth2_access_token";
    private static final String QUESTION_MARK = "?";
    private static final String EQUALS = "=";

    private TextView welcomeText;
    private ProgressDialog pd;

    TextView name,family,id,givenname,localname;
    ImageView pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_login);
        //welcomeText = (TextView) findViewById(R.id.activity_profile_welcome_text);
        name=(TextView)findViewById(R.id.tvgooglename);
        family=(TextView)findViewById(R.id.tvgooglefamilyname);
        id=(TextView)findViewById(R.id.tvgoogleid);
        givenname=(TextView)findViewById(R.id.tvgooglegivenname);
        localname=(TextView)findViewById(R.id.tvgooglelocale);
        pic= (ImageView) findViewById(R.id.tvgoogleimage);

        //Request basic profile of the user
        SharedPreferences preferences = this.getSharedPreferences("user_info", 0);
        String accessToken = preferences.getString("accessToken", null);
        if(accessToken!=null){
            String profileUrl = getProfileUrl(accessToken);
            new GetProfileRequestAsyncTask().execute(profileUrl);
        }
    }

    private static final String getProfileUrl(String accessToken){
        return PROFILE_URL
                +accessToken;
    }

    private class GetProfileRequestAsyncTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute(){
            pd = ProgressDialog.show(GoogleProf.this, "", "Loading..", true);
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            if(urls.length>0){
                String url = urls[0];
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(url);
                httpget.setHeader("x-li-format", "json");
                try{
                    HttpResponse response = httpClient.execute(httpget);
                    if(response!=null){
                        //If status is OK 200
                        if(response.getStatusLine().getStatusCode()==200){
                            String result = EntityUtils.toString(response.getEntity());
                            System.out.println("json response "+result);
                            //Convert the string result to a JSON Object
                            return new JSONObject(result);
                        }
                    }
                }catch(IOException e){
                    Log.e("Authorize","Error Http response "+e.getLocalizedMessage());
                } catch (JSONException e) {
                    Log.e("Authorize","Error Http response "+e.getLocalizedMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject data){
            if(pd!=null && pd.isShowing()){
                pd.dismiss();
            }
            if(data!=null){

                try {
                    System.out.println("jsonm "+data);
//                    String welcomeTextString = String.format("Welcome %1$s %2$s, You are a %3$s",data.getString("firstName"),data.getString("lastName"),data.getString("headline"));
//                    welcomeText.setText(welcomeTextString);
                    id.setText(data.getString("id")+"");
                    name.setText(data.getString("name")+"");
                    givenname.setText(data.getString("given_name")+"");
                    //tvProfileLink.setText(data.getJSONObject("siteStandardProfileRequest").getString("url")+"");
                    family.setText(data.getString("family_name")+"");
                    localname.setText(data.getString("locale")+"");


                } catch (JSONException e) {
                    Log.e("Authorize", "Error Parsing json " + e.getLocalizedMessage());
                }
            }
        }


    };

}
