# Android-Termux

* https://plus.google.com/+AldrinHolmes20/posts/8nn8YAn8reM
* https://wiki.lineageos.org/devices/mako/install

fastboot flash recovery twrp-3.2.1-0-mako.img


## Enable ADB from Recovery

```
mount /dev/block/bootdevice/by-name/system /system  
#or
twrp mount system
twrp mount data
```
Add to /default.prop and /system/build.prop

```
persist.service.adb.enable=1                                                    
persist.service.debuggable=1
persist.sys.usb.config=mtp,adb
```
Copy ~/.android/adbkey.pub to /data/misc/adb/adb_keys

## WIFI best Ping

```
adb shell
mount -o rw,remount /system
vi /system/etc/wifi/WCNSS_qcom_cfg.ini  (i -> gEnableBmps=0 ESC+:wq)
```

## Root SH

```
su -c bash
```
## Charging

```
echo 0 > /sys/class/power_supply/usb/device/charge
```

## Sensors
* http://papermint-designs.com/community/node/420

## Compile APK in Termux
```
pkg i ecj dx aapt apksigner
```
uncomment in ~/../usr/bin/dalvikvm
```
#export ANDROID_DATA=/data/data/com.termux/files/usr/var/android/
#mkdir -p $ANDROID_DATA/dalvik-cache
```

Build commands for termux-api
```
#git clone https://github.com/termux/termux-api.git
#copy to termux-api-folder

if [ ! -d "./bin" ]; then
  # Control will enter here if $DIRECTORY doesn't exist.
mkdir ./bin
fi

aapt package -v -f -M ./app/src/main/AndroidManifest.xml -I $PREFIX/share/java/android.jar -J ./app/src/main/java/ -S ./app/src/main/res -m
ecj -d ./obj -classpath $HOME/../usr/share/java/android.jar -sourcepath ./app/src $(find ./app/src -type f -name "*.java")
dx --dex --verbose --output=./bin/classes.dex ./obj

aapt package -v -f -M ./app/src/main/AndroidManifest.xml -S ./app/src/main/res -F ./bin/tmp.apk
cd bin
aapt add -f tmp.apk classes.dex
apksigner ~/storage/shared/my.key tmp.apk termux-api.apk -p 123456
chmod 444 termux-api.apk
```
