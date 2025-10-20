Java 21
========

This project is configured to build and run with Java 21 (LTS). Ensure your local JDK is Java 21 before building. The Docker image uses Temurin 21.

To build locally:

	mvn -DskipTests package

To run with Docker:

	docker build -t task-api .
	docker run -p 8080:8080 task-api

