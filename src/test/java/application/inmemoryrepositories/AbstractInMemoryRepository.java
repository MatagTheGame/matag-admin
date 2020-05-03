package application.inmemoryrepositories;

import lombok.SneakyThrows;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

public abstract class AbstractInMemoryRepository<T> implements CrudRepository<T, Long> {
  protected final Map<Long, T> DATA = new HashMap<>();

  private final AtomicLong idGenerator = new AtomicLong();

  @SneakyThrows
  public Long getId(T t) {
    Field idField = findIdField(t);
    idField.setAccessible(true);
    return (Long) idField.get(t);
  }

  @SneakyThrows
  public void setId(T t, Long id) {
    Field idField = findIdField(t);
    idField.setAccessible(true);
    idField.set(t, id);
  }

  public void resetGenerator() {
    idGenerator.set(0);
  }

  @NonNull
  @Override
  public List<T> findAll() {
    return new ArrayList<>(DATA.values());
  }

  @NonNull
  @Override
  public List<T> findAllById(Iterable<Long> ids) {
    return StreamSupport.stream(ids.spliterator(), false)
      .map(this::findById)
      .map(Optional::get)
      .collect(toList());
  }

  @Override
  public long count() {
    return DATA.size();
  }

  @Override
  public void deleteById(@NonNull Long id) {
    DATA.remove(id);
  }

  @Override
  public void delete(@NonNull T t) {
    deleteById(getId(t));
  }

  @Override
  public void deleteAll(Iterable<? extends T> ss) {
    ss.forEach(this::delete);
  }

  @Override
  public void deleteAll() {
    DATA.clear();
  }

  @NonNull
  @Override
  public <S extends T> S save(@NonNull S s) {
    Long id = getId(s);
    if (id == null) {
      setId(s, idGenerator.incrementAndGet());
    }
    DATA.put(getId(s), s);
    return s;
  }

  @NonNull
  @Override
  public <S extends T> List<S> saveAll(@NonNull Iterable<S> ss) {
    return StreamSupport.stream(ss.spliterator(), false)
      .peek(this::save)
      .collect(Collectors.toList());
  }

  @NonNull
  @Override
  public Optional<T> findById(@NonNull Long id) {
    return Optional.ofNullable(DATA.get(id));
  }

  @Override
  public boolean existsById(@NonNull Long id) {
    return DATA.containsKey(id);
  }

  private Field findIdField(T t) {
    Field[] fields = t.getClass().getDeclaredFields();

    for (Field field : fields) {
      Annotation[] fieldAnnotations = field.getDeclaredAnnotations();
      for (Annotation fieldAnnotation : fieldAnnotations) {
        if (fieldAnnotation.toString().equals("@javax.persistence.Id()")) {
          if (field.getType().isAssignableFrom(Long.class)) {
            return field;

          } else {
            throw new RuntimeException("Annotated @Id field type[" + field.getType() + "] is not supported.");
          }
        }
      }
    }

    throw new RuntimeException("No field found annotated with @Id.");
  }
}
