# Bigspin 

[![Build Status](https://travis-ci.org/greatjapa/bigspin.svg?branch=master)](https://travis-ci.org/greatjapa/bigspin)
[![license](https://img.shields.io/github/license/mashape/apistatus.svg?maxAge=2592000)](https://github.com/greatjapa/bigspin/blob/master/LICENSE)
[![codecov](https://codecov.io/gh/greatjapa/bigspin/branch/master/graph/badge.svg)](https://codecov.io/gh/greatjapa/bigspin)

A simple, compact and extensible mechanism to load/get internationalized messages for your Java Application. This library defines a common interface `I18N` and provides a default implementation based on a property file `DefaultI18N`. 

#### Why should I use I18N instead of ResourceBundle?

Most of well-known internationalization libraries are property-file based, as [ResourceBundle](https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html). However, `ResourceBundle` has some practical limitations if you need work with distributed systems.

Let's say you want to split your messages in different files (e.g. base_messages, server_messages, client_messages, log_messages, etc). How should you load all files in a single object? How to redefine messages? Does it need to replicate all files if we ran in different VMs distributed in several machines?

Bigspin aims to solve these problems in a simple way. Here are the feature list: 

- I18N is extensible because you can provide your custom implementation for the `I18N` interface (e.g. load messages from database, files, web-resources, caching, etc);
- You can build a hierarchical set of `I18N` instances to reuse and redefine some messages;
- You can define a callback to manage missing resources (ResourceBundle throws [MissingResourceException](https://docs.oracle.com/javase/8/docs/api/java/util/MissingResourceException.html) in this case)
- DefaultI18N is Serializable;

#### Examples

Loading a local single file:

```Java
I18N i18n = new DefaultI18N("base_en_US.properties");
String okText = i18n.get("ok"); // okText = Yes 
```

Reusing messages stored in different files by creating a hierarchical set of `I18N` objects:

```Java
//create a hierarchical I18N
I18N i18n = new DefaultI18N("base_en_US.properties");
String okText = i18n.get("ok"); // okText = Yes 

i18n = new DefaultI18N("custom_en_US.properties", i18n);
okText = i18n.get("ok"); // okText = Confirm 
```
If your messages comes from a database instead of property files, you can make your own implementation of `I18N`. Pull request? =]

## License

 Bigspin is released under the [MIT license](LICENSE).
