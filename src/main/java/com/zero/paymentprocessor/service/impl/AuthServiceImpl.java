package com.zero.paymentprocessor.service.impl;

import com.zero.paymentprocessor.domain.User;
import com.zero.paymentprocessor.dto.UserDto;
import com.zero.paymentprocessor.dto.UserLoginDto;
import com.zero.paymentprocessor.model.MessageModel;
import com.zero.paymentprocessor.model.ResponseModel;
import com.zero.paymentprocessor.repository.UserRepository;
import com.zero.paymentprocessor.service.AuthService;
import com.zero.paymentprocessor.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public ResponseModel login(UserLoginDto userLoginDto) {
        Optional<User> username = userRepository.findByUsername(userLoginDto.getUsername());
        if (username.isPresent() && encoder.matches(userLoginDto.getPassword(), username.get().getPassword())) {
            return new ResponseModel(MessageModel.SUCCESS, jwtTokenUtil.generateToken(username.get()));
        }
        return new ResponseModel((MessageModel.AUTHENTICATION_FAILED);
    }

    @Override
    public ResponseModel register(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            return new ResponseModel(MessageModel.RECORD_AlREADY_EXIST);
        }
        User user = mapper.map(userDto, User.class);
        encodePassword(user);
        User save = userRepository.save(user);
        if (save.getId() != null) {
            return new ResponseModel(MessageModel.SUCCESS, jwtTokenUtil.generateToken(save));
        }
        return new ResponseModel(MessageModel.COULD_NOT_SAVE_RECORD);
    }
    private void encodePassword(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
    }

}
