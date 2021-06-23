package com.yqy.wiki.vo;

/**
 * @author bahsk
 * @createTime 2021-06-18 21:55
 * @description
 */
public class EbookVO {

    private Long id;

    private String name;



    private String description;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");

        sb.append(", id=").append(id);
        sb.append(", name=").append(name);

        sb.append(", description=").append(description);

        sb.append("]");
        return sb.toString();
    }
}
