package com.catering.dto.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecordExistDto {

	private Boolean isExist;

	private Boolean isExistSymbol;

	private Boolean isExistMobile;

	private Boolean isCurrentPasswordWrong;

	private Boolean isNameDefaultLang;

	private Boolean isNamePreferLang;

	private Boolean isNameSupportiveLang;

	private Boolean isSymbolDefaultLang;

	private Boolean isSymbolPreferLang;

	private Boolean isSymbolSupportiveLang;

}