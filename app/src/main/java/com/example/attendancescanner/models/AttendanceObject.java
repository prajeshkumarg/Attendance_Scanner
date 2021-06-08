package com.example.attendancescanner.models;

import java.util.List;

public class AttendanceObject {
    private List<String> attendanceContent;
    private String attendanceTitle;

    public AttendanceObject() {
    }

    public AttendanceObject(List<String> attendanceContent, String attendanceTitle) {
        this.attendanceContent = attendanceContent;
        this.attendanceTitle = attendanceTitle;
    }

    public List<String> getAttendanceContent() {
        return attendanceContent;
    }

    public void setAttendanceContent(List<String> attendanceContent) {
        this.attendanceContent = attendanceContent;
    }

    public String getAttendanceTitle() {
        return attendanceTitle;
    }

    public void setAttendanceTitle(String attendanceTitle) {
        this.attendanceTitle = attendanceTitle;
    }
}
