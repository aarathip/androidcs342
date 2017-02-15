<h2> Instructions for LocationDemo app</h2>
The LocationDemo app shows how to capture current location on the smartphone/emulator.

<!--
If running on the emulator, make sure to follow the instructions in the next section.
Start the emulator. See if it is running by typing the following command on your terminal

adb shell

Then to install it in the emulator, type the following command
adb install /Users/aprasad/Downloads/MockWalker.apk

You cannot reinstall the same app; to do so, you need to uninstall it first.
If you need to uninstall it, first you need to figure out the package name.
So you need access to the shell.

**adb shell**

Then type the following command to see the list of apps installed in the emulator.

**pm list packages -f**

Find MockWalker and copy the package name. The package name value will be the value following the text **apk=** for the MockWalker app.

Then exit the emulator shell, and once you are back in your laptop shell, type the following command to uninstall.

**adb uninstall <packagename> -->

<h3>  Play Services </h3>
To get your location using the Fused Location Provider, you need to use Google Play Services. To get those up and running, you will need to add a few standard bits of boilerplate to your app. First, you need to add the Google Play Services library dependency. The services themselves live in the Play app, but the Play Services library contains all the code to interface with them. Open up your app module’s settings (File → Project Structure). Navigate to its dependencies, and add a library dependency. Type in the following dependency name:
 **com.google.android.gms:play-services-location:7.3.0.**

 Add play services check in the onCreate(), onResume() of the activity. Add appropriate permissions in AndroidManifest.xml

Next we need to create a client to access the Play Services. To create a client, create a **GoogleApiClient.Builder** and configure it. At a minimum, you want to configure the instance with the specific APIs you will be using. Then call **build()** to create an instance.

Connection state information is passed through two callback interfaces: ConnectionCallbacks and OnConnectionFailedListener. You can implement either if required.

<h3> Requesting location </h3>
We will request for location in the onClick of the "Where am I" button. First we need to create a LocationRequest.

LocationRequest objects configure a variety of parameters for your request:
<li> interval – how frequently the location should be updated
<li> number of updates – how many times the location should be updated
<li> priority – how Android should prioritize battery life against accuracy to satisfy your request
expiration – whether the request should expire and, if so, when
<li> smallest displacement – the smallest amount the device must move (in meters) to trigger a location update

When you first create a LocationRequest, it will be configured for accuracy within a city block, with repeated slow updates until the end of time. In your code, you change this to get a single, high-accuracy location fix by changing the priority and the number of updates. You also set the interval to 0, to signify that you would like a location fix as soon as possible.
The next step is to send off this request and listen for the Locations that come back. You do this by adding a LocationListener. There are two versions of LocationListener you can import. Choose **com.google.android.gms.location.LocationListener**.

If this were a longer-lived request, you would need to hold on to your listener and call **removeLocationUpdates(…)** later to cancel the request. However, since you called **setNumUpdates(1)**, all you need to do is send this off and forget about it.

Run and click the "Where am I" button. Every time you move, the **onLocationChanged** method is called.

<h3> Using mock location in emulator </h3>
Find a location using :link http://www.latlong.net/convert-address-to-lat-long.html

Next open the Extended Controls on the emulator by pressing the ... option in the menu. Select Location and type in latitude and longitude and press Send. Then press the "Where am I" button; you should see that the **onLocationChanged** method was called. 
