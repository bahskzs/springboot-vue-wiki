package com.yqy.wiki.vo;

/**
 * @author bahsk
 * @createTime 2021-06-25 12:45
 * @description
 */
public class ChartDataVO {


    private String name;

    private Integer value;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());

        sb.append(", name=").append(name);
        sb.append(", value=").append(value);
        sb.append("]");
        return sb.toString();
    }
}
