package com.example.springbootdemo.service;

import com.example.springbootdemo.dto.BugDTO;
import com.example.springbootdemo.dto.HistoryDTO;
import com.example.springbootdemo.model.Bug;
import com.example.springbootdemo.model.History;
import com.example.springbootdemo.model.enums.BugStatus;
import com.example.springbootdemo.repository.BugRepository;
import com.example.springbootdemo.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

import static com.example.springbootdemo.model.History.mapToDTO;

@Component
public class HistoryService {
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private BugRepository bugRepository;

    public HistoryDTO addHistory(BugStatus oldStatus, BugStatus newStatus, Long id) {
        History history=new History();
        history.setBeforeStatus(oldStatus.name());
        history.setAfterStatus(newStatus.name());
        history.setBug(bugRepository.findById(id).get());
        history.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        history.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        historyRepository.save(history);
        return mapToDTO(history);
    }

    public List<HistoryDTO> findAllByBug(Long id) {
        List<History> histories= (List<History>) historyRepository.findAll();
        return histories.stream().filter(history -> history.getBug().getIdBug().equals(id)).map(History::mapToDTO).toList();
    }
}
