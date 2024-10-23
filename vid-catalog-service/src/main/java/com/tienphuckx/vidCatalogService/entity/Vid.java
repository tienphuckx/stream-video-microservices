package com.tienphuckx.vidCatalogService.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@DynamicInsert
@Data
@Table(name = "tbl_vid_info")
public class Vid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "vid_name")
    private String vidName;

    @Column(name = "vid_des")
    private String vidDescription;

    @Column(name = "vid_path")
    private String vidPath;
}
