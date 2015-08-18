ReGalAndroid  [![Build Status](https://travis-ci.org/anthonydahanne/ReGalAndroid.svg?branch=master)](https://travis-ci.org/anthonydahanne/ReGalAndroid)
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

The project recently (August 2015) switched to Android Studio 1.3
You can whether use Linux, Windows or MacOsX to build any of regalandroid sub-projects.

You will need the following components installed on your machine :

- Sun (Oracle) JDK 5.0 (though I'm using JDK 8.0)
- Android Studio (I'm using Android Studio 1.3)
- a git client

Make sure your environment is ok (ie the environment variables : JAVA_HOME should be set properly and also added to the path, for example, add $JAVA_HOME/bin to your $PATH)

Create a new working directory (it will become your workspace)
	mkdir workspace && cd workspace
Checkout the whole project using :
	git clone git@github.com:anthonydahanne/ReGalAndroid.git ./
Build and start the app :
	$ ./gradlew -x test installDebug
The APK should be in regalandroid/build/outputs/apk/regalandroid-debug.apk


How to import in Android Studio
-------------------------------

Well, just import the project, build it and deploy !


How to deploy (to an emulator/phone/any android 1.5+ device)
------------------------------------------------------------

`Quickly deploy to your device`

If you use an emulator, make sure to provision at least 400MB for the external storage, or else (for a reason I don't yet know) the virtual device will refuse to write to the external storage.

You can grab the compiled regalandroid APK (or build it using gradle, it will be in regalandroid/build/outputs/apk/regalandroid-debug.apk) and then copy it to your device sdcard; then browse this file on your device (using Astro for example) and install it (you need your device to be in dev mode : Menu->Settings->Applications->check Unknown sources and enable Development options)


License
-------

ReGalAndroid is distributed under [GNU General Public License v3](http://www.gnu.org/licenses/gpl-3.0.html).

Want to participate?
--------------------

You can fork this project and contact me ! (anthony.dahanne AT gmail DOT com)


