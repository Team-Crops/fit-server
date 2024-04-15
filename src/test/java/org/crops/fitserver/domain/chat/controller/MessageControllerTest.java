package org.crops.fitserver.domain.chat.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.Schema.schema;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import java.util.List;
import org.crops.fitserver.config.MockMvcDocsWithLogin;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.domain.chat.dto.MessageDto;
import org.crops.fitserver.domain.chat.facade.MessageFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(MessageController.class)
@DisplayName("[Chat][Controller] ChatController Test")
class MessageControllerTest extends MockMvcDocsWithLogin {

  @MockBean
  MessageFacade messageFacade;

  @Nested
  @DisplayName("[GET] GET Message 테스트")
  class GetMessageTest {

    private static final String URL = "/v1/message/room/{roomId}";
    private static final long ROOM_ID = 1L;

    @Nested
    @DisplayName("성공")
    class Success {

      @DisplayName("[200] 메세지 조회 성공")
      @Test
      void getMessage() throws Exception {
        List<MessageDto> messageDtos = List.of(
            MessageDto.builder()
                .userId(1L)
                .messageType(MessageType.TEXT)
                .content("content")
                .build()
        );
        given(
            messageFacade.getMessages(anyLong(), anyLong(), anyInt()))
            .willReturn(messageDtos);
        var result = mockMvc.perform(
            get(URL, ROOM_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(loginPrincipal))
                .with(csrf())
                .queryParam("page", "1")
        );

        result.andExpect(status().isOk())
            .andExpect(handler().handlerType(MessageController.class))
            .andDo(
                document("Recommend User Success",
                    resource(ResourceSnippetParameters.builder()
                        .tag("Recommend")
                        .summary("Recommend User api")
                        .description("팀원 추천 api")
                        .pathParameters(
                            parameterWithName("roomId")
                                .description("방 ID")
                                .defaultValue("1")
                                .type(SimpleType.INTEGER))
                        .queryParameters(
                            parameterWithName("page")
                                .description("페이징 번호")
                                .type(SimpleType.INTEGER))
                        .responseSchema(
                            schema("GetMessageListResponse"))
                        .responseFields(
                            fieldWithPath("messages[].userId")
                                .type(JsonFieldType.NUMBER)
                                .description("User Id"),
                            fieldWithPath("messages[].messageType")
                                .type(JsonFieldType.STRING)
                                .description("User Id"),
                            fieldWithPath("messages[].content")
                                .type(JsonFieldType.STRING)
                                .description("내용"))
                        .build())));
      }
    }
  }

  @Nested
  @DisplayName("성공")
  class Fail {


  }
}