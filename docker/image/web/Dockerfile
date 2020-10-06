FROM openjdk:8-jdk-alpine

LABEL maintainer="alexandre@piveteau.email"

ADD bin /usr/local/bin
ADD lib /usr/local/lib

ENTRYPOINT [ "./usr/local/bin/web" ]