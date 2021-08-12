package com.homework.social_network.service.message;

import com.homework.social_network.dto.request.PrivateMessageRequestDto;
import com.homework.social_network.dto.response.message.PrivateMessageResponseDto;
import com.homework.social_network.dto.response.message.PrivateMessagesResponseDto;
import com.homework.social_network.entity.message.PrivateMessage;
import com.homework.social_network.repo.PrivateMessageRepository;
import com.homework.social_network.utils.CustomPageable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.homework.social_network.utils.Utils.getCurrentAccountId;

@Service
public class PrivateMessageService {

    @Autowired
    private PrivateMessageRepository privateMessageRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public PrivateMessagesResponseDto getPrivateMessages(UUID targetAccountId, LocalDateTime startLoadTime, int start, int end) {
        UUID currentAccountId = getCurrentAccountId();
        Pageable pageable = new CustomPageable(start, end - start, Sort.by("createDate"));
        LocalDateTime startLoadTimeInternal = Optional.ofNullable(startLoadTime).orElse(LocalDateTime.now());
        List<PrivateMessageResponseDto> messages = privateMessageRepository.getPrivateMessages(currentAccountId, targetAccountId, startLoadTime, pageable)
                .stream()
                .map(m -> modelMapper.map(m, PrivateMessageResponseDto.class))
                .collect(Collectors.toList());
        return PrivateMessagesResponseDto.builder()
                .messages(messages)
                .startLoadTime(startLoadTimeInternal)
                .build();
    }

    @Transactional
    public PrivateMessageResponseDto save(PrivateMessageRequestDto requestDto) {
        PrivateMessage message = modelMapper.map(requestDto, PrivateMessage.class);
        message.setAccepted(false);
        message.setCreateDate(LocalDateTime.now());
        return modelMapper.map(privateMessageRepository.save(message), PrivateMessageResponseDto.class);
    }

    @Transactional
    public void acceptMessages(List<UUID> messageIds) {
        privateMessageRepository.acceptMessages(messageIds);
    }

    @Transactional
    public List<PrivateMessageResponseDto> getUnacceptedMessages() {
        UUID currentAccountId = getCurrentAccountId();
        return privateMessageRepository.getByDestinationIdAndAccepted(currentAccountId, false)
                .stream()
                .map(m -> modelMapper.map(m, PrivateMessageResponseDto.class))
                .collect(Collectors.toList());
    }
}
