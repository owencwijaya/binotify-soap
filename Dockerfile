FROM tomcat:jdk17
COPY ./target/binotify-soap.war /usr/local/tomcat/webapps/
EXPOSE 3001
CMD ["catalina.sh", "run"]