package com.recruitment.skybook.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "airports")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    private String name;
    private String city;
    private String country;
    private String timezone;
    private double latitude;
    private double longitude;

    @OneToMany(mappedBy = "airport", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Terminal> terminals = new ArrayList<>();
}
