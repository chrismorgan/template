package com.morgan.template.model;

import lombok.Data;

import java.util.List;

@Data
public class Template {

    private Long id;
    private String body;
    private List<String> channels;

}
