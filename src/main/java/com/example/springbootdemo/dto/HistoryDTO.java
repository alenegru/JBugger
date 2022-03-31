package com.example.springbootdemo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@NoArgsConstructor
public class HistoryDTO {
    private Long id;
    private Timestamp modifiedDate;
    private String afterStatus;
    private String beforeStatus;
    private String modifiedBy;
    private Long idBug;
}
