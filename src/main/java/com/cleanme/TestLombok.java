package com.cleanme;

import lombok.Data;

@Data
public class TestLombok {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = "ALEN";
    }
}


