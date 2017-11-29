package org.paasta.servicebroker.deliverypipeline.model;

/**
 * Created by hrjin on 2017-05-29.
 */
public class CiInfo {

    private Long id;


    private String serverUrl;


    private int usedcount;


    private String type;


    private String status;

    public CiInfo() {
        // DO NOTHING
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public int getUsedcount() {
        return usedcount;
    }

    public void setUsedcount(int usedcount) {
        this.usedcount = usedcount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
