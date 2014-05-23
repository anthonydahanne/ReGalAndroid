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
android-sdk-linux/tools/android update sdk --no-ui --obsolete --force
echo "here is the list of the downloaded platforms : "
ls android-sdk-linux/platforms
echo "creating an emulator"
#the echo no | before the command is to answer no to the question "do you wish to create a custom hardware profile"
#see http://stackoverflow.com/questions/7931997/setting-android-avd-command-line-hardware-profile
echo no | android-sdk-linux/tools/android create avd -n android2.3.3 -t android-10 -c 16M -b armeabi
echo "starting the emulator in background with no window"
android-sdk-linux/tools/emulator-arm -avd android2.3.3 -no-window &
