# Java library which provides developers notification via messengers 

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/303c60ee26a14d1ead7c6c1473ab25cc)](https://www.codacy.com/app/vl.fedorchuck/developers-notification?utm_source=github.com&utm_medium=referral&utm_content=fedorchuck/developers-notification&utm_campaign=badger)
[![Build Status](https://travis-ci.org/fedorchuck/developers-notification.svg?branch=master)](https://travis-ci.org/fedorchuck/developers-notification)
[![Apache License Version 2.0](https://img.shields.io/badge/license-Apache%20License%202.0-brightgreen.svg)](https://github.com/fedorchuck/developers-notification/blob/master/LICENSE.md)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.fedorchuck/developers-notification/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.fedorchuck/developers-notification)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/303c60ee26a14d1ead7c6c1473ab25cc)](https://www.codacy.com/app/vl.fedorchuck/developers-notification?utm_source=github.com&utm_medium=referral&utm_content=fedorchuck/developers-notification&utm_campaign=badger)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/4f64c5669e1e4f9684c8f916eb76842f)](https://www.codacy.com/app/vl.fedorchuck/developers-notification?utm_source=github.com&utm_medium=referral&utm_content=fedorchuck/developers-notification&utm_campaign=Badge_Coverage)

## Introduction
Sometimes every developer needs to know about the event that happened as soon as possible. For example, about incorrect 
work of the server or about changing the third-party rest-api, about anything, depending on the specifics of the project.
 
There are many ways to inform developers about such events - all sorts of services, loggers. But most of them send 
notifications to the mail or a issue tracking system of a different type, which is not always convenient and not always 
possible to track quickly.

This library sending notifications to messengers. Just create and connect a bot in the messenger that your team using 
and that is supported by this library. For now it is Slack and Telegram. Add the library to the project, add it 
configuration (access keys to the bot that you created), add lines, where it is needed, to send message or logging 
appender and you receive messages when something happened.

Also, this library have monitoring module. It can monitor current usage of RAM and disk memory, set limits of their 
usage and in case of overspending - informs to the selected messengers.

This library compatible with Java 6+


## Contents

- [Getting started](#getting-started)
  - [Download](#download)
  - [Setup](#setup)
    - [Environment configuration](#environment-configuration)
    - [Logging appender](#logging-appender)
  - [Connecting your bot](#connecting-your-bot)
    - [Slack](#slack)
    - [Telegram](#telegram) 
- [Available methods](#available-methods)
- [Usage](#usage) 
- [Changelog](#changelog)
- [Dependencies](#dependencies)
- [License](#license)

## Getting started
### Download
Gradle:
```groovy
compile 'com.github.fedorchuck:developers-notification:0.3.0'
```
Maven:
```xml
<dependency>
  <groupId>com.github.fedorchuck</groupId>
  <artifactId>developers-notification</artifactId>
  <version>0.3.0</version>
</dependency>
```
JAR-files:  
https://oss.sonatype.org/content/repositories/releases/com/github/fedorchuck/developers-notification/

### Setup
#### Environment configuration
The library is configured by environment variables or system properties. Supported variable is `DN`. It is single 
environment variable witch required if you use this library. Accepted value is JSON:
```json
{
	"messenger": [{
		"name": "SLACK",
		"token": "SLACK_TOKEN",
		"channel": "SLACK_CHANNEL"
	}, {
		"name": "TELEGRAM",
		"token": "TELEGRAM_TOKEN",
		"channel": "TELEGRAM_CHANNEL"
	}],
	"show_whole_log_details": true,
	"protection_from_spam": true,
	"project_name": "Where this library will be invoked",
	"connect_timeout": 5000,
	"user_agent": "Mozilla/5.0",
	"monitoring": {
		"period": 5,
		"unit": "seconds",
		"max_ram": 90,
		"max_disk": 90,
		"disk_consumption_rate": 2
	}
}
```
* `messenger` it is array of objects with configure part of messages destination; required;
   * `name` contains name of integration. Now available: `SLACK` and `TELEGRAM`;
   * `token` contains token of integration;
   * `channel` contains channel of integration;  
   
   **Note:** If you specify several messengers, you will recive instant messages to all specified messengers.
* `show_whole_log_details` receive boolean value; if value is `true` - log will contain Information containing 
passwords; is not required; default value is `false`;
* `protection_from_spam` receive boolean value; if value is `true` - you will be protected from spam (receiving the 
same messages each second). It is necessary for adjust the frequency of sending messages; is not required; default value is `false`;
* `project_name` name of project using this library; required;
* `connect_timeout` for 
[HttpClient](https://fedorchuck.github.io/developers-notification/javadoc-0.3.0/com/github/fedorchuck/developers_notification/http/HttpClient.html) is not required; default value is `5000`;
* `user_agent` user agent for 
[HttpClient](https://fedorchuck.github.io/developers-notification/javadoc-0.3.0/com/github/fedorchuck/developers_notification/http/HttpClient.html) is not required; default value is `Mozilla/5.0`;
* `monitoring` it is object with configure monitoring of current RAM and disk memory usage, set limits of their use and 
in case of overspending - informs in the selected messengers; required if you use this feature;
   * `period` receive integer value; the frequency with which the monitoring will be carried out;
   * `unit` the designation in which units the period is measured; should be:
      * `SECONDS`
      * `MINUTES`
      * `HOURS`
      * `DAYS`
  * `max_ram` receive integer value; determines the permissible limit of use ram in percent;
  * `max_disk` receive integer value; determines the permissible limit of use disk in percent;
  * `disk_consumption_rate` receive integer value; determines the permissible limit of disk consumption rate in percent;

#### Logging appender
If you want routing some logging level messages to messengers, you should configure this part of project. If you use [log4j](http://logging.apache.org/log4j/1.2/) - your `log4j.properties` file should contain rows like this:
```property
# Define the root logger with appender DevelopersNotificationAppender
log4j.rootLogger = DEBUG, DNAppender

# Define the DevelopersNotificationAppender appender
log4j.appender.DNAppender=com.github.fedorchuck.developers_notification.DevelopersNotificationAppender

log4j.appender.DNAppender.Level=ERROR
log4j.appender.DNAppender.layout=org.apache.log4j.PatternLayout
log4j.appender.DNAppender.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} [%c{1}] %5p - %m%n
```

   **Note:** If you specify unknown logging level, you will recive all logging events to all specified messengers.

### Connecting your bot
#### Slack
1) Login in into [Slack](https://slack.com/)
1) Add [incoming-webhook](https://my.slack.com/services/new/incoming-webhook/)
2) Choose a channel and add press green button :-)
3) Choose the channel again add press green button, again :-)
4) Find the `Integration Settings` section
5) Find the `Webhook URL` sub-section and copy token. Example url: 
`https://hooks.slack.com/services/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`, token: `TZXG2L132/B56Z6RGR1/AdlsZzn1QGfo2qrEK4jpO4wJ`
6) Messenger configuration to your project should look like this:
```json
{
    "name": "SLACK",
    "token": "TZXG2L132/B56Z6RGR1/AdlsZzn1QGfo2qrEK4jpO4wJ",
    "channel": "general"
}
```

#### Telegram
1) [Create your bot](https://core.telegram.org/bots#3-how-do-i-create-a-bot)
2) Add the Telegram BOT to your chat.
3) Get the list of updates for your BOT:
```http
GET https://api.telegram.org/bot<YourBOTToken>/getUpdates
```
example:
```http
GET https://api.telegram.org/bot32031087:pabzcu_j17-jbd78sadvbdy63d37gda37-d8/getUpdates
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
5) Messenger configuration to your project should seems like this:
```json
{
    "name": "TELEGRAM",
    "token": "32031087:pabzcu_j17-jbd78sadvbdy63d37gda37-d8",
    "channel": "-197235129"
}
```
## Available methods
You can set needed environment variables using:
```groovy
DevelopersNotificationUtil.setEnvironmentVariable(key, value);
```
For checking set environment variables which needed this library use:
```groovy
DevelopersNotification.printConfiguration();
```
**Note:** If configuration value of field `show_whole_log_details` is `false` - will be printed result of method 
[Config#getPublicToString()](https://fedorchuck.github.io/developers-notification/javadoc-0.3.0/com/github/fedorchuck/developers_notification/configuration/Config.html)
For sending message to chosen destination you can use methods:
1) Messenger, spam protection and project name will be reading from config.</p>
```groovy
DevelopersNotification.send(
    description,
    throwable
);
```
```groovy
DevelopersNotification.send(
    "short description",
    new IllegalStateException("abcd")
);
```
2) Messenger and spam protection will be reading from config.
```groovy
DevelopersNotification.send(
   projectName,
   description,
   throwable
);
```
```groovy
DevelopersNotification.send(
   "your project name",
   "short description",
   new IllegalStateException("abcd")
);
```
3) Spam protection will be reading from config.
```groovy
DevelopersNotification.send(
   messengerDestination,
   projectName,
   description,
   throwable
);
```
```groovy
DevelopersNotification.send(
    DevelopersNotificationMessenger.ALL_AVAILABLE,
   "your project name",
   "short description",
   new IllegalStateException("abcd")
);
```
4) Messenger will be reading from config.
```groovy
DevelopersNotification.send(
   protectionFromSpam,
   projectName,
   description,
   throwable
);
```
```groovy
DevelopersNotification.send(
   true,
   "your project name",
   "short description",
   new IllegalStateException("abcd")
);
```
For monitoring current usage of ram and disk memory and in case of overspending limits of their use - get messages in 
the selected messengers, you can use methods:
* Launches monitoring process for current application.
```groovy
DevelopersNotification.monitoringStart();
```
* Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted. 
Invocation has no additional effect if already shut down.
```groovy
DevelopersNotification.monitoringStop();
```
* Check if the monitoring thread is alive. A thread is alive if it has been started and has not yet died.
```groovy
DevelopersNotification.isMonitoringStateAlive();
```

## Usage
After [setup](#setup), just insert line in your code, for example:
```groovy
try {
// Your code
} catch (Exception ex) {
    DevelopersNotification.send("example of usage", ex);   
}
```

Also, you can use logger appender. Just send message into logger with logging level, which you specified in log 
configuration and you receive message into specified messenger.

## Changelog
See [changelog file](https://github.com/fedorchuck/developers-notification/blob/master/CHANGELOG.md)

## Dependencies
* [org.slf4j:slf4j-simple:1.7.25](http://www.slf4j.org/)
* [org.slf4j:slf4j-api:1.7.25](https://www.slf4j.org/) 
* [org.apache.httpcomponents:httpmime:4.5.4](http://hc.apache.org/httpcomponents-client/)
* [com.github.fedorchuck:dnjson:0.1.0](https://github.com/fedorchuck/dnjson/) 

## License
This software is licensed under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
