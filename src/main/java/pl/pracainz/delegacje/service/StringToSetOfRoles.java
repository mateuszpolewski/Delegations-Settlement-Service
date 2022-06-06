/*
package pl.pracainz.delegacje.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.pracainz.delegacje.model.Role;
import pl.pracainz.delegacje.repository.RoleRepository;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class StringToSetOfRoles implements Converter<String, Set> {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public HashSet<Role> convert(String role) {
        System.out.println("TRYING TO CONVERT");
        System.out.println("TRYING TO CONVERT");
        Role userRole = roleRepository.findByRole(role);
        return new HashSet<Role>(Arrays.asList(userRole));
    }
}
*/