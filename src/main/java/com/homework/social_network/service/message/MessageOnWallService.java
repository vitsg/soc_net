package com.homework.social_network.service.message;

import com.homework.social_network.dto.request.message.MessageOnWallRequestDto;
import com.homework.social_network.dto.response.account.list.AccountSimpleResponseDto;
import com.homework.social_network.dto.response.message.MessageOnWallFullResponseDto;
import com.homework.social_network.dto.response.message.MessageOnWallResponseDto;
import com.homework.social_network.dto.response.message.MessagesOnWallResponseDto;
import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.message.MessageOnWall;
import com.homework.social_network.repo.MessageOnWallRepository;
import com.homework.social_network.service.AccountService;
import com.homework.social_network.utils.CustomPageable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.homework.social_network.utils.Utils.getOrDefaultContentPhoto;

@Service
public class MessageOnWallService {

    @Autowired
    private MessageOnWallRepository messageOnWallRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccountService accountService;

    @Transactional
    public MessageOnWallResponseDto save(MessageOnWallRequestDto requestDto) {
        MessageOnWall message = modelMapper.map(requestDto, MessageOnWall.class);
        message.setCreateDate(LocalDateTime.now());
        return modelMapper.map(messageOnWallRepository.save(message), MessageOnWallResponseDto.class);
    }

    @Transactional
    public MessagesOnWallResponseDto getMessagesOnWall(UUID targetId, LocalDateTime startLoadTime, int start, int end) {
        Pageable pageable = new CustomPageable(start, end - start, Sort.by(Sort.Direction.DESC, "sendDate"));
        List<MessageOnWall> result = messageOnWallRepository.getMessagesOnWall(targetId, startLoadTime, pageable);
        Map<UUID, Account> accounts = accountService.getBatch(new ArrayList<>(result.stream()
                .map(MessageOnWall::getSourceId)
                .collect(Collectors.toSet())));
        List<MessageOnWallFullResponseDto> messages = result.stream()
                .map(m -> {
                    MessageOnWallFullResponseDto dto = modelMapper.map(m, MessageOnWallFullResponseDto.class);
                    Account account = accounts.get(m.getSourceId());
                    AccountSimpleResponseDto simple = modelMapper.map(account, AccountSimpleResponseDto.class);
                    simple.setPhoto(getOrDefaultContentPhoto(account.getPhoto()));
                    dto.setSource(simple);
                    return dto;
                })
                .collect(Collectors.toList());
        return MessagesOnWallResponseDto.builder()
                .messages(messages)
                .startLoadTime(startLoadTime)
                .totalSize(messageOnWallRepository.getMessagesOnWallCount(targetId, startLoadTime))
                .build();
    }
}
