package org.crops.fitserver.domain.user.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.crops.fitserver.domain.user.util.PrincipalDetailsUtil.getPrincipalDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.EnumFields;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.region.domain.Region;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.skillset.domain.Skill;
import org.crops.fitserver.domain.user.constant.BackgroundStatus;
import org.crops.fitserver.domain.user.constant.LinkType;
import org.crops.fitserver.domain.user.constant.PolicyType;
import org.crops.fitserver.domain.user.constant.UserInfoStatus;
import org.crops.fitserver.domain.user.domain.Link;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.domain.UserInfo;
import org.crops.fitserver.domain.user.domain.UserRole;
import org.crops.fitserver.domain.user.dto.PolicyAgreementDto;
import org.crops.fitserver.domain.user.dto.UserInfoDto;
import org.crops.fitserver.domain.user.dto.request.UpdatePolicyAgreementRequest;
import org.crops.fitserver.domain.user.dto.request.UpdateUserRequest;
import org.crops.fitserver.domain.user.facade.UserFacade;
import org.crops.fitserver.util.MockMvcDocs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@WebMvcTest(UserController.class)
@Slf4j
class UserControllerTest extends MockMvcDocs {

  @MockBean
  private UserFacade userFacade;

  @Override
  @BeforeEach
  protected void setUp(RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(springSecurity())
        .apply(
            documentationConfiguration(restDocumentation)
                .operationPreprocessors()
                .withResponseDefaults(Preprocessors.prettyPrint())
        )
        .alwaysDo(print())
        .build();
  }

