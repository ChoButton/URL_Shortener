package com.chobutton.back.service;

import com.chobutton.back.dto.UserDTO;
import com.chobutton.back.dto.UserUpdateDTO;
import com.chobutton.back.entity.User;
import com.chobutton.back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/*
 * BCryptPasswordEncoder의 순환참조 문제를 해결하기 위한
 * @Lazy 어노테이션을 사용하기 위하여
 * @RequiredArgsConstructor 사용하지 않고 생성자 직접 생성
 */
@Service
public class UsersServiceImpl implements UsersService{

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // BCryptPasswordEncoder의 순환참조 문제를 해결하기 위해 @Lazy 어노테이션 사용
    @Autowired
    public UsersServiceImpl(UserRepository userRepository,
                            @Lazy BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    @Override
    public UserDTO findById(int id) {
        User user = userRepository.findById(id).get();
        return fromUserEntityForFind(user);
    }

    // 추후 oAuth2 사용시 사용할 기능
    @Transactional
    @Override
    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return fromUserEntityForFind(user);
    }

    // 관리자용 기능
    @Transactional
    @Override
    public List<UserDTO> findAll() {
        List<User> userList = userRepository.findAll();
        return fromUserEntityForFindAll(userList);
    }

    @Transactional
    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    /*
     * password 암호화 인코딩후 DB 적재
     * bCryptPasswordEncoder 사용으로 인해
     * 변환 메서드는 save 메서드 내에서 사용
     */
    @Transactional
    @Override
    public void save(UserDTO userDTO) {
        User user = User.builder()
                .email(userDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(userDTO.getPassword()))
                .build();
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void update(UserUpdateDTO userUpdateDTO){
        User user = userRepository.findById(userUpdateDTO.getId()).get();
        // user가 입력한 원password와 DB에 적재된 password를 비교후 true면 수정로직 수행
        if(bCryptPasswordEncoder.matches(userUpdateDTO.getOriginPassword(), user.getPassword())){
            user.updatePassword(bCryptPasswordEncoder.encode(userUpdateDTO.getNewPassword()));
        }
    }

    // Entity <-> DTO 변환 메서드
    private static UserDTO fromUserEntityForFind(User user){
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    private static List<UserDTO> fromUserEntityForFindAll(List<User> userList){
        return userList.stream()
                .map(UsersServiceImpl::fromUserEntityForFind)
                .collect(Collectors.toList());
    }

}
