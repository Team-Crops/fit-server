package org.crops.fitserver.domain.skillset.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateSkillRequest (@NotBlank String displayName){

}
