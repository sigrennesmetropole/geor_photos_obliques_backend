FROM openjdk:17.0.2-jdk-bullseye
USER root
MAINTAINER sigsupport@rennesmetropole.fr
ENV TZ=Europe/Paris

RUN mkdir /tmp/jetty
RUN chmod 777 -R /tmp && chmod o+t -R /tmp

RUN mkdir -p /etc/georchestra
RUN chmod 777 -R /etc/georchestra

RUN mkdir -p /opt/photosobliques
ADD georchestra-photos-obliques-facade.jar /opt/photosobliques/service.jar
RUN chmod 777 -R /opt/photosobliques

ENV PROPERTIES_PATH /etc/georchestra/photos-obliques/photos-obliques.properties

ADD entrypoint.sh /opt/photosobliques/entrypoint.sh
ENTRYPOINT [ "/bin/bash", "/opt/photosobliques/entrypoint.sh" ]

CMD [ "sh", "-c", "exec java --add-opens java.base/java.util=ALL-UNNAMED \
      -Duser.language=fr -Duser.country=FR \
      -Djava.io.tmpdir=/tmp/jetty \
      -Dgeorchestra.datadir=/etc/georchestra \
      -Dspring.config.additional-location=file:/etc/georchestra/config/ \
      ${ADD_JAVA_OPTS}                                   \
      -Xmx${XMX:-1G} -Xms${XMX:-1G}                      \
      -jar /opt/photosobliques/service.jar" ]  

