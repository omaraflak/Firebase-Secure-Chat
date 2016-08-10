# Firebase-Secure-Chat
This is a simple example of how to use FirebaseAuth within an Android chat!

# Gradle and other settings

In your build.gradle Project file, add:

    classpath 'com.google.gms:google-services:3.0.0'
  
In your build.gradle Module file, add:

    apply plugin: 'com.google.gms.google-services' // top of the file
    
    compile 'com.google.firebase:firebase-database:9.4.0' // dependencies
    compile 'com.google.firebase:firebase-auth:9.4.0' // dependencies
  
Then open up your _SDK Manager_ and install **Google Play Services** and **Google Repository** (both are located under **_Extras_**).

You should have generated a **google-services.json** file with Firebase website, place it at: **_/YourProject/app_**

In your Firebase console, enable **_Email/Password_** authentication in the **Auth** section.

That's it, you should be ready to code!

# 'Features' in the app

    1) Create an account
    2) Automatic login when user already loged in
    3) Login with an existing account
    4) Chat room
    5) Logout
