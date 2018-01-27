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

## Enable ADB TCP

```
adb shell 'su -c "setprop service.adb.tcp.port 5555 && stop adbd && start adbd"'
adb tcpip 5555
adb connect 192.168.0.xx:5555
```

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

su -c "pm install -r /data/data/com.termux/files/home/termux-api/bin/termux-api.apk"
```

## mount to local folder
```
sshfs pi@192.168.0.8:/data/data/com.termux/files/home/ -p 8022 /home/master/SamsungS4 -o allow_other,IdentityFile=/home/master/.ssh/id_rsa 
```

## USB TETHERING 
```
adb shell 'su -c "service call connectivity 33 i32 1"'

adb shell 'su -c "ifconfig usb0 192.168.0.7"'

adb shell 'su -c "iptables -F"'
adb shell 'su -c "iptables -X"'
adb shell 'su -c "iptables -t nat -F"'
adb shell 'su -c "iptables -t nat -X"'
adb shell 'su -c "iptables -t mangle -F"'
adb shell 'su -c "iptables -t mangle -X"'
adb shell 'su -c "iptables -P INPUT ACCEPT"'
adb shell 'su -c "iptables -P FORWARD ACCEPT"'
adb shell 'su -c "iptables -P OUTPUT ACCEPT"'

#adb shell 'su -c "iptables-restore < /data/local/iptables-rules"'

```
