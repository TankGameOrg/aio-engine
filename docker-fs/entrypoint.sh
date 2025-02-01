#!/bin/sh

if [ $# -gt 0 ]; then
    exec "$@"
fi

export STORAGE_PATH=/app/games

trap "nginx -s stop" EXIT

nginx
java -jar target/*-spring-boot.jar
exit $?
