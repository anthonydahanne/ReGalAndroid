ReGalAndroid
=============

Formerly named [g2android](http://code.google.com/p/g2android/), ReGalAndroid is a [Menalto Gallery 2](http://gallery.menalto.com/) client; but aimed to be also compatible with [G3](https://github.com/gallery/gallery3), [Piwigo](http://piwigo.org/) , etc...

There are 6 modules present :

- g2-java-client : the gallery2 (G2) compatibility layer, providing a lightweight Java API (and its unit tests) to communicate with the gallery2 remote interface [described here](http://codex.gallery2.org/Gallery_Remote:Protocol)
- g3-java-client : the gallery3 (G3) compatibility layer, providing a lightweight Java API (and its unit tests) to communicate with the G3 remote interface [described here](http://codex.gallery2.org/Gallery3:API:REST)
- commons-gallery : a project providing common (!) API elements, for all gallery implementations ; that is, for now, mainly an Album and a Picture class
- jiwigo-ws-api : the [Piwigo](http://piwigo.org/) compatibility layer, providing a lightweight Java API (and its unit tests) to communicate with the Piwigo remote interface [this module has been developed by Gael Le Guevel](http://www.le-guevel.com/jiwigo/)
- regalandroid : this is the android application; compatible with all Android enabled phones and tablets starting from 1.5
- regalandroid-parent : this is the parent of all the project; build this one and you'll build all the projects at once.

How to compile/build
----------------------

You can whether use Linux, Windows or MacOsX to build any of regalandroid sub-projects. (for info, I'm using Linux Ubuntu 10.10)

You will need the following components installed on your machine :

- Sun (Oracle) JDK 5.0 (though I'm using JDK 6.0)
- [Maven 2](http://maven.apache.org/) (though I'm using Maven 3)
- [Android SDK](http://developer.android.com/sdk/index.html)
- an IDE with proper Android tooling (I'm using Eclipse 3.6.1)
- a git client 

Make sure your environment is ok (ie the environment variables : ANDROID_HOME, JAVA_HOME and M2_HOME should be set properly and also added to the path, for example, add $ANDROID_HOME/tools , $JAVA_HOME/bin and $M2_HOME/bin to your $PATH)

Create a new working directory (it will become your workspace)
	mkdir workspace && cd workspace
Checkout the whole project using :
	git clone git@github.com:anthonydahanne/ReGalAndroid.git ./
Go to the parent project
	cd regalandroid-parent
Compile everything
	mvn clean install
The APK should be in regalandroid/target; you can send it to your phone.


How to deploy (to an emulator/phone/any android 1.5+ device)
------------------------------------------------------------

`Quickly deploy to your device`

You can grab the compiled regalandroid APK (or build it using maven, it will be in regalandroid/target) and then copy it to your device sdcard; then browse this file on your device (using Astro for example) and install it (you need your device to be in dev mode : Menu->Settings->Applications->check Unknown sources and enable Development options)

`Deploy to an emulator using maven`

Make sure you have an Android emulator (AVD) created (you could have created it using eclipse or ADT), and note its ID; this is because in the project pom file it is assumed that you have an AVD named android2.2.
If it is not the case, replace this with the id of your android AVD in regalandroid/pom.xml (for more info, have a look at the maven android site : http://code.google.com/p/maven-android-plugin/wiki/Samples)

then, :

	cd regalandroid
	mvn clean package android:deploy

The latest version should now be deployed to your AVD.

License
-------

ReGalAndroid is distributed under [GNU General Public License v3](http://www.gnu.org/licenses/gpl-3.0.html).

Want to participate?
--------------------

You can fork this project and contact me ! (anthony.dahanne AT gmail DOT com)


