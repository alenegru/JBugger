package com.example.springbootdemo.service;

import com.example.springbootdemo.dto.AttachmentDTO;
import com.example.springbootdemo.model.Attachment;
import com.example.springbootdemo.model.Bug;
import com.example.springbootdemo.repository.AttachmentRepository;
import com.example.springbootdemo.repository.BugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;

import static com.example.springbootdemo.model.Attachment.mapToDTO;

@Component
public class AttachmentService {
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private BugRepository bugRepository;

    @Transactional
    public Boolean addAttachmentToBug(MultipartFile file, Long id) throws IOException {
        Bug bug = bugRepository.findById(id).orElse(null);
        if (bug == null)
            throw new EntityNotFoundException("Bug could not be found!");
        Attachment attachment = new Attachment();
        attachment.setContent(file.getBytes());
        attachment.setTitle(file.getOriginalFilename());
        attachment.setBug(bug);
        attachmentRepository.save(attachment);
        return true;
    }

    @Transactional
    public List<AttachmentDTO> findByBug(Long id) {
        List<Attachment> attachments = attachmentRepository.findByBug(id).orElse(null);
        if (attachments == null)
            throw new EntityNotFoundException("Bug could not be found!");
        return attachments.stream().map(Attachment::mapToDTO).toList();
    }

    @Transactional
    public AttachmentDTO findById(Long id) {
        Attachment attachment = attachmentRepository.findById(id).orElse(null);
        if (attachment == null)
            throw new EntityNotFoundException("Attachment could not be found!");
        return mapToDTO(attachment);
    }

}
