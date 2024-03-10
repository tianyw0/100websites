#!/bin/bash

echo '开始运行...'

ps -ef | grep knowledge-0.0.1.jar  | grep -v grep | awk '{print $2}'  | xargs kill -9;
export BUILD_ID=dontKillMe
mvn clean package
nohup java -jar ./target/knowledge-0.0.1.jar &> nohup.out&
#奇怪如果去掉以下延时,就会被jenkins杀掉.尴尬。。。
sleep 10s