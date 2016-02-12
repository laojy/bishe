package com.example.face;

import java.io.Serializable;

public class group implements Serializable {
 private String api_key=null;
 private String api_secret=null;
 private String group_id=null;
 private String name=null;
 private String status=null;
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public String getApi_key() {
	return api_key;
}
public void setApi_key(String api_key) {
	this.api_key = api_key;
}
public String getApi_secret() {
	return api_secret;
}
public void setApi_secret(String api_secret) {
	this.api_secret = api_secret;
}
public String getGroup_id() {
	return group_id;
}
public void setGroup_id(String group_id) {
	this.group_id = group_id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
}
