package com.example.springbootdemo.model;

import com.example.springbootdemo.dto.HistoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "history_gen")
    @SequenceGenerator(name = "history_gen", sequenceName = "history_seq", allocationSize = 1)
    private Long idHistory;

    @ManyToOne
    @JoinColumn(name = "id_bug", referencedColumnName = "idBug")
    private Bug bug;

    private Timestamp modifiedDate;
    private String afterStatus;
    private String beforeStatus;
    private String modifiedBy;

    public static HistoryDTO mapToDTO(History history){
        HistoryDTO dto=new HistoryDTO();
        dto.setId(history.getIdHistory());
        dto.setAfterStatus(history.getAfterStatus());
        dto.setBeforeStatus(history.getBeforeStatus());
        dto.setIdBug(history.getBug().getIdBug());
        dto.setModifiedBy(history.getModifiedBy());
        dto.setModifiedDate(history.getModifiedDate());
        return dto;
    }
}
