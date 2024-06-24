package org.crops.fitserver.domain.chat.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.Schema.schema;
import static java.time.OffsetDateTime.*;
import static org.crops.fitserver.domain.chat.domain.MessageType.TEXT;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.util.ArrayList;
import java.util.List;
import org.crops.fitserver.config.MockMvcDocsWithLogin;
import org.crops.fitserver.domain.chat.dto.response.TextMessageResponse;
import org.crops.fitserver.domain.chat.facade.ChatRoomFacade;
import org.crops.fitserver.global.http.PageResult;
import org.crops.fitserver.global.socket.service.MessageResponse;
import org.crops.fitserver.global.socket.service.SocketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(ChatController.class)
@DisplayName("[Chat][Controller] ChatController Test")
class ChatControllerTest extends MockMvcDocsWithLogin {

  @MockBean
  ChatRoomFacade chatRoomFacade;

  @MockBean
  SocketService socketService;

  @Nested
  @DisplayName("[GET] GET Message 테스트")
  class GetMessageTest {

    private static final String URL = "/v1/chat/room/{roomId}/message";
    private static final long ROOM_ID = 1L;

    @Nested
    @DisplayName("성공")
    class Success {

      @DisplayName("[200] 텍스트 메세지 조회 성공")
      @Test
      void getMessage() throws Exception {
        List<MessageResponse> messageResponse = new ArrayList<>(List.of(
            TextMessageResponse.builder().messageId(1L).messageType(TEXT).userId(1L).content("content").createdAt(now()).build(),
            TextMessageResponse.builder().messageId(2L).messageType(TEXT).userId(1L).content("content").createdAt(now()).build(),
            TextMessageResponse.builder().messageId(3L).messageType(TEXT).userId(1L).content("content").createdAt(now()).build()));
        PageResult<MessageResponse> pageResult = PageResult.of(messageResponse,
            true);
        given(
            chatRoomFacade.getMessages(anyLong(), anyLong(), anyLong()))
            .willReturn(pageResult);
        var result = mockMvc.perform(
            get(URL, ROOM_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(loginPrincipal))
                .with(csrf())
                .queryParam("lastMessageId", "1")
        );

        result.andExpect(status().isOk())
            .andExpect(handler().handlerType(ChatController.class))
            .andDo(
                document("get Message Success",
                    resource(ResourceSnippetParameters.builder()
                        .tag("Chat")
                        .summary("get message api")
                        .description("방 메세지 조회 api")
                        .pathParameters(
                            parameterWithName("roomId")
                                .description("방 ID")
                                .defaultValue("1"))
                        .queryParameters(
                            parameterWithName("lastMessageId")
                                .description("해당 id 이후의 메세지 페이징 조회")
                                .defaultValue("1"))
                        .responseSchema(
                            schema("GetMessageListResponse"))
                        .responseFields(
                            fieldWithPath("pageResult.values[].messageId")
                                .type(NUMBER)
                                .description("Message Id"),
                            fieldWithPath("pageResult.values[].messageType")
                                .type(STRING)
                                .description("TEXT, IMAGE, NOTICE 중 하나"),
                            fieldWithPath("pageResult.values[].userId")
                                .type(NUMBER)
                                .description("TEXT, IMAGE일 경우에만 존재"),
                            fieldWithPath("pageResult.values[].content")
                                .type(STRING)
                                .description("TEXT일 경우에만 존재"),
                            fieldWithPath("pageResult.values[].createdAt")
                                .type(STRING)
                                .description("메세지 생성 시간"),
                            fieldWithPath("pageResult.hasNext")
                                .type(BOOLEAN)
                                .description("true: 다음 페이지 있음, false: 다음 페이지 없음"))
                        .build())));
      }

    }
  }

  @Nested
  @DisplayName("[GET] GET Last Seen Message Id 테스트")
  class GetLastSeenMessageTest {

    private static final String URL = "/v1/chat/room/{roomId}/message/recent";
    private static final long ROOM_ID = 1L;

    @Nested
    @DisplayName("성공")
    class Success {

      @DisplayName("[200] 최근 읽은 메세지 id 조회 성공")
      @Test
      void getLastSeenMessage() throws Exception {
        Long messageId = 1L;
        given(
            chatRoomFacade.getRecentMessageId(anyLong(), anyLong()))
            .willReturn(messageId);
        var result = mockMvc.perform(
            get(URL, ROOM_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(loginPrincipal))
                .with(csrf()));

        result.andExpect(status().isOk())
            .andExpect(handler().handlerType(ChatController.class))
            .andDo(
                document("get Last Seen Message Success",
                    resource(ResourceSnippetParameters.builder()
                        .tag("Chat")
                        .summary("get last seen message api")
                        .description("최근 읽은 메세지 조회 api")
                        .pathParameters(
                            parameterWithName("roomId")
                                .description("방 ID")
                                .defaultValue("1"))
                        .responseSchema(
                            schema("GetLastSeenMessageResponse"))
                        .responseFields(
                            fieldWithPath("messageId")
                                .type(NUMBER)
                                .description("Message Id"))
                        .build())));
      }
    }
  }
}