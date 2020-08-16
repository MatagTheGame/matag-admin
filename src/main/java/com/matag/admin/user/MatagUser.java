package com.matag.admin.user;

import com.matag.admin.user.verification.MatagUserVerification;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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
}
