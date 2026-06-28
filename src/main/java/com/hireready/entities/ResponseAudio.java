package com.hireready.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "response_audios")
public class ResponseAudio {
    @Id
    private Long responseId;
    @Column(columnDefinition = "bytea")
    private byte[] audio;
    private String contentType;
}