  @Test
  void getUser_success() throws Exception {
    // given
    var linkList = List.of(
        Link.builder().linkType(LinkType.GITHUB).linkUrl("github.com").build(),
        Link.builder().linkType(LinkType.ETC).linkUrl("test.com").build()
    );
    var userInfo = UserInfo.builder()
        .id(1L)
        .linkJson(objectMapper.writeValueAsString(linkList))
        .build();
    var user = User.builder().id(1L).userRole(UserRole.MEMBER).userInfo(userInfo).build();
    var principalDetails = getPrincipalDetails(user.getId(), user.getUserRole());
    var url = "/v1/user";
    given(userFacade.getUserWithInfo(user.getId())).willReturn(UserInfoDto.from(user));
    // when
    var result = mockMvc.perform(get(url)
        .contentType(MediaType.APPLICATION_JSON)
        .with(user(principalDetails))
        .with(csrf())
    );

    // then
    result.andExpect(status().isOk())
        .andDo(document("user/getUser",
            resource(
                ResourceSnippetParameters.builder()
                    .tag("user")
                    .description("사용자 정보 조회")
                    .summary("사용자 정보 조회")
                    .responseSchema(Schema.schema("UserInfoDto"))
                    .responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 id"),
                        fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
                            .description("프로필 이미지 url").optional(),
                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자 이름")
                            .optional(),
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("사용자 닉네임")
                            .optional(),
                        fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                            .description("사용자 전화번호").optional(),
                        fieldWithPath("isOpenPhoneNum").type(JsonFieldType.BOOLEAN)
                            .description("전화번호 공개 여부").optional(),
                        fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일")
                            .optional(),
                        new EnumFields(BackgroundStatus.class).withPath("backgroundStatus")
                            .description("학력/경력 상태").optional(),
                        fieldWithPath("backgroundText").type(JsonFieldType.STRING)
                            .description("학력/경력 텍스트").optional(),
                        fieldWithPath("isOpenProfile").type(JsonFieldType.BOOLEAN)
                            .description("프로필 공개 여부").optional(),
                        fieldWithPath("status").type(JsonFieldType.STRING).description("사용자 정보 상태")
                            .optional(),
                        fieldWithPath("portfolioUrl").type(JsonFieldType.STRING)
                            .description("포트폴리오 url").optional(),
                        fieldWithPath("projectCount").type(JsonFieldType.NUMBER)
                            .description("프로젝트 수").optional(),
                        fieldWithPath("activityHour").type(JsonFieldType.NUMBER)
                            .description("활동 시간").optional(),
                        fieldWithPath("introduce").type(JsonFieldType.STRING).description("자기소개")
                            .optional(),
                        fieldWithPath("linkList").type(JsonFieldType.ARRAY).description("링크 list")
                            .optional(),
                        fieldWithPath("linkList[].linkUrl").type(JsonFieldType.STRING)
                            .description("링크 url").optional(),
                        fieldWithPath("linkList[].linkType").type(JsonFieldType.STRING)
                            .description("링크 타입").optional(),
                        fieldWithPath("skillIdList").type(JsonFieldType.ARRAY)
                            .description("스킬 id list").optional(),
                        fieldWithPath("positionId").type(JsonFieldType.NUMBER).description("직군 id")
                            .optional(),
                        fieldWithPath("regionId").type(JsonFieldType.NUMBER).description("지역 id")
                            .optional()
                    )
                    .build()
            )
        ));
  }

  @Test
  void updateUser_success_minimum_insert() throws Exception {
    // given
    var url = "/v1/user";
    var userInfo = UserInfo.builder()
        .id(1L)
        .build();
    var user = User.builder().id(1L).userRole(UserRole.MEMBER).userInfo(userInfo).build();

    var updateUserRequest = UpdateUserRequest.builder()
        .isOpenPhoneNum(true)
        .isOpenProfile(true)
        .build();

    var newUserInfo = UserInfo.builder()
        .id(1L)
        .user(user)
        .isOpenProfile(true)
        .status(UserInfoStatus.INCOMPLETE)
        .build();

    var newUser = User.builder()
        .id(1L)
        .userRole(UserRole.MEMBER)
        .isOpenPhoneNum(true)
        .userInfo(newUserInfo)
        .build();

    given(userFacade.updateUserWithInfo(any(), any())).willReturn(UserInfoDto.from(newUser));
    var principalDetails = getPrincipalDetails(1L, UserRole.MEMBER);

    // when
    var result = mockMvc.perform(patch(url)
        .contentType(MediaType.APPLICATION_JSON)
        .with(user(principalDetails))
        .with(csrf())
        .content("{" +
            "\"isOpenPhoneNum\": true," +
            "\"isOpenProfile\": true" +
            "}"
        )
    );

    // then
    result.andExpect(status().isOk())
        .andDo(document("user/updateUser",
            resource(
                ResourceSnippetParameters.builder()
                    .tag("user")
                    .description("사용자 정보 수정")
                    .summary("사용자 정보 수정")
                    .requestSchema(Schema.schema("UpdateUserRequest"))
                    .responseSchema(Schema.schema("UserInfoDto"))
                    .requestFields(
                        fieldWithPath("isOpenPhoneNum").type(JsonFieldType.BOOLEAN)
                            .description("전화번호 공개 여부").optional(),
                        fieldWithPath("isOpenProfile").type(JsonFieldType.BOOLEAN)
                            .description("프로필 공개 여부").optional()
                    )
                    .responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 id"),
                        fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
                            .description("프로필 이미지 url").optional(),
                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자 이름")
                            .optional(),
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("사용자 닉네임")
                            .optional(),
                        fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                            .description("사용자 전화번호").optional(),
                        fieldWithPath("isOpenPhoneNum").type(JsonFieldType.BOOLEAN)
                            .description("전화번호 공개 여부").optional(),
                        fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일")
                            .optional(),
                        new EnumFields(BackgroundStatus.class).withPath("backgroundStatus")
                            .description("학력/경력 상태").optional(),
                        fieldWithPath("backgroundText").type(JsonFieldType.STRING)
                            .description("학력/경력 텍스트").optional(),
                        fieldWithPath("isOpenProfile").type(JsonFieldType.BOOLEAN)
                            .description("프로필 공개 여부").optional(),
                        fieldWithPath("portfolioUrl").type(JsonFieldType.STRING)
                            .description("포트폴리오 url").optional(),
                        fieldWithPath("projectCount").type(JsonFieldType.NUMBER)
                            .description("프로젝트 수").optional(),
                        fieldWithPath("activityHour").type(JsonFieldType.NUMBER)
                            .description("활동 시간").optional(),
                        fieldWithPath("introduce").type(JsonFieldType.STRING).description("자기소개")
                            .optional(),
                        fieldWithPath("linkList").description("링크 list")
                            .optional(),
                        fieldWithPath("positionId").type(JsonFieldType.NUMBER).description("직군 id")
                            .optional(),
                        fieldWithPath("regionId").type(JsonFieldType.NUMBER).description("지역 id")
                            .optional(),
                        fieldWithPath("skillIdList").description("스킬 id list").optional(),
                        fieldWithPath("status").type(JsonFieldType.STRING).description("사용자 정보 상태")
                    )
                    .build()
            )
        ));
  }

  @Test
  void updateUser_success_1() throws Exception {
    // given
    var url = "/v1/user";
    var userInfo = UserInfo.builder()
        .id(1L)
        .build();
    var user = User.builder().id(1L).userRole(UserRole.MEMBER).userInfo(userInfo).build();

    var updateUserRequest = UpdateUserRequest.builder()
        .isOpenPhoneNum(true)
        .isOpenProfile(true)
        .build();

    var newUserInfo = UserInfo.builder()
        .id(1L)
        .user(user)
        .isOpenProfile(true)
        .status(UserInfoStatus.INCOMPLETE)
        .build();

    var newUser = User.builder()
        .id(1L)
        .userRole(UserRole.MEMBER)
        .isOpenPhoneNum(true)
        .userInfo(newUserInfo)
        .build();

    given(userFacade.updateUserWithInfo(any(), any())).willReturn(UserInfoDto.from(newUser));
    var principalDetails = getPrincipalDetails(1L, UserRole.MEMBER);

    // when
    var result = mockMvc.perform(patch(url)
        .contentType(MediaType.APPLICATION_JSON)
        .with(user(principalDetails))
        .with(csrf())
        .content(objectMapper.writeValueAsString(updateUserRequest))
    );

    // then
    result.andExpect(status().isOk())
        .andDo(document("user/updateUser",
            resource(
                ResourceSnippetParameters.builder()
                    .tag("user")
                    .description("사용자 정보 수정")
                    .summary("사용자 정보 수정")
                    .requestSchema(Schema.schema("UpdateUserRequest"))
                    .responseSchema(Schema.schema("UserInfoDto"))
                    .requestFields(
                        fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
                            .description("프로필 이미지 url").optional(),
                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자 이름")
                            .optional(),
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("사용자 닉네임")
                            .optional(),
                        fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                            .description("사용자 전화번호").optional(),
                        fieldWithPath("isOpenPhoneNum").type(JsonFieldType.BOOLEAN)
                            .description("전화번호 공개 여부").optional(),
                        fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일")
                            .optional(),
                        new EnumFields(BackgroundStatus.class).withPath("backgroundStatus")
                            .description("학력/경력 상태").optional(),
                        fieldWithPath("backgroundText").type(JsonFieldType.STRING)
                            .description("학력/경력 텍스트").optional(),
                        fieldWithPath("isOpenProfile").type(JsonFieldType.BOOLEAN)
                            .description("프로필 공개 여부").optional(),
                        fieldWithPath("portfolioUrl").type(JsonFieldType.STRING)
                            .description("포트폴리오 url").optional(),
                        fieldWithPath("projectCount").type(JsonFieldType.NUMBER)
                            .description("프로젝트 수").optional(),
                        fieldWithPath("activityHour").type(JsonFieldType.NUMBER)
                            .description("활동 시간").optional(),
                        fieldWithPath("introduce").type(JsonFieldType.STRING).description("자기소개")
                            .optional(),
                        fieldWithPath("linkList").description("링크 list").optional(),
                        fieldWithPath("skillIdList").description("스킬 id list").optional(),
                        fieldWithPath("positionId").type(JsonFieldType.NUMBER).description("직군 id")
                            .optional(),
                        fieldWithPath("regionId").type(JsonFieldType.NUMBER).description("지역 id")
                            .optional()
                    )
                    .responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 id"),
                        fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
                            .description("프로필 이미지 url").optional(),
                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자 이름")
                            .optional(),
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("사용자 닉네임")
                            .optional(),
                        fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                            .description("사용자 전화번호").optional(),
                        fieldWithPath("isOpenPhoneNum").type(JsonFieldType.BOOLEAN)
                            .description("전화번호 공개 여부").optional(),
                        fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일")
                            .optional(),
                        new EnumFields(BackgroundStatus.class).withPath("backgroundStatus")
                            .description("학력/경력 상태").optional(),
                        fieldWithPath("backgroundText").type(JsonFieldType.STRING)
                            .description("학력/경력 텍스트").optional(),
                        fieldWithPath("isOpenProfile").type(JsonFieldType.BOOLEAN)
                            .description("프로필 공개 여부").optional(),
                        fieldWithPath("portfolioUrl").type(JsonFieldType.STRING)
                            .description("포트폴리오 url").optional(),
                        fieldWithPath("projectCount").type(JsonFieldType.NUMBER)
                            .description("프로젝트 수").optional(),
                        fieldWithPath("activityHour").type(JsonFieldType.NUMBER)
                            .description("활동 시간").optional(),
                        fieldWithPath("introduce").type(JsonFieldType.STRING).description("자기소개")
                            .optional(),
                        fieldWithPath("linkList").description("링크 list")
                            .optional(),
                        fieldWithPath("positionId").type(JsonFieldType.NUMBER).description("직군 id")
                            .optional(),
                        fieldWithPath("regionId").type(JsonFieldType.NUMBER).description("지역 id")
                            .optional(),
                        fieldWithPath("skillIdList").description("스킬 id list").optional(),
                        fieldWithPath("status").type(JsonFieldType.STRING).description("사용자 정보 상태")
                    )
                    .build()
            )
        ));
  }

  @Test
  void updateUser_success() throws Exception {
    // given
    var url = "/v1/user";
    var userInfo = UserInfo.builder()
        .id(1L)
        .build();
    var user = User.builder().id(1L).userRole(UserRole.MEMBER).userInfo(userInfo).build();

    var linkList = List.of(
        Link.builder().linkType(LinkType.GITHUB).linkUrl("github.com").build(),
        Link.builder().linkType(LinkType.ETC).linkUrl("test.com").build()
    );
    var skillIdList = List.of(1L, 2L, 3L);

    var updateUserRequest = UpdateUserRequest.builder()
        .profileImageUrl("test.com")
        .username("test")
        .nickname("test")
        .phoneNumber("010-1234-1234")
        .isOpenPhoneNum(true)
        .email("test@gmail.com")
        .portfolioUrl("test.com")
        .projectCount(1)
        .activityHour(1)
        .introduce("test")
        .isOpenProfile(true)
        .positionId(1L)
        .regionId(1L)
        .linkList(linkList)
        .skillIdList(skillIdList)
        .build();

    var newUserInfo = UserInfo.builder()
        .id(1L)
        .user(user)
        .portfolioUrl("test.com")
        .projectCount(1)
        .activityHour(1)
        .introduce("test")
        .isOpenProfile(true)
        .position(Position.builder().id(1L).build())
        .region(Region.builder().id(1L).build())
        .linkJson(objectMapper.writeValueAsString(linkList))
        .status(UserInfoStatus.INCOMPLETE)
        .build();
    newUserInfo.addSkill(Skill.builder().id(1L).build());
    newUserInfo.addSkill(Skill.builder().id(2L).build());
    newUserInfo.addSkill(Skill.builder().id(3L).build());

    var newUser = User.builder()
        .id(1L)
        .userRole(UserRole.MEMBER)
        .profileImageUrl("test.com")
        .username("test")
        .nickname("test")
        .phoneNumber("010-1234-1234")
        .isOpenPhoneNum(true)
        .userInfo(newUserInfo)
        .email("test@gmail.com")
        .build();

    given(userFacade.updateUserWithInfo(any(), any())).willReturn(UserInfoDto.from(newUser));
    var principalDetails = getPrincipalDetails(1L, UserRole.MEMBER);

    // when

    var result = mockMvc.perform(patch(url)
        .contentType(MediaType.APPLICATION_JSON)
        .with(user(principalDetails))
        .with(csrf())
        .content(objectMapper.writeValueAsString(updateUserRequest))
    );

    // then
    result.andExpect(status().isOk())
        .andDo(document("user/updateUser",
            resource(
                ResourceSnippetParameters.builder()
                    .tag("user")
                    .description("사용자 정보 수정")
                    .summary("사용자 정보 수정")
                    .requestSchema(Schema.schema("UpdateUserRequest"))
                    .responseSchema(Schema.schema("UserInfoDto"))
                    .requestFields(
                        fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
                            .description("프로필 이미지 url").optional(),
                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자 이름")
                            .optional(),
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("사용자 닉네임")
                            .optional(),
                        fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                            .description("사용자 전화번호").optional(),
                        fieldWithPath("isOpenPhoneNum").type(JsonFieldType.BOOLEAN)
                            .description("전화번호 공개 여부").optional(),
                        fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일")
                            .optional(),
                        new EnumFields(BackgroundStatus.class).withPath("backgroundStatus")
                            .description("학력/경력 상태").optional(),
                        fieldWithPath("backgroundText").type(JsonFieldType.STRING)
                            .description("학력/경력 텍스트").optional(),
                        fieldWithPath("isOpenProfile").type(JsonFieldType.BOOLEAN)
                            .description("프로필 공개 여부").optional(),
                        fieldWithPath("portfolioUrl").type(JsonFieldType.STRING)
                            .description("포트폴리오 url").optional(),
                        fieldWithPath("projectCount").type(JsonFieldType.NUMBER)
                            .description("프로젝트 수").optional(),
                        fieldWithPath("activityHour").type(JsonFieldType.NUMBER)
                            .description("활동 시간").optional(),
                        fieldWithPath("introduce").type(JsonFieldType.STRING).description("자기소개")
                            .optional(),
                        fieldWithPath("linkList[]").description("링크 list").optional(),
                        fieldWithPath("linkList[].linkUrl").type(JsonFieldType.STRING)
                            .description("링크 url").optional(),
                        fieldWithPath("linkList[].linkType").type(JsonFieldType.STRING)
                            .description("링크 타입").optional(),
                        fieldWithPath("skillIdList").description("스킬 id list").optional(),
                        fieldWithPath("positionId").type(JsonFieldType.NUMBER).description("직군 id")
                            .optional(),
                        fieldWithPath("regionId").type(JsonFieldType.NUMBER).description("지역 id")
                            .optional()
                    )
                    .responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 id"),
                        fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
                            .description("프로필 이미지 url").optional(),
                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자 이름")
                            .optional(),
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("사용자 닉네임")
                            .optional(),
                        fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                            .description("사용자 전화번호").optional(),
                        fieldWithPath("isOpenPhoneNum").type(JsonFieldType.BOOLEAN)
                            .description("전화번호 공개 여부").optional(),
                        fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일")
                            .optional(),
                        new EnumFields(BackgroundStatus.class).withPath("backgroundStatus")
                            .description("학력/경력 상태").optional(),
                        fieldWithPath("backgroundText").type(JsonFieldType.STRING)
                            .description("학력/경력 텍스트").optional(),
                        fieldWithPath("isOpenProfile").type(JsonFieldType.BOOLEAN)
                            .description("프로필 공개 여부").optional(),
                        fieldWithPath("portfolioUrl").type(JsonFieldType.STRING)
                            .description("포트폴리오 url").optional(),
                        fieldWithPath("projectCount").type(JsonFieldType.NUMBER)
                            .description("프로젝트 수").optional(),
                        fieldWithPath("activityHour").type(JsonFieldType.NUMBER)
                            .description("활동 시간").optional(),
                        fieldWithPath("introduce").type(JsonFieldType.STRING).description("자기소개")
                            .optional(),
                        fieldWithPath("linkList[]").description("링크 list").optional(),
                        fieldWithPath("linkList[].linkUrl").type(JsonFieldType.STRING)
                            .description("링크 url").optional(),
                        fieldWithPath("linkList[].linkType").type(JsonFieldType.STRING)
                            .description("링크 타입").optional(),
                        fieldWithPath("positionId").type(JsonFieldType.NUMBER).description("직군 id")
                            .optional(),
                        fieldWithPath("regionId").type(JsonFieldType.NUMBER).description("지역 id")
                            .optional(),
                        fieldWithPath("skillIdList").description("스킬 id list").optional(),
                        fieldWithPath("status").type(JsonFieldType.STRING).description("사용자 정보 상태")
                    )
                    .build()
            )
        ));
  }

  @Test
  void getPolicyAgreement() throws Exception {
    //given
    var url = "/v1/user/policy-agreement";
    var policyAgreementDtoList = List.of(
        PolicyAgreementDto.builder()
            .policyType(PolicyType.PRIVACY_POLICY)
            .version("1.0.0")
            .isAgree(true)
            .build(),
        PolicyAgreementDto.builder()
            .policyType(PolicyType.PRIVACY_POLICY)
            .version("1.0.0")
            .isAgree(true)
            .build()
    );

    var principalDetails = getPrincipalDetails(1L, UserRole.MEMBER);
    given(userFacade.getPolicyAgreementList(any())).willReturn(policyAgreementDtoList);

    //when
    var result = mockMvc.perform(get(url)
        .contentType(MediaType.APPLICATION_JSON)
        .with(user(principalDetails))
        .with(csrf())
    );
    //then
    result.andExpect(status().isOk())
        .andDo(document("user/getPolicyAgreement",
            resource(
                ResourceSnippetParameters.builder()
                    .tag("user")
                    .description("개인정보 동의 조회")
                    .summary("개인정보 동의 조회")
                    .requestFields()
                    .responseSchema(Schema.schema("PolicyAgreementDto"))
                    .responseFields(
                        fieldWithPath("policyAgreementList[]").description("개인정보 동의 list")
                            .optional(),
                        new EnumFields(PolicyType.class).withPath(
                                "policyAgreementList[].policyType")
                            .description("개인정보 동의 타입"),
                        fieldWithPath("policyAgreementList[].version").type(JsonFieldType.STRING)
                            .description("개인정보 동의 버전"),
                        fieldWithPath("policyAgreementList[].isAgree").type(JsonFieldType.BOOLEAN)
                            .description("개인정보 동의 여부"),
                        fieldWithPath("policyAgreementList[].updatedAt").type(JsonFieldType.NULL)
                            .description("개인정보 동의 업데이트 시간")
                            .optional()
                    )
                    .build()
            )
        ));
  }

  @Test
  void updatePolicyAgreement_failed_빈값() throws Exception {
    var url = "/v1/user/policy-agreement";
    var policyAgreementDtoList = List.of(
        PolicyAgreementDto.builder()
            .policyType(PolicyType.PRIVACY_POLICY)
            .version("1.0.0")
            .isAgree(null)
            .build()
    );
    var updatePolicyAgreementRequest = new UpdatePolicyAgreementRequest(policyAgreementDtoList);
    var principalDetails = getPrincipalDetails(1L, UserRole.MEMBER);

    //when
    var result = mockMvc.perform(put(url)
        .contentType(MediaType.APPLICATION_JSON)
        .with(user(principalDetails))
        .with(csrf())
        .content(objectMapper.writeValueAsString(updatePolicyAgreementRequest))
    );

    //then
    result.andExpect(status().isBadRequest())
        .andDo(document("user/updatePolicyAgreement",
            resource(
                ResourceSnippetParameters.builder()
                    .tag("user")
                    .description("개인정보 동의")
                    .summary("개인정보 동의")
                    .requestSchema(Schema.schema("UpdatePolicyAgreementRequest"))
                    .responseSchema(Schema.schema("errorResponse"))
                    .requestFields(
                        fieldWithPath("policyAgreementList[]").description("개인정보 동의 list")
                            .optional(),
                        new EnumFields(PolicyType.class).withPath(
                                "policyAgreementList[].policyType")
                            .description("개인정보 동의 타입"),
                        fieldWithPath("policyAgreementList[].version").type(JsonFieldType.STRING)
                            .description("개인정보 동의 버전"),
                        fieldWithPath("policyAgreementList[].isAgree").type(JsonFieldType.NULL)
                            .description("개인정보 동의 여부"),
                        fieldWithPath("policyAgreementList[].updatedAt").type(JsonFieldType.NULL)
                            .description("무시해주세요!!! 문서 자동화로 인해 추가된 필드입니다. 실제로는 필요없습니다.")
                            .optional()
                    )
                    .responseFields(
                        fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING)
                            .description("에러 메시지")
                    )
                    .build()
            )
        ));

  }

  @Test
  void updatePolicyAgreement_failed_중복() throws Exception {
    //given
    var url = "/v1/user/policy-agreement";
    var policyAgreementDtoList = List.of(
        PolicyAgreementDto.builder()
            .policyType(PolicyType.PRIVACY_POLICY)
            .version("1.0.0")
            .isAgree(true)
            .build(),
        PolicyAgreementDto.builder()
            .policyType(PolicyType.PRIVACY_POLICY)
            .version("1.0.0")
            .isAgree(true)
            .build()
    );
    var updatePolicyAgreementRequest = new UpdatePolicyAgreementRequest(policyAgreementDtoList);
    var principalDetails = getPrincipalDetails(1L, UserRole.MEMBER);
    given(userFacade.updatePolicyAgreement(any(), any())).willReturn(policyAgreementDtoList);

    //when
    var result = mockMvc.perform(put(url)
        .contentType(MediaType.APPLICATION_JSON)
        .with(user(principalDetails))
        .with(csrf())
        .content(objectMapper.writeValueAsString(updatePolicyAgreementRequest))
    );
    //then
    result.andExpect(status().isBadRequest())
        .andDo(document("user/updatePolicyAgreement",
            resource(
                ResourceSnippetParameters.builder()
                    .tag("user")
                    .description("개인정보 동의")
                    .summary("개인정보 동의")
                    .requestSchema(Schema.schema("UpdatePolicyAgreementRequest"))
                    .responseSchema(Schema.schema("errorResponse"))
                    .requestFields(
                        fieldWithPath("policyAgreementList[]").description("개인정보 동의 list")
                            .optional(),
                        new EnumFields(PolicyType.class).withPath(
                                "policyAgreementList[].policyType")
                            .description("개인정보 동의 타입"),
                        fieldWithPath("policyAgreementList[].version").type(JsonFieldType.STRING)
                            .description("개인정보 동의 버전"),
                        fieldWithPath("policyAgreementList[].isAgree").type(JsonFieldType.BOOLEAN)
                            .description("개인정보 동의 여부"),
                        fieldWithPath("policyAgreementList[].updatedAt").type(JsonFieldType.NULL)
                            .description("무시해주세요!!! 문서 자동화로 인해 추가된 필드입니다. 실제로는 필요없습니다.")
                            .optional()
                    )
                    .responseFields(
                        fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING)
                            .description("에러 메시지")
                    )
                    .build()
            )
        ));
  }

  @Test
  void updatePolicyAgreement_success() throws Exception {
    //given
    var url = "/v1/user/policy-agreement";
    var policyAgreementDtoList = List.of(
        PolicyAgreementDto.builder()
            .policyType(PolicyType.PRIVACY_POLICY)
            .version("1.0.0")
            .isAgree(true)
            .build(),
        PolicyAgreementDto.builder()
            .policyType(PolicyType.SERVICE_POLICY)
            .version("1.0.0")
            .isAgree(true)
            .build()
    );
    var newPolicyAgreementDtoList = List.of(
        PolicyAgreementDto.builder()
            .policyType(PolicyType.PRIVACY_POLICY)
            .version("1.0.0")
            .isAgree(true)
            .updatedAt("2021-08-10T00:00:00.000+00:00")
            .build(),
        PolicyAgreementDto.builder()
            .policyType(PolicyType.PRIVACY_POLICY)
            .version("1.0.0")
            .isAgree(true)
            .updatedAt("2021-08-10T00:00:00.000+00:00")
            .build()
    );
    var updatePolicyAgreementRequest = new UpdatePolicyAgreementRequest(policyAgreementDtoList);
    var principalDetails = getPrincipalDetails(1L, UserRole.MEMBER);
    given(userFacade.updatePolicyAgreement(any(), any())).willReturn(
        newPolicyAgreementDtoList);

    //when
    var result = mockMvc.perform(put(url)
        .contentType(MediaType.APPLICATION_JSON)
        .with(user(principalDetails))
        .with(csrf())
        .content(objectMapper.writeValueAsString(updatePolicyAgreementRequest))
    );
    //then
    result.andExpect(status().isOk())
        .andDo(document("user/updatePolicyAgreement",
            resource(
                ResourceSnippetParameters.builder()
                    .tag("user")
                    .description("개인정보 동의")
                    .summary("개인정보 동의")
                    .requestSchema(Schema.schema("UpdatePolicyAgreementRequest"))
                    .responseSchema(Schema.schema("UpdatePolicyAgreementResponse"))
                    .requestFields(
                        fieldWithPath("policyAgreementList[]").description("개인정보 동의 list")
                            .optional(),
                        new EnumFields(PolicyType.class).withPath(
                                "policyAgreementList[].policyType")
                            .description("개인정보 동의 타입"),
                        fieldWithPath("policyAgreementList[].version").type(JsonFieldType.STRING)
                            .description("개인정보 동의 버전"),
                        fieldWithPath("policyAgreementList[].isAgree").type(JsonFieldType.BOOLEAN)
                            .description("개인정보 동의 여부"),
                        fieldWithPath("policyAgreementList[].updatedAt").type(JsonFieldType.NULL)
                            .description("무시해주세요!!! 문서 자동화로 인해 추가된 필드입니다. 실제로는 필요없습니다.")
                            .optional()
                    )
                    .responseFields(
                        fieldWithPath("policyAgreementList[]").description("개인정보 동의 list")
                            .optional(),
                        new EnumFields(PolicyType.class).withPath(
                                "policyAgreementList[].policyType")
                            .description("개인정보 동의 타입"),
                        fieldWithPath("policyAgreementList[].version").type(JsonFieldType.STRING)
                            .description("개인정보 동의 버전"),
                        fieldWithPath("policyAgreementList[].isAgree").type(JsonFieldType.BOOLEAN)
                            .description("개인정보 동의 여부"),
                        fieldWithPath("policyAgreementList[].updatedAt").type(JsonFieldType.STRING)
                            .description("개인정보 동의 업데이트 시간")
                    )
                    .build()
            )
        ));
  }
}
