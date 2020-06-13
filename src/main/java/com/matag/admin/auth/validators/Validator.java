package com.matag.admin.auth.validators;

public interface Validator<T> {
  void validate(T field) throws ValidationException;
}
