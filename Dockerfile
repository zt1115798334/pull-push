FROM java:8
MAINTAINER zt
VOLUME /tmp
COPY ./application/application.yml /application/

ADD /target/pull-push-0.0.1-SNAPSHOT.jar pull-push-0.0.1-SNAPSHOT.jar
RUN export LC_ALL=zh_CN.UTF-8
RUN echo "export LC_ALL=zh_CN.UTF-8"  >>  /etc/profile
RUN echo "Asia/shanghai" > /etc/timezone
EXPOSE 8080
ENTRYPOINT ["java","-jar","pull-push-0.0.1-SNAPSHOT.jar","--spring.config.location=/application/application.yml"]
