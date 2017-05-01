# Java library which provides developers notification via messengers 

[![Build Status](https://travis-ci.org/fedorchuck/developers-notification.svg?branch=master)](https://travis-ci.org/fedorchuck/developers-notification)
[![Apache License Version 2.0](https://img.shields.io/badge/license-Apache%20License%202.0-brightgreen.svg)](https://github.com/fedorchuck/developers-notification/blob/master/LICENSE.md)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.fedorchuck/developers-notification/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.fedorchuck/developers-notification)

## Introduction
Sometimes every developer needs to know about the event that happened as soon as possible. For example, about irregular work of the server or about changing the third-party rest-api, about anything, depending on the specifics of the project.
 
There are many ways to inform developers about such events - all sorts of services, loggers. But most of them send notifications to the mail or a issue tracking system of a different type, which is not always convenient and not always possible to track quickly.

This library sending notifications to messengers. Just create and connect a bot in the messenger that your team using and that is supported by this library. For now it is Slack and Telegram. Add the library to the project, add it configuration (access keys to the bot that you created), add lines, where it is needed, to send message and you receive messages when something happened.

This library compatible with Java 6+

## Getting started
### Download
Gradle:
```groovy
compile 'com.github.fedorchuck:developers-notification:0.1.1'
```
Maven:
```xml
<dependency>
  <groupId>com.github.fedorchuck</groupId>
  <artifactId>developers-notification</artifactId>
  <version>0.1.1</version>
</dependency>
```
JAR-files:  
https://oss.sonatype.org/content/repositories/releases/com/github/fedorchuck/developers-notification/

### Setup
The library is configured by environment variables or system properties. Supported variables:
* `DN_MESSENGER` where the message will be sent; possible values you can see in [DevelopersNotificationMessenger](http://fedorchuck.github.io/developers-notification/javadoc/com/github/fedorchuck/developers_notification/DevelopersNotificationMessenger.html) required if you use method <code>send</code> with signature `DevelopersNotification.send(String, String, Throwable)`;
* `DN_SLACK_TOKEN` access key; required if you use Slack messages;
* `DN_SLACK_CHANNEL` destination chat; required if you use Slack messages;
* `DN_TELEGRAM_TOKEN` access key; required if you use Telegram messages;
* `DN_TELEGRAM_CHANNEL` destination chat; required if you use Telegram messages;
* `DN_USER_AGENT` user agent for [HttpClient](https://fedorchuck.github.io/developers-notification/javadoc/com/github/fedorchuck/developers_notification/http/HttpClient.html) is not required; default value is `Mozilla/5.0`;
* `DN_CONNECT_TIMEOUT` for [HttpClient](https://fedorchuck.github.io/developers-notification/javadoc/com/github/fedorchuck/developers_notification/http/HttpClient.html) is not required; default value is `5000`.

Required configuration which will be using for sending messages.

### Connecting your bot
#### Slack
1) Login in into [Slack](https://slack.com/)
1) Add [incoming-webhook](https://my.slack.com/services/new/incoming-webhook/)
2) Choose a channel and add press green button :-)
3) Choose the channel again add press green button, again :-)
4) Find the `Integration Settings` section
5) Find the `Webhook URL` sub-section and copy token. Example url: `https://hooks.slack.com/services/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`, token: `TZXG2L132/B56Z6RGR1/AdlsZzn1QGfo2qrEK4jpO4wJ`
6) Add configuration to your project. For this example it is:
* `DN_SLACK_TOKEN` = `TZXG2L132/B56Z6RGR1/AdlsZzn1QGfo2qrEK4jpO4wJ`
* `DN_SLACK_CHANNEL` = `general`

#### Telegram
1) Create your [bot](https://core.telegram.org/bots#3-how-do-i-create-a-bot)
2) Add the Telegram BOT to your chat.
3) Get the list of updates for your BOT:
```http
GET https://api.telegram.org/bot<YourBOTToken>/getUpdates
```
example:
```http
GET https://api.telegram.org/bot32031087p:abzcu_j17-jbd78sadvbdy63d37gda37-d8/getUpdates
```
4) Find object <b>chat</b>. It looks like this:
```json
        "chat": {
          "id": -197235129,
          "title": "test group developers notification",
          "type": "group",
          "all_members_are_administrators": true
        },
```
5) Add configuration to your project: 
* `DN_TELEGRAM_TOKEN` = `YourBOTToken`
* `DN_TELEGRAM_CHANNEL` = `chat.id`
 For this example it is:
* `DN_TELEGRAM_TOKEN` = `32031087p:abzcu_j17-jbd78sadvbdy63d37gda37-d8`
* `DN_TELEGRAM_CHANNEL` = `-197235129`

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
## Changelog
See [changelog file](https://github.com/fedorchuck/developers-notification/blob/master/CHANGELOG.md)

## Dependencies
* [org.projectlombok:lombok:1.16.16](https://projectlombok.org/)
* com.fasterxml.jackson.core:jackson-core:2.8.8
* com.fasterxml.jackson.core:jackson-annotations:2.8.8
* com.fasterxml.jackson.core:jackson-databind:2.8.8
* [org.slf4j:slf4j-api:1.7.25](https://www.slf4j.org/) 

## License
This software is licensed under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
