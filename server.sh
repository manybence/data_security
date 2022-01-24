#! /usr/bin/env bash

clear
cd src

# Kill and relaunch Java RMI Registry
pidof rmiregistry | xargs kill &>/dev/null
sleep 0.5
rmiregistry&

# Compile and launch
javac *.java && java PrintServer

