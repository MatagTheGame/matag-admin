package com.matag.admin.session;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.matag.admin.user.MatagUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "matag_session")
public class MatagSession {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String sessionId;
  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private MatagUser matagUser;
  private LocalDateTime createdAt;
  private LocalDateTime validUntil;

  public Long getId() {
    return id;
  }

  public String getSessionId() {
    return sessionId;
  }

  public MatagUser getMatagUser() {
    return matagUser;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getValidUntil() {
    return validUntil;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public void setMatagUser(MatagUser matagUser) {
    this.matagUser = matagUser;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public void setValidUntil(LocalDateTime validUntil) {
    this.validUntil = validUntil;
  }
}
