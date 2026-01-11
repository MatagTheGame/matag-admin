package com.matag.admin.user;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.matag.admin.user.verification.MatagUserVerification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = {"password", "matagUserVerification"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "matag_user")
public class MatagUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String password;
  private String emailAddress;
  @Enumerated(EnumType.STRING)
  private MatagUserStatus status;
  @Enumerated(EnumType.STRING)
  private MatagUserType type;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @OneToOne(mappedBy = "matagUser", cascade = CascadeType.REMOVE)
  private MatagUserVerification matagUserVerification;

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public MatagUserStatus getStatus() {
    return status;
  }

  public MatagUserType getType() {
    return type;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public MatagUserVerification getMatagUserVerification() {
    return matagUserVerification;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public void setStatus(MatagUserStatus status) {
    this.status = status;
  }

  public void setType(MatagUserType type) {
    this.type = type;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public void setMatagUserVerification(MatagUserVerification matagUserVerification) {
    this.matagUserVerification = matagUserVerification;
  }
}
