package com.matag.admin.user.verification;

import com.matag.admin.user.MatagUser;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "matag_user_verification")
public class MatagUserVerification {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @OneToOne
  @JoinColumn(referencedColumnName = "id")
  private MatagUser matagUser;
  private String verificationCode;
  private LocalDateTime validUntil;
  private Integer attempts;
}
