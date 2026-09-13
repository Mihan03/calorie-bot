package com.caloriebot.gateway.handler;

import com.caloriebot.gateway.UserState;
import com.caloriebot.gateway.client.UserServiceClient;
import com.caloriebot.gateway.client.dto.StartConfigureResponseDto;
import com.caloriebot.gateway.screen.Screen;
import com.caloriebot.gateway.screen.StateMap;
import com.caloriebot.gateway.service.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StartConfigureCallbackHandlerImplTest {
    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private StartConfigureCallbackHandlerImpl startConfigureCallbackHandler;

    @Test
    public void shouldShowCurrentScreenWithoutPrefixWhenUserIsNew() {
        Long tgId = 1L;
        Long chatId = 2L;
        StartConfigureResponseDto startConfigureResponseDto = new StartConfigureResponseDto(
                UserState.NEW.name(),
                false
        );

        Screen expectedScreen = StateMap.getScreen(UserState.valueOf(startConfigureResponseDto.userState()));
        String expectedPrefix = "";

        given(userServiceClient.processingStateStartConfigure(tgId))
                .willReturn(startConfigureResponseDto);

        startConfigureCallbackHandler.processStartConfigureHandler(tgId, chatId);

        verify(messageService).getMessageByScreen(expectedScreen, chatId, expectedPrefix);
    }

    @Test
    public void shouldChangeScreenToWaitingWeight() {
        Long tgId = 1L;
        Long chatId = 2L;
        StartConfigureResponseDto startConfigureResponseDto = new StartConfigureResponseDto(
                UserState.WAITING_WEIGHT.name(),
                true
        );

        Screen expectedScreen = StateMap.getScreen(UserState.valueOf(startConfigureResponseDto.userState()));

        given(userServiceClient.processingStateStartConfigure(tgId))
                .willReturn(startConfigureResponseDto);

        startConfigureCallbackHandler.processStartConfigureHandler(tgId, chatId);

        verify(messageService).getMessageByScreen(expectedScreen, chatId);
    }
}
