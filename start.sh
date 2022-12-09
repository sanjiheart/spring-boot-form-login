#!/bin/bash

yarn install
gulp build
./gradlew clean build
java -jar build/libs/spring-boot-form-login-1.0.0.jar