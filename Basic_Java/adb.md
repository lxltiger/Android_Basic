//查看网络信息
root@android:/ # netcfg
//查看eth0
ifconfig eth0
//查看dns
getprop net.eth0.dns1

设置ip
ifconfig eth0 192.168.0.173 netmask 255.255.255.0

设置网管
route add default gw 192.168.0.1 dev eth0
添加dns
setprop net.eth0.dns1 8.8.8.8

//产看eth0配置
getprop | grep eth0