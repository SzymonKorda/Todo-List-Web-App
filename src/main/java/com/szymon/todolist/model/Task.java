package com.szymon.todolist.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Column(name = "description", length = 200, nullable = false)
    private String description;

    @CreationTimestamp
    @Column(name = "created_on", nullable = false)
    private Date createdOn;

    @Column(name = "finished_on")
    private Date finishedOn;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
