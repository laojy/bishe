package com.example.face;
import java.io.Serializable;


public class stu implements Serializable {
	private String faceid;
	private String memberid;
	private String groupid;
	private String name;
	private String remark;
	private String apikey;
	private String apisecret;
	public String getApikey() {
		return apikey;
	}
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
	public String getApisecret() {
		return apisecret;
	}
	public void setApisecret(String apisecret) {
		this.apisecret = apisecret;
	}
	public String getFaceid() {
		return faceid;
	}
	public void setFaceid(String faceid) {
		this.faceid = faceid;
	}
	public String getMemberid() {
		return memberid;
	}
	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
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
}
