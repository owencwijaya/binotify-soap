FROM tomcat:jdk17
 
COPY ./target/binotify-soap.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]