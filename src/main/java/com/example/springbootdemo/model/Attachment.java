package com.example.springbootdemo.model;

import com.example.springbootdemo.dto.AttachmentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "attachments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attachments_gen")
    @SequenceGenerator(name = "attachments_gen", sequenceName = "attachments_seq", allocationSize = 1)
    private Long idAttachment;

    @ManyToOne
    @JoinColumn(name = "id_bug", referencedColumnName = "idBug")
    private Bug bug;
    @Lob
    private byte[] content;

    private String title;

    public static Attachment mapToAttachment(AttachmentDTO dto) {
        Attachment attachment = new Attachment();
        attachment.setContent(dto.getContent());
        return attachment;
    }

    public static AttachmentDTO mapToDTO(Attachment attachment) {
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        attachmentDTO.setContent(attachment.getContent());
        attachmentDTO.setTitle(attachment.getTitle());
        attachmentDTO.setId(attachment.getIdAttachment());
        return attachmentDTO;
    }
}
