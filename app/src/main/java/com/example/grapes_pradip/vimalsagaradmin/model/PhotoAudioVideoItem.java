package com.example.grapes_pradip.vimalsagaradmin.model;



public class PhotoAudioVideoItem {

    private String url;
    private String name;
    private String specification;

    public PhotoAudioVideoItem(String url, String name, String specification) {
        this.url = url;
        this.name = name;
        this.specification = specification;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }
}
