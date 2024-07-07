package org.crops.fitserver.domain.mail.constants;

public class MailTemplates {

  public static String DEFAULT_TEMPLATE =
      "<div style=\"margin-top: 50px;\">\n"
          + "                <img src=\"https://d2ueefa0uvyh4f.cloudfront.net/mail/images/logo.png\" alt=\"F-IT &#47196;&#44256;\" style=\"width: 160px; height: 51.526px;\">\n"
          + "              </div>\n"
          + "              <div style=\"margin-top: 30px;\">\n"
          + "                ${content}\n"
          + "              </div>\n"
          + "              <div style=\"margin-top: 60px; text-align: center;\">\n"
          + "                <a href=\"${url}\" style=\"background-color: #FF8A80; color: white; padding: 15px 30px; border: none; border-radius: 10px; font-size: 16px; cursor: pointer; text-decoration: none;\">&#54168;&#51060;&#51648;&#47196; &#51060;&#46041; &rarr;</a>\n"
          + "              </div>\n"
      ;

  public static String CREATE_MATCHING_ROOM_TEMPLATE =
      "<h1>F-IT 대기방이 생성되었어요!</h1>"
          + "<p>안녕하세요, (주)구황작물입니다.</p>"
          + "<p>${nickname} 님의 <b>대기방이 드디어 생성되었어요.</b>"
          + "<br>지금 바로 F-IT 사이트에서 팀빌딩을 확인해주세요!</p>";

  public static String CREATE_MATCHING_ROOM_FOR_HOST_TEMPLATE =
      "<h1>F-IT 대기방이 생성되었어요!</h1>"
          + "<p>안녕하세요, (주)구황작물입니다.</p>"
          + "<p>${nickname} 님의 <b>대기방이 드디어 생성되었어요.</b>"
          + "<br>또한, ${nickname} 님은 방장이에요! 대기방의 팀원들과 이야기를 나눈 후, 프로젝트를 시작해주세요!"
          + "<br>지금 바로 F-IT 사이트에서 팀빌딩을 확인해주세요!</p>";

  public static String DONE_READY_COMPLETE_TEMPLATE =
      "<h1>F-IT 프로젝트를 시작해주세요!</h1>"
          + "<p>안녕하세요, (주)구황작물입니다.</p>"
          + "<p><b>대기방의 팀원들이 ${nickname} 님의 시작을 기다리고 있어요!</b>"
          + "<br>지금 바로 F-IT 사이트에서 팀원들을 확인해주세요!</p>";

  public static String START_PROJECT_TEMPLATE =
      "<h1>F-IT 대기방이 생성되었어요!</h1>"
          + "<p>안녕하세요, (주)구황작물입니다.</p>"
          + "<p>대기방 팀원들과의 <b>프로젝트가 시작</b>되었어요!</b>"
          + "<br>지금 바로 F-IT 사이트에서 팀원들을 확인해주세요!</p>";

  public static String REPORT_TEMPLATE =
      "<h1>F-IT 신고가 접수되었습니다.</h1>"
      + "<br>유저이름(닉네임, userId, 이메일)"
      + "<br>신고한 유저: ${reporter.username}(${reporter.nickname}, ${reporter.id}, ${reporter.email})"
      + "<br>신고당한 유저: ${reported.username}(${reported.nickname}, ${reported.id}, ${reported.email})"
      + "<br>신고 내용: ${reportContent}</p>";
}
