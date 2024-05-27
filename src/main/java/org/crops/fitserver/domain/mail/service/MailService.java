package org.crops.fitserver.domain.mail.service;

import org.crops.fitserver.domain.mail.dto.MailRequiredInfo;
import org.crops.fitserver.domain.mail.dto.MailType;

public interface MailService {
  public void send(MailType mailType, String email, MailRequiredInfo mailRequiredInfo);
}
