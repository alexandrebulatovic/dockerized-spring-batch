# Use the openJDK alpine image as a parent image
FROM  openjdk:8-jdk-alpine

# Copy the file from your host to your current location
COPY SpringBatchHibernate.jar SpringBatchHibernate.jar

# Inform Docker that the container is listening on the specified port at runtime.
EXPOSE 8080

# Run the specified command within the container.
CMD ["java", "-jar", "SpringBatchHibernate.jar"]