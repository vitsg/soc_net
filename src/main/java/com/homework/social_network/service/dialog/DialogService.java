package com.homework.social_network.service.dialog;

import com.homework.social_network.dto.response.account.list.AccountSimpleResponseDto;
import com.homework.social_network.dto.response.dialog.DialogResponseDto;
import com.homework.social_network.dto.response.dialog.DialogsResponseDto;
import com.homework.social_network.dto.response.message.PrivateMessageResponseDto;
import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.message.PrivateMessage;
import com.homework.social_network.projections.DialogProjection;
import com.homework.social_network.repo.PrivateMessageRepository;
import com.homework.social_network.service.AccountService;
import com.homework.social_network.service.message.PrivateMessageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.homework.social_network.utils.Utils.getCurrentAccountId;
import static com.homework.social_network.utils.Utils.getOrDefaultContentPhoto;

@Service
public class DialogService {

    @Autowired
    private PrivateMessageRepository privateMessageRepository;

    @Autowired
    private PrivateMessageService privateMessageService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public DialogsResponseDto getDialogs(int start, int end) {
        UUID currentAccountId = getCurrentAccountId();
        List<DialogProjection> dialogs = privateMessageRepository.getDialogs(currentAccountId, start, end - start);
        Map<UUID, Account> accounts = accountService.getBatch(dialogs.stream()
                .map(DialogProjection::getCompanionId)
                .distinct()
                .collect(Collectors.toList()));
        Map<UUID, PrivateMessage> messages = privateMessageService.getBatch(dialogs.stream()
                .map(DialogProjection::getLastMessageId)
                .distinct()
                .collect(Collectors.toList()));
        return DialogsResponseDto.builder()
                .dialogs(
                        dialogs.stream()
                                .map(d -> {
                                            Account companion = accounts.get(d.getCompanionId());
                                            AccountSimpleResponseDto simple = modelMapper.map(companion, AccountSimpleResponseDto.class);
                                            simple.setPhoto(getOrDefaultContentPhoto(companion.getPhoto()));
                                            return DialogResponseDto.builder()
                                                    .lastMessage(modelMapper.map(messages.get(d.getLastMessageId()), PrivateMessageResponseDto.class))
                                                    .account(simple)
                                                    .countUnacceptedMessages(privateMessageRepository.countBySourceIdAndDestinationIdAndAccepted(d.getCompanionId(), currentAccountId, false))
                                                    .build();
                                        }
                                )
                                .collect(Collectors.toList()))
                .totalSize(privateMessageRepository.countDialogs(currentAccountId))
                .build();
    }

    @Transactional
    public DialogResponseDto getDialog(UUID targetId) {
        UUID currentAccountId = getCurrentAccountId();
        DialogProjection dialog = privateMessageRepository.getDialog(currentAccountId, targetId);
        Account companion = accountService.getByIdWithPhoto(dialog.getCompanionId());
        AccountSimpleResponseDto simple = modelMapper.map(accountService.getByIdWithPhoto(dialog.getCompanionId()), AccountSimpleResponseDto.class);
        simple.setPhoto(getOrDefaultContentPhoto(companion.getPhoto()));

        PrivateMessage message = privateMessageRepository.findById(dialog.getLastMessageId()).orElseThrow();
        PrivateMessageResponseDto messageResponseDto = modelMapper.map(message, PrivateMessageResponseDto.class);

        return DialogResponseDto.builder()
                .account(simple)
                .lastMessage(messageResponseDto)
                .countUnacceptedMessages(privateMessageRepository.countBySourceIdAndDestinationIdAndAccepted(companion.getId(), currentAccountId, false))
                .build();
    }
}
