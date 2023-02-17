package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.MessageModel;
import com.fivesysdev.Fiveogram.repositories.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {
    @InjectMocks
    MessageServiceImpl messageService;
    @Mock
    MessageRepository messageRepository;

    @Test
    public void testSave() {
        when(messageRepository.save(any())).thenReturn(null);
        messageService.save(new MessageModel());
        verify(messageRepository, times(1)).save(any());
    }
}