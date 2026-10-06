package com.udacity.jwdnd.course1.cloudstorage.model;

public class Credential {
    private Integer credentialid;
    private String url1;
    private String username;
    private String key1;
    private String password1;
    private Integer userid;

    public Credential (Integer credentialid, String url1, String username, String key1,String password1, Integer userid) {
        this.credentialid = credentialid;
        this.url1 = url1;
        this.username = username;
        this.key1 = key1;
        this.password1 = password1;
        this.userid = userid;
    }

    public Integer getCredentialid() { return credentialid; }
    public void setCredentialid(Integer credentialid) { this.credentialid = credentialid; }
    public String getUrl1() { return url1; }
    public void setUrl1(String url1) { this.url1 = url1; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getKey1() { return key1; }
    public void setKey1(String key1) { this.key1 = key1; }
    public String getPassword1() { return password1; }
    public void setPassword1(String password1) { this.password1 = password1; }
    public Integer getUserid() { return userid; }
    public void setUserid(Integer userid) { this.userid = userid; }
}
