package com.chobutton.back.service;

import com.chobutton.back.config.jwt.TokenProvider;
import com.chobutton.back.dto.UserDTO;
import com.chobutton.back.dto.UserUpdateDTO;
import com.chobutton.back.entity.User;
import com.chobutton.back.entity.UserRole;
import com.chobutton.back.enums.Role;
import com.chobutton.back.exception.BadRequestException;
import com.chobutton.back.repository.UrlRepository;
import com.chobutton.back.repository.UserRepository;
import com.chobutton.back.repository.UserRoleRepository;
import org.apache.ibatis.javassist.tools.web.BadHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
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

    private final TokenProvider tokenProvider;

    private UrlRepository urlRepository;

    private UserRoleRepository userRoleRepository;

    // BCryptPasswordEncoder의 순환참조 문제를 해결하기 위해 @Lazy 어노테이션 사용
    @Autowired
    public UsersServiceImpl(UserRepository userRepository,
                            @Lazy BCryptPasswordEncoder bCryptPasswordEncoder,
                            @Lazy TokenProvider tokenProvider,
                            UrlRepository urlRepository,
                            UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenProvider = tokenProvider;
        this.urlRepository = urlRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Transactional
    @Override
    public UserDTO findById(int id) {
        User user = userRepository.findById(id).orElse(null);
        if(user == null){
            throw new BadRequestException("찾는 유저가 없습니다.");
        }else {
            return fromUserEntityForFind(user);
        }
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
        urlRepository.deleteAllByUserId(id);
        userRoleRepository.deleteAllByUserId(id);
        userRepository.deleteById(id);
    }

    /*
     * password 암호화 인코딩후 DB 적재
     * bCryptPasswordEncoder 사용으로 인해
     * 변환 메서드는 save 메서드 내에서 사용
     */
    @Transactional
    @Override
    public ResponseEntity<String> save(UserDTO signupUser) {
        User findByEmail = userRepository.findByEmail(signupUser.getEmail());
        if(findByEmail == null) {
            User user = User.builder()
                    .email(signupUser.getEmail())
                    .password(bCryptPasswordEncoder.encode(signupUser.getPassword()))
                    .build();
            userRepository.save(user);
            int userId = userRepository.findByEmail(user.getEmail()).getId();
            // 유저가 최초 회원가입시 기본적으로 USER권한 부여
            UserRole userRole = UserRole.builder()
                    .userId(userId)
                    .role(Role.USER)
                    .build();
            userRoleRepository.save(userRole);
            return ResponseEntity.ok().body("회원가입이 정상적으로 완료되었습니다.");
        }else {
            throw new BadRequestException("이미 가입된 사용자 입니다.");
        }
    }

    @Transactional
    @Override
    public ResponseEntity<String> update(UserUpdateDTO userUpdate){
        User user = userRepository.findById(userUpdate.getId()).get();
        // user가 입력한 원password와 DB에 적재된 password를 비교후 true면 수정로직 수행
        if(bCryptPasswordEncoder.matches(userUpdate.getOriginPassword(), user.getPassword())){
            user.updatePassword(bCryptPasswordEncoder.encode(userUpdate.getNewPassword()));
            return ResponseEntity.ok().body("비밀번호가 정상적으로 변경되었습니다.");
        }else {
            throw new BadRequestException("입력하신 기존 비밀번호가 다릅니다.");
        }
    }

    @Transactional
    @Override
    public ResponseEntity<String> loginAuthentication(UserDTO loginUser) {
        // 프론트에서 전달된 로그인 정보중 로그인 아이디로 사용되는 이메일을 통해 DB정보 가져옴
        User userInfo = userRepository.findByEmail(loginUser.getEmail());
        if(userInfo == null){
            throw new BadRequestException("가입이 안된 사용자 입니다.");
        } else if(bCryptPasswordEncoder.matches(loginUser.getPassword(), userInfo.getPassword())) {
            String token = tokenProvider.generateToken(userInfo, Duration.ofHours(1));
            return ResponseEntity.ok(token);
        }else {
            throw new BadRequestException("비밀번호가 틀렸습니다.");
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
