Android-Chat
------------

This is Android Chat Application project where one user can connect to other users from a given list of users provided that the users are already available. As the new user joins the chat application, the new user is available to other users who all have already joined the application. Therefore the users can send and receive messages and can also delete their own user account.

##Due to certain errors on manifest and gradle dependencies the project haas been converted to AndroidX.##

The link for this android application : https://play.google.com/store/apps/details?id=com.sandeep.androidchat&hl=en

Gradle
------
``` dependecies used
dependencies{
    implementation 'com.google.firebase:firebase-database:16.1.0'
}
```


For Desiging purpose third party dependencies used :

```
    dependencies{
    implementation 'de.hdodenhof:circleimageview:3.0.0'
    implementation 'com.xwray:groupie:2.3.0'
    implementation 'com.squareup.picasso:picasso:2.71828'   
   }
   ```
   
   Limitations
   -----------
   
   * The user is said to be anonymous, if and only if the user has not uploaded its profile image and due to this profile image has been set to mandatory.
   * The user interface for egistration and Login is not appropriate to its constraint whose API level is less than 23.
   * Once the user deletes its account the last data is still existed which will be modified on later ver.
   
   Important Links
   ---------------
   The link for this android application : https://play.google.com/store/apps/details?id=com.sandeep.androidchat&hl=en
   
   Links for the other third parties dependencies:
   * https://github.com/square/picasso
   * https://github.com/sandeepmaxpayne/CircleImageView
   * https://github.com/sandeepduttacse/groupie
   
      
   
   
