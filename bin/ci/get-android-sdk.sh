#!/bin/sh
echo "about to download Android SDK from http://dl.google.com/android/android-sdk_r16-linux.tgz"
wget http://dl.google.com/android/android-sdk_r16-linux.tgz
#ls
echo "unzipping android-sdk_r16-linux.tgz"
tar xvzf android-sdk_r16-linux.tgz
#ls android-sdk-linux
echo "getting some Android platforms"
android-sdk-linux/tools/android update sdk --no-ui -t platform
android-sdk-linux/tools/android update sdk --no-ui -t platform-tools
echo "here is the list of the downloaded platforms : "
ls android-sdk-linux/platforms
echo "creating an emulator"
android create avd -n android2.3 -t 9 -c 16M
echo "starting the emulator in background with no window"
emulator-arm -avd android2.3 -no-window
