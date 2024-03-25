package com.vecondev.buildoptima.mapper.user;

import static com.vecondev.buildoptima.model.user.Role.CLIENT;

import com.vecondev.buildoptima.dto.EntityOverview;
import com.vecondev.buildoptima.dto.ImageOverview;
import com.vecondev.buildoptima.dto.user.request.UserRegistrationRequestDto;
import com.vecondev.buildoptima.dto.user.response.UserResponseDto;
import com.vecondev.buildoptima.model.user.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class UserMapperDecorator implements UserMapper {

  @Autowired
  @Qualifier("delegate")
  private UserMapper mapper;

  @Autowired private PasswordEncoder passwordEncoder;

  @Override
  public User mapToEntity(UserRegistrationRequestDto dto) {
    return mapper.mapToEntity(dto).toBuilder()
        .password(passwordEncoder.encode(dto.getPassword()))
        .role(CLIENT)
        .enabled(false)
        .imageVersion(0)
        .build();
  }

  @Override
  public UserResponseDto mapToResponseDto(User user) {
    UserResponseDto responseDto =
        mapper.mapToResponseDto(user).toBuilder().id(user.getId()).build();
    responseDto.setUpdatedAt(user.getUpdatedAt());
    return responseDto;
  }

  public List<UserResponseDto> mapToResponseList(Page<User> users) {
    return mapper.mapToResponseList(users);
  }

  @Override
  public EntityOverview mapToOverview(User user) {
    return mapper.mapToOverview(user).toBuilder()
        .name(String.format("%s %s", user.getFirstName(), user.getLastName()))
        .build();
  }

  @Override
  public ImageOverview mapToUserImageOverview(User user) {
    return mapper.mapToUserImageOverview(user);
  }
}
