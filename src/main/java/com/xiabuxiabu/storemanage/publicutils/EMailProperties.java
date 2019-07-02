package com.xiabuxiabu.storemanage.publicutils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "mailconfig",ignoreUnknownFields = false)
@PropertySource(value = "classpath:config/mailConfig.properties",encoding = "UTF-8")
@Component
public class EMailProperties {

    private String nickname;
    private String subject;
    private String host;
    private String username;
    private String password;
    private String encoding;
    private String additems;
    private String checkitems;
    private String updateitems;

    public String getUpdateitems() {
        return updateitems;
    }

    public void setUpdateitems(String updateitems) {
        this.updateitems = updateitems;
    }

    public String getAdditems() {
        return additems;
    }

    public void setAdditems(String additems) {
        this.additems = additems;
    }

    public String getCheckitems() {
        return checkitems;
    }

    public void setCheckitems(String checkitems) {
        this.checkitems = checkitems;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
