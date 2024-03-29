package com.fivesysdev.Fiveogram.roles;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, Long> {

    @Override
    public Long convertToDatabaseColumn(Role role) {
        if (role == null) {
            return null;
        }
        return role.getCode();
    }

    @Override
    public Role convertToEntityAttribute(Long code) {
        if (code == null) {
            return null;
        }
        return Stream.of(Role.values())
                .filter(c -> code.equals(c.getCode()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}