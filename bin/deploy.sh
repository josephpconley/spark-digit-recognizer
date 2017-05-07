#!/bin/bash

JAR=$(find target/ -maxdepth 2 -name *.jar 2>&1)

if [ $? -eq 0 ]; then
    scp $JAR jconley@www.swingstats.com:/home/jconley
fi