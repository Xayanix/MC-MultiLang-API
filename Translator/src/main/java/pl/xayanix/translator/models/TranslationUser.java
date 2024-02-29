package pl.xayanix.translator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class TranslationUser {

	private final UUID name;
	private TranslationLocale locale;

}
