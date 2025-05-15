package com.catering.dto.tenant.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersWithMainMenuListDto extends UserDto {

	private List<UserRightsMainMenuSubMenuDto> mainMenu;

}