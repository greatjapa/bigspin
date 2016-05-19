# Bigspin 

A simple, compact and extensible mechanism to load/get internationalized messages for Java. This library defines a common interface `I18N` and provides a default implementation based on a property file `DefaultI18N`. 

#### Why should I use I18N instead of ResourceBundle properly?

Most of well-known internationalization tools are property-file based, as [ResourceBundle](https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html). However, `ResourceBundle` has some limitations for use in complex projects. If you want to split your messages in differents files (e.g. base_messages, server_messages, client_messages, log_messages, etc) how to load all of them in one single object? If your project runs in different VMs distributed in several machines, do we need to replicate all files on every machine?

Bigspin aims to solve these problems in one simple way. Here are the feature list: 

- I18N is extensible because you can provide your custom implementation for `I18N` interface, for instance, load messages from database, caching, etc;
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

Created by and copyright GitHub, Inc. Released under the [MIT license](LICENSE).
