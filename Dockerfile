# Use Java 11
FROM maven:3.8.5-openjdk-11

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Build the project
RUN mvn clean package -DskipTests

# Run the app
CMD ["java", "-cp", "target/classes:target/dependency/*", "bank.Main"]