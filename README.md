
# v2v
a v2v example repo
=======
# Toyota Vehicle-to-Vehicle Mobile Cloud Node
This Java application demonstrates a node in the mobile cloud middleware. Composed of Java Servlet and JGroups RPC instance, a node may instantiate a cluster of other nodes, which then can send/receive messages to compute shared information ready to be accessed by client applications.

# Pre-requisite
1. Apache Tomcat 7+ installed
2. Maven 3+ installed
3. JDK 1.7+ installed

# Compilation
`mvn clean install`

This command will create the compiled .jar file with dependencies.

## Poor Man's Installation
Simply copy the created .jar file to:
`cp {$V2V_DIR}/target/v2v-1.0-SNAPSHOT-jar-with-dependencies.jar {$TOMCAT_SERVER_DIR}/WEB-INF/lib`
and change {$TOMCAT_DIR}/WEB-INF/web.xml to deploy the servlet.

    <servlet>
      <servlet-name>jgroupsrpc</servlet-name>
      <servlet-class>cmu.practicum.servlet.JgroupsServlet</servlet-class>
    </servlet>

    <servlet-mapping>
      <servlet-name>jgroupsrpc</servlet-name>
      <url-pattern>{CHOOSE_YOUR_URL_HERE}</url-pattern>
    </servlet-mapping>

