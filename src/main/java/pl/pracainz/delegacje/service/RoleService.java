package pl.pracainz.delegacje.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pracainz.delegacje.model.Role;
import pl.pracainz.delegacje.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    public List<Role> getAll() {
        return roleRepository.findAll();
    }

}
