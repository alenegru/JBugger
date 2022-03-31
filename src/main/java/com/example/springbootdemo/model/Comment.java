package com.example.springbootdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "comments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comments_gen")
    @SequenceGenerator(name = "comments_gen", sequenceName = "comments_seq", allocationSize = 1)
    private Long idComment;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "idUser")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_bug", referencedColumnName = "idBug")
    private Bug bug;

    private String text;

    private Timestamp date;
}
