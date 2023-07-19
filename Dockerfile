FROM shoothzj/compile:jdk17-mvn AS build
COPY . /opt/compile
WORKDIR /opt/compile
RUN mvn -B clean package -DskipTests

FROM shoothzj/base:jdk8

WORKDIR /opt/paas-dashboard

COPY --from=build /opt/compile/target/paas-dashboard-0.0.1-SNAPSHOT-jar-with-dependencies.jar /opt/paas-dashboard/paas-dashboard.jar

RUN wget -q https://github.com/paas-dashboard/paas-dashboard-portal-elementplus/releases/download/latest/paas-dashboard-portal.tar.gz && \
    tar -xzf paas-dashboard-portal.tar.gz && \
    rm -rf paas-dashboard-portal.tar.gz

EXPOSE 11111

CMD ["/usr/bin/dumb-init", "java", "-jar", "/opt/paas-dashboard/paas-dashboard.jar"]
