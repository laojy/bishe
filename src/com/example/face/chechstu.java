package com.example.face;

import java.io.Serializable;

public class chechstu implements Serializable {
  private String memberid;
  private String faceid;
  private String name;
  private String remark;
  private String score;
  private String groupid;
public String getGroupid() {
	return groupid;
}
public void setGroupid(String groupid) {
	this.groupid = groupid;
}
public String getMemberid() {
	return memberid;
}
public void setMemberid(String memberid) {
	this.memberid = memberid;
}
public String getFaceid() {
	return faceid;
}
public void setFaceid(String faceid) {
	this.faceid = faceid;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getRemark() {
	return remark;
}
public void setRemark(String remark) {
	this.remark = remark;
}
public String getScore() {
	return score;
}
public void setScore(String score) {
	this.score = score;
}
}
