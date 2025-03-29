export MAVEN_OPTS="-Xmx2g -XX:ReservedCodeCacheSize=512m -XX:+UseG1GC"
mvn clean install;
mvn test
mvn spring-boot:run;

