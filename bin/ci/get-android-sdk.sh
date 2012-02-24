#!/bin/sh
echo "about to download Android SDK from http://dl.google.com/android/android-sdk_r16-linux.tgz"
wget http://dl.google.com/android/android-sdk_r16-linux.tgz
#ls
echo "unzipping android-sdk_r16-linux.tgz"
tar xvzf android-sdk_r16-linux.tgz
#ls android-sdk-linux
echo "getting some Android platforms"
android-sdk-linux/tools/android update sdk --no-ui -t platform
echo "here is the list of the downloaded platforms : "
ls android-sdk-linux/platforms