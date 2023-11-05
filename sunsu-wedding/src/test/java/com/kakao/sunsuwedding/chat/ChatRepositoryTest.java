package com.kakao.sunsuwedding.chat;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ChatRepositoryTest {

    @Autowired
    ChatJPARepository chatJPARepository;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    public void beforeEach() {
        List<Chat> chats = List.of(
                Chat.builder().id(1L).build(),
                Chat.builder().id(2L).build()
        );
        chatJPARepository.saveAll(chats);
    }

    @AfterEach
    public void afterEach() {
        entityManager
                .createNativeQuery("ALTER TABLE chat_tb ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();
    }

    @DisplayName("채팅방 생성 테스트")
    @Test
    public void create_chat_test() {
        // given
        Chat chat = Chat.builder().build();

        // when
        Chat chatPS = chatJPARepository.save(chat);

        // then
        assertThat(chatPS.getId()).isEqualTo(3L);
    }

    @DisplayName("채팅방 조회 테스트")
    @Test
    public void read_chat_test() {
        // given
        Long chatId = 1L;

        // when
        Chat chat = chatJPARepository.findById(chatId).orElseThrow(
                () -> new NotFoundException(BaseException.MATCHING_NOT_FOUND)
        );

        // then
        assertThat(chat.getId()).isEqualTo(1L);
    }
    
    @DisplayName("채팅방 삭제 테스트")
    @Test
    public void delete_chat_test() {
        // given
        Long chatId = 2L;
        long previousCount = chatJPARepository.count();
        Chat chat = chatJPARepository.findById(chatId).orElseThrow(
                () -> new NotFoundException(BaseException.MATCHING_NOT_FOUND)
        );


        // when
        chatJPARepository.delete(chat);
        long newCount = chatJPARepository.count();

        // then
        assertThat(newCount).isEqualTo(previousCount - 1);
    }
}
