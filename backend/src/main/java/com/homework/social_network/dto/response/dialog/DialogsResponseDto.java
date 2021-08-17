package com.homework.social_network.dto.response.dialog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DialogsResponseDto {

    private List<DialogResponseDto> dialogs;
    private long totalSize;
}
