package com.manage.springusermanagement.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN(Set.of(Permissions.WEATHER_WRITE,Permissions.WEATHER_READ,Permissions.WEATHER_DELETE)),
    USER(Set.of(Permissions.WEATHER_READ,Permissions.WEATHER_WRITE)),
    GUEST(Set.of(Permissions.WEATHER_READ));
    private final Set<Permissions> permissions ;

}
