package com.zero.paymentprocessor.service.impl;

import com.zero.paymentprocessor.domain.User;
import com.zero.paymentprocessor.dto.JwtDto;
import com.zero.paymentprocessor.dto.UserDto;
import com.zero.paymentprocessor.dto.UserLoginDto;
import com.zero.paymentprocessor.model.MessageModel;
import com.zero.paymentprocessor.model.ResponseModel;
import com.zero.paymentprocessor.repository.UserRepository;
import com.zero.paymentprocessor.service.AuthService;
import com.zero.paymentprocessor.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * This method is used authenticate existing user.
     *
     * @param userLoginDto User login details.
     * @return jwt token.
     */
    @Override
    public ResponseModel login(UserLoginDto userLoginDto) {
        log.info(">> login: username=" + userLoginDto.getUsername());
        Optional<User> username = userRepository.findByUsername(userLoginDto.getUsername());
        if (!username.isPresent()) {
            log.warn("<< login: Not found");
            return new ResponseModel(MessageModel.NOT_FOUND);
        }
        if (encoder.matches(userLoginDto.getPassword(), username.get().getPassword())) {
            log.info("<< login: success");
            return new ResponseModel(MessageModel.SUCCESS, new JwtDto("Bearer", jwtTokenUtil.generateToken(username.get())));
        }
        log.warn("<< login: Authentication failed");
        return new ResponseModel(MessageModel.AUTHENTICATION_FAILED);
    }

    /**
     * This method is used to save new User entity in database.
     *
     * @param userDto The User information to save.
     * @return jwt token.
     */
    @Override
    public ResponseModel register(UserDto userDto) {
        log.info(">> register: username=" + userDto.getUsername() + " phoneNumber=" + userDto.getPhoneNumber());
        if (userRepository.existsByUsername(userDto.getUsername())) {
            log.warn("<< register: Record already exist");
            return new ResponseModel(MessageModel.RECORD_AlREADY_EXIST);
        }
        User user = mapper.map(userDto, User.class);
        encodePassword(user);
        User save = userRepository.save(user);
        log.info("<< register: Success");
        return new ResponseModel(MessageModel.SUCCESS);
    }

    /**
     * This method encodes User password.
     */
    private void encodePassword(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
    }

}
