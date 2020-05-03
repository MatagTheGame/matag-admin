package application.inmemoryrepositories;

import com.matag.admin.user.MatagUser;
import com.matag.admin.user.MatagUserRepository;
import com.matag.admin.user.MatagUserStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MatagUserAbstractInMemoryRepository extends AbstractInMemoryRepository<MatagUser> implements MatagUserRepository {
  @Override
  public Optional<MatagUser> findByEmailAddress(String email) {
    return DATA.values().stream()
      .filter(matagUser -> matagUser.getEmailAddress().equals(email))
      .findFirst();
  }

  @Override
  public Optional<MatagUser> findByUsername(String username) {
    return DATA.values().stream()
      .filter(matagUser -> matagUser.getUsername().equals(username))
      .findFirst();
  }

  @Override
  public long countUsersByStatus(MatagUserStatus matagUserStatus) {
    return DATA.values().stream().filter(u -> u.getStatus() == matagUserStatus).count();
  }
}
