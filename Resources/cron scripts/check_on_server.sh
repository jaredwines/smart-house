#!/bin/bash
PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/home/pi/tcp/cron_scripts/
#check_on_server.sh
#make sure a process is always running.

process=smartserver
makerun="smartserver"

if ps ax | grep -v grep | grep $process > /dev/null
then
    exit
else
    $makerun &
fi

exit