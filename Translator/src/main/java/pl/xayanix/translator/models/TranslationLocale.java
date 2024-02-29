package pl.xayanix.translator.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TranslationLocale {
	PL("Polski"),
	EN("English"),
	DE("Deutsch"),
	FR("Français"),
	ES("Español"),
	IT("Italiano"),
	PT("Português"),
	RU("Русский"),
	NL("Nederlands"),
	SV("Svenska"),
	NO("Norsk"),
	DA("Dansk"),
	FI("Suomi"),
	TR("Türkçe"),
	RO("Română"),
	HU("Magyar"),
	CS("Čeština"),
	SK("Slovenčina"),
	HR("Hrvatski"),
	SL("Slovenščina"),
	ET("Eesti"),
	LV("Latviešu"),
	LT("Lietuvių"),
	SQ("Shqip"),
	UZ("Oʻzbekcha"),
	TK("Türkmen");

	private final String name;
}
