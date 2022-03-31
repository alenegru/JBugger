package com.example.springbootdemo.dto;


import com.example.springbootdemo.model.enums.BugSeverity;

import com.example.springbootdemo.model.enums.BugStatus;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
public class BugDTO {
    private Long id;
    private String title;
    private String description;
    private String version;
    private Timestamp targetDate;
    private BugStatus status;
    private String fixedVersion;
    private BugSeverity severity;
    private String assignedTo;
    private String createdBy;
    private List<AttachmentDTO> attachments;
}
