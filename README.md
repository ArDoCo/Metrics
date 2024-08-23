# ArDoCo: Metrics Calculator
Welcome to the **ArDoCo Metrics Calculator** project! This tool provides functionality to calculate and aggregate **classification** and **rank metrics** for various machine learning and ranking tasks.

The [Wiki](https://github.com/ArDoCo/Metrics/wiki) contains all the necessary information to use the **ArDoCo Metrics Calculator** via multiple interfaces, including a library, REST API, and command-line interface (CLI).

## Quickstart

To use this project as a Maven dependency, you need to include the following dependency in your `pom.xml` file:

```xml
<dependency>
    <groupId>io.github.ardoco</groupId>
    <artifactId>metrics</artifactId>
    <version>${revision}</version>
</dependency>
```

To use the CLI run the following command:

```shell
java -jar metrics-cli.jar -h
```

To use the REST API via Docker, start the server with the following command:
```shell
docker run -it -p 8080:8080 ghcr.io/ardoco/metrics:latest
```
