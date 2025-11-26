## Environment

**Java Version: Java 21**

**Build Tool: Maven**

## Code Style & Formatting

**Variables:** 'minutesPerHour' (camelCase)

**Classes:** 'MyClass' (PascalCase)

**CONSTANTS:** 'MAX_ATTEMPTS' (UpperSnakeCase)

**Braces:** 'if (condition) {' same line as statement

**Spaces:** 'x = 5 + 3' (put spaces around operators)

## Run (local / Codespaces)

### Build:

mvn -q -DskipTests package

### Run:

java -jar target/experiment-1.0-SNAPSHOT.jar

(Or run from Codespaces / IDE with `App.java` main method.)