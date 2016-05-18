# Bigspin 

(under construction)

Simple, compact and extensible mechanism to load/get intenationalized messages in Java. This library defines a common interface `I18N` and provides a default implementation based on properties files, `DefaultI18N`. 

#### Why should I use I18N instead of ResourceBundle?

Most of well-known internationalization tools are property-file based, as [ResourceBundle](https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html). 

- DefaultI18N is Serializable;
- I18N is extensible because you can provide your custom implementation for `I18N` interface, for instance, load messages from Database, caching, etc;
- You can build a hierarchical set of `I18N` instances to reuse and redefine some messages;
- You can define a callback to manage missing resources (ResourceBundle throws [MissingResourceException](https://docs.oracle.com/javase/8/docs/api/java/util/MissingResourceException.html)

```Java
I18N i18n = new DefaultI18N("base_en_US.properties");
String okText = i18n.get("ok"); // okText = Yes 
```

You may redefine messages

```Java
//create a hierarchical I18N
i18n = new DefaultI18N("custom_en_US.properties", i18n);
okText = i18n.get("ok"); // okText = Confirm 
```

## License

Created by and copyright GitHub, Inc. Released under the [Apache license](LICENSE).
