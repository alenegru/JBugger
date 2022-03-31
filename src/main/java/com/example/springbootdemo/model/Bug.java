package com.example.springbootdemo.model;

import com.example.springbootdemo.model.enums.BugSeverity;
import com.example.springbootdemo.model.enums.BugStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "bugs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bug {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bugs_gen")
    @SequenceGenerator(name = "bugs_gen", sequenceName = "bugs_seq", allocationSize = 1)
    private Long idBug;
    @Column(nullable = false, unique = true)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    @Pattern(regexp = "^[a-zA-Z0-9.]+$")
    private String version;
    private Timestamp targetDate;

    private BugStatus status;
    private String fixedVersion;
    private BugSeverity severity;

    @ManyToOne
    @JoinColumn(name = "id_user_creator", referencedColumnName = "idUser")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "id_user_assigned", referencedColumnName = "idUser")
    private User assignedTo;

    @OneToMany(mappedBy = "bug", cascade = CascadeType.REMOVE)
    private List<Attachment> attachment;

    @OneToMany(mappedBy = "bug", cascade = CascadeType.REMOVE)
    private List<History> histories;

    @OneToMany(mappedBy = "bug")
    private List<Comment> comments;
}
