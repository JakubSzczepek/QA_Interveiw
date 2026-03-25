package com.recruitment.skybook.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "terminals")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Terminal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "terminal_gates", joinColumns = @JoinColumn(name = "terminal_id"))
    @Column(name = "gate")
    @Builder.Default
    private List<String> gates = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "terminal_facilities", joinColumns = @JoinColumn(name = "terminal_id"))
    @Column(name = "facility")
    @Builder.Default
    private List<String> facilities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airport_id")
    @JsonIgnore
    private Airport airport;
}
