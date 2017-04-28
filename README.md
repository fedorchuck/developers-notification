# Java library which provides developers notification via messengers 

[![Build Status](https://travis-ci.org/fedorchuck/developers-notification.svg?branch=master)](https://travis-ci.org/fedorchuck/developers-notification)
[![Apache License Version 2.0](https://img.shields.io/badge/license-Apache%20License%202.0-brightgreen.svg)](https://github.com/fedorchuck/developers-notification/blob/master/LICENSE.md)
[![Maven Central](https://img.shields.io/badge/maven--central-0.1.0-brightgreen.svg)](TODO)

## Introduction

## Getting started
### Download
Gradle:
```groovy
compile 'com.github.fedorchuck:developers-notification:0.1.0'
```
Maven:
```xml
<dependency>
  <groupId>com.github.fedorchuck</groupId>
  <artifactId>developers-notification</artifactId>
  <version>0.1.0</version>
</dependency>
```
JAR-files:  
TODO

### Connecting your bot
#### Telegram
#### Slack

### Setup
The library is configured with environment configuration. Supported environment variables:
* `DN_MESSENGER` where the message will be sent; possible values you can see in [DevelopersNotificationMessenger](http://fedorchuck.github.io/developers-notification/javadoc/com/github/fedorchuck/developers_notification/DevelopersNotificationMessenger.html) required if you use method <code>send</code> with signature `DevelopersNotification.send(String, String, Throwable)`;
* `DN_SLACK_TOKEN` access key; required if you use Slack messages;
* `DN_SLACK_CHANNEL` destination chat; required if you use Slack messages;
* `DN_TELEGRAM_TOKEN` access key; required if you use Telegram messages;
* `DN_TELEGRAM_CHANNEL` destination chat; required if you use Telegram messages;
* `DN_USER_AGENT` user agent for [HttpClient](https://fedorchuck.github.io/developers-notification/javadoc/com/github/fedorchuck/developers_notification/http/HttpClient.html) is not required; default value is `Mozilla/5.0`;
* `DN_CONNECT_TIMEOUT` for [HttpClient](https://fedorchuck.github.io/developers-notification/javadoc/com/github/fedorchuck/developers_notification/http/HttpClient.html) is not required; default value is `5000`.

Required configuration which will be using for sending messages.

## Usage
You can set needed environment variables using:
```groovy
DevelopersNotificationUtil.setEnvironmentVariable(key, value);
```
For checking set environment variables which needed this library use:
```groovy
DevelopersNotification.printConfiguration();
```
For sending message to chosen destination you can use methods:
```groovy
DevelopersNotification.send(DevelopersNotificationMessenger.ALL_AVAILABLE,
  "your project name", "short description", new IllegalStateException("abcd"));
```
```groovy
DevelopersNotification.send(
  "your project name", "short description", new IllegalStateException("abcd"));
```

# License
This software is licensed under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
