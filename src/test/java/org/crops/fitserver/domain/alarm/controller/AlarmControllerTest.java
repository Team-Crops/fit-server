package org.crops.fitserver.domain.alarm.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.Schema.schema;
import static java.time.OffsetDateTime.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.util.List;
import org.crops.fitserver.config.MockMvcDocsWithLogin;
import org.crops.fitserver.domain.alarm.domain.AlarmCase;
import org.crops.fitserver.domain.alarm.dto.AlarmDto;
import org.crops.fitserver.domain.alarm.dto.response.GetAlarmListResponse;
import org.crops.fitserver.domain.alarm.facade.AlarmFacade;
import org.crops.fitserver.global.http.PageResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(AlarmController.class)
@DisplayName("[Alarm][Controller] AlarmController Test")
class AlarmControllerTest extends MockMvcDocsWithLogin {

  @MockBean
  AlarmFacade alarmFacade;

  @Nested
  @DisplayName("[GET] GET Alarm Test")
  class GetAlarmListTest {

    private static final String URL = "/v1/alarm";

    @Nested
    @DisplayName("성공")
    class Success {

      @DisplayName("[200] 알람 조회 성공")
      @Test
      void getAlarmListSuccess() throws Exception {
        // given
        PageResult<AlarmDto> pageResult = PageResult.of(List.of(
                AlarmDto.builder()
                    .id(1L)
                    .alarmCase(AlarmCase.REPORT)
                    .isRead(false)
                    .createAt(now())
                    .build(),
                AlarmDto.builder()
                    .id(1L)
                    .alarmCase(AlarmCase.NEW_MESSAGE_MATCHING)
                    .isRead(false)
                    .createAt(now())
                    .build()),
            true);
        given(alarmFacade.getAlarmList(anyLong(), anyInt()))
            .willReturn(GetAlarmListResponse.from(pageResult));
        // when
        var result = mockMvc.perform(
            get(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(loginPrincipal))
                .with(csrf())
                .queryParam("page", "0"));

        // then
        result.andExpect(status().isOk())
            .andExpect(handler().handlerType(AlarmController.class))
            .andDo(
                document("get Alarm Success",
                    resource(ResourceSnippetParameters.builder()
                        .tag("Alarm")
                        .summary("get alarm api")
                        .description("알람 조회 api")
                        .queryParameters(
                            parameterWithName("page")
                                .description("페이지 번호")
                                .defaultValue("0"))
                        .responseSchema(
                            schema("GetAlarmListResponse"))
                        .responseFields(
                            fieldWithPath("pageResult.values[].id")
                                .type(NUMBER)
                                .description("Alarm Id"),
                            fieldWithPath("pageResult.values[].alarmCase")
                                .type(STRING)
                                .description(
                                    "FAILED_MATCHING, STARTED_PROJECT, START_PROJECT, NEW_MATCHING_ROOM, "
                                        + "PROGRESS_MATCHING, FORCE_OUT, NEW_MESSAGE_MATCHING, NEW_MESSAGE_PROJECT, REPORT"),
                            fieldWithPath("pageResult.values[].isRead")
                                .type(BOOLEAN)
                                .description("알람 읽음 여부"),
                            fieldWithPath("pageResult.values[].createAt")
                                .type(STRING)
                                .description("알람 생성 시간"),
                            fieldWithPath("pageResult.hasNext")
                                .type(BOOLEAN)
                                .description("true: 다음 페이지 있음, false: 다음 페이지 없음"))
                        .build())));
      }
    }
  }
}