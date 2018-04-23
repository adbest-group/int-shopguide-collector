#!/bin/bash

PRGDIR=`dirname $0`
SERVER_HOME=$(echo `readlink -f $PRGDIR` | sed 's/\/bin//')
JAVA_HOME="/opt/dsp_software/jdk1.8.0_111"

export LANG=zh_CN
export LC_ALL=zh_CN.UTF-8
export JAVA_HOME=$JAVA_HOME

CLASSPATH=`echo $JAVA_HOME/lib/*.jar | tr ' ' ':'`
CLASSPATH=$CLASSPATH:`echo $SERVER_HOME/target/lib/compile/*.jar | tr ' ' ':'`
CLASSPATH=$CLASSPATH:$SERVER_HOME/conf
if [ -d "$SERVER_HOME/target/src/classes" ] ; then
	CLASSPATH=$SERVER_HOME/target/src/classes:$CLASSPATH
fi

main=com.bt.shopguide.collector.app.AppLuncher

export CLASSPATH=$CLASSPATH
$JAVA_HOME/bin/java ${main}


