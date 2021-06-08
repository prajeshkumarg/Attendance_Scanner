package com.example.attendancescanner.models;

import android.widget.ListView;

import java.util.List;

public class Classes {
    List<String> class_names;

    public List<String> getClass_names() {
        return class_names;
    }

    public void setClass_names(List<String> class_names) {
        this.class_names = class_names;
    }

    public Classes(List<String> class_names) {
        this.class_names = class_names;
    }
}
