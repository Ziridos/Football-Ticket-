FROM gradle:8.8-jdk21

WORKDIR /opt/app

COPY ./build/libs/Ticketmaster-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar Ticketmaster-0.0.1-SNAPSHOT.jar"]