#!/bin/bash
PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/home/pi/tcp/cron_scripts/
#check_on_ddclient.sh
#make sure a process is always running.

process=ddclient
makerun="sudo ddclient"

if ps ax | grep -v grep | grep $process > /dev/null
then
    exit
else
    $makerun &
fi

$sudo ddclient -query

exit