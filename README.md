# FacebookFirebase-Demo
Android Facebook Login with Firebase

## Step 1 - Connect App with Firebase
1. In Android Studio, go to `Tools` -> `Firebase`.
2. Go to `Authentication` -> `Email and password authentication`. 
3. Connect app to Firebase by clicking `Connect to Firebase` button and once dialog opens, click on the blue button below.
4. Once app is connected, add Firebase Authentication to the app.

## Step 2 - Enable Facebook Login
1. Go to [Facebook for Developers](https://developers.facebook.com/).
2. On top right hand corner, go to `Create App` button and enter the App name as the display name and your own contact email.
3. Go to Facebook console page and go to `Dashboard` to retrieve `App ID` and `App Secret`.
4. Go to Firebase console page and go to `Authentication` -> `Sign-in Method` and enable Facebook login. Enter the `App ID` and `App Secret` retrieved from Facebook console page. Copy the OAuth redirect URI as well.
5. Go back to Facebook console page and click on `Add Product`-> under Facebook, click `Setup`. Paste the OAuth redirect URI under the textbox for `Valid OAuth redirect URIs`.

## Step 3 - Android Code for Facebook
1. Go to [Facebook Login](https://developers.facebook.com/docs/facebook-login/android).
2. Select your app in step 1.
3. Link the Facebook SDK by selecting `Maven`. Add the following code in `build.gradle(Project)`
```
buildscript {
    
    repositories {
        maven { url 'https://maven.google.com' }
        mavenCentral()
        jcenter()
    }
    dependencies {
        // Eliminated
    }
}
```
4. Add the following dependencies in `build.gradle(Module)`
```
implementation 'com.facebook.android:facebook-login:[4,5)'
```
5. Add the following in `strings.xml`:
```
<string name="facebook_app_id">201486427066708</string>
<string name="fb_login_protocol_scheme">fb201486427066708</string>    
```
6. In your Manifest, add the following permission:
```
<uses-permission android:name="android.permission.INTERNET" />
```
Also add the following in your application body:
```
<meta-data
    android:name="com.facebook.sdk.ApplicationId"
    android:value="@string/facebook_app_id" />
    
<activity
    android:name="com.facebook.FacebookActivity"
    android:configChanges="keyboard|keyboardHidden|screenLayout|screenSize|orientation"
    android:label="@string/app_name" />
<activity
    android:name="com.facebook.CustomTabActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />

        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />

        <data android:scheme="@string/fb_login_protocol_scheme" />
    </intent-filter>
</activity>
```
