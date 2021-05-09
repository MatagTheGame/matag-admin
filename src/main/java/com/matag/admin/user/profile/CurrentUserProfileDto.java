package com.matag.admin.user.profile;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = CurrentUserProfileDto.CurrentUserProfileDtoBuilder.class)
@Builder
public class CurrentUserProfileDto {
  String username;
  String type;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CurrentUserProfileDtoBuilder {

  }
}
