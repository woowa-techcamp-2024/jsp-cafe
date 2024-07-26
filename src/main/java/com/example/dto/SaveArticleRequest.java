package com.example.dto;

import com.example.annotation.NotNull;
import com.example.dto.util.DtoValidationUtil;

public record SaveArticleRequest(
	@NotNull String title,
	@NotNull String contents
) {

	public void validate() {
		DtoValidationUtil.validate(this);
	}
}
