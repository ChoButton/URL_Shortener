package com.chobutton.back.service;

import com.chobutton.back.dto.UrlDTO;
import com.chobutton.back.dto.UserRoleDTO;
import com.chobutton.back.entity.Url;
import com.chobutton.back.entity.UserRole;
import com.chobutton.back.enums.Role;
import com.chobutton.back.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.chobutton.back.enums.Role.ADMIN;
import static com.chobutton.back.enums.Role.USER;

@Service
public class UserRoleServiceImpl implements UserRoleService{

    UserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository){
        this.userRoleRepository = userRoleRepository;
    }

    // 유저의 권한정보를 불러올 경우 Enum타입의 role를 Enum안쪽의 getName 메서드를 통해
    // roleName 리스트로 리턴
    @Transactional
    @Override
    public List<String> getRolesName(int userId) {
        List<UserRole> userRoles = userRoleRepository.findAllByUserId(userId);
        return userRoles.stream()
                .map(userRole ->userRole.getRole().getRoleName())
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void save(UserRoleDTO userRoleDTO) {
        // 추후 다른 검증 로직이 추가될 경우를 대비해 정상적이지 않은 권한을 확인하는 if문으로 작성
        if((userRoleDTO.getRole() != USER) && (userRoleDTO.getRole() != ADMIN)){
            throw new DataIntegrityViolationException("허용되지 않는 권한 정보 입니다.");
        }

        UserRole userRole = toEntityForSave(userRoleDTO);

        userRoleRepository.save(userRole);
    }

    @Transactional
    @Override
    public List<UserRoleDTO> findAll() {

        List<UserRole> userRoleList = userRoleRepository.findAll();

        return fromUserRoleEntityForFindAll(userRoleList);
    }

    @Transactional
    @Override
    public List<UserRoleDTO> findAllByUserId(int userId) {

        List<UserRole> userRoleList = userRoleRepository.findAllByUserId(userId);

        return fromUserRoleEntityForFindAll(userRoleList);
    }

    @Transactional
    @Override
    public UserRoleDTO findById(int id) {

        UserRole userRole = userRoleRepository.findById(id).get();

        return fromUserRoleEntityForFind(userRole);
    }

    @Override
    public void deleteById(int id) {
        userRoleRepository.deleteById(id);
    }

    @Override
    public void deleteAllByUserId(int userId) {
        userRoleRepository.deleteAllByUserId(userId);
    }


    // Entity <-> DTO 변환 메서드
    private static UserRoleDTO fromUserRoleEntityForFind(UserRole userRole){
        return UserRoleDTO.builder()
                .userId(userRole.getUserId())
                .role(userRole.getRole())
                .build();
    }

    private static List<UserRoleDTO> fromUserRoleEntityForFindAll(List<UserRole> roleList){
        return roleList.stream()
                .map(UserRoleServiceImpl::fromUserRoleEntityForFind)
                .collect(Collectors.toList());
    }

    private static UserRole toEntityForSave(UserRoleDTO userRoleDTO){
        return UserRole.builder()
                .userId(userRoleDTO.getUserId())
                .role(userRoleDTO.getRole())
                .build();
    }
}
