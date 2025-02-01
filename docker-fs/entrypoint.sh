#!/bin/sh

if [ $# -gt 0 ]; then
    exec "$@"
fi

trap "nginx -s stop" EXIT

nginx
java -jar target/*-spring-boot.jar
exit $?
