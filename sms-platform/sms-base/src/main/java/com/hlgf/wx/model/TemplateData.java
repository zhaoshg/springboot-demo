package com.hlgf.wx.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 *
 */
@Getter
@Setter
@Data
public class TemplateData {
    public TemplateData() {

    }

    public TemplateData(String value) {
        this.value = value;
    }

    public TemplateData(String value, String color) {
        this.value = value;
        this.color = color;
    }

    private String value;
    private String color = "#000000";
}
