package scraper.web;

import scraper.data.Site;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class LanguageCheck {
	private static final List<String> langs = new ArrayList<>(Arrays.asList(
			"af", "af-ZA",
			"sq", "sq-AL",
			"ar", "ar-SA",
			"hy", "hy-AM",
			"az", "az-AZ-Latn",
			"eu", "eu-ES",
			"be", "be-BY",
			"bg", "bg-BG",
			"ca", "ca-ES",
			"zh", "cn", "zh-CHS",
			"hr", "hr-HR",
			"cs", "cs-CZ",
			"da", "da-DK",
			"div", "div-MV",
			"nl", "nl-NL",
			//"en", "en-US", we want en
			"et", "et-EE",
			"fo", "fo-FO",
			"fa", "fa-IR",
			"fi", "fi-FI",
			"fr", "fr-FR",
			"gl", "gl-ES",
			"ka", "ka-GE",
			"de", "de-DE",
			"el", "el-GR",
			"gu", "gu-IN",
			"he", "he-IL",
			"hi", "hi-IN",
			"hu", "hu-HU",
			"is", "is-IS",
			"id", "id-ID",
			"it", "it-IT",
			"ja", "jp", "ja-JP",
			"kn", "kn-IN",
			"kk", "kk-KZ",
			"kok", "kok-IN",
			"ko", "kr", "ko-KR",
			"ky", "ky-KZ",
			"lv", "lv-LV",
			"lt", "lt-LT",
			"mk", "mk-MK",
			"ms", "ms-MY",
			"mr", "mr-IN",
			"mn", "mn-MN",
			"no", "nn-NO",
			"nb", "nb-NO",
			"pl", "pl-PL",
			"pt", "pt-PT",
			"pa", "pa-IN",
			"ro", "ro-RO",
			"ru", "ru-RU",
			"sa", "sa-IN",
			"sr", "sr-SP-Latn",
			"sk", "sk-SK",
			"sl", "sl-SI",
			"es", "es-ES",
			"sw", "sw-KE",
			"sv", "sv-SE",
			"syr", "syr-SY",
			"ta", "ta-IN",
			"tt", "tt-RU",
			"te", "te-IN",
			"th", "th-TH",
			"tr", "tr-TR",
			"uk", "uk-UA",
			"ur", "ur-PK",
			"uz", "uz-UZ-Latn",
			"vi", "vi-VN"));
	
	public static void main(String[] args) {
		String url = "http://gatherproxy.com/ja/anonymousproxylist";
		List<String> links = new ArrayList<>(Arrays.asList("http://gatherproxy.com/anonymousproxylist"));
		boolean is = isFromOtherLang(url, links);
	}
	
	public static boolean isFromOtherLang(Site site, List<Site> sites) {
		return isFromOtherLang(site.getAddress(),
							   sites.stream()
									   .map(Site::getAddress)
									   .collect(Collectors.toList()));
	}
	
	public static boolean isFromOtherLang(String url, List<String> links) {
		for (String link : links) {
			if (langs.contains(stringsIntersection(link, url)
									   .replace("/", "")
									   .toLowerCase()))
				return true;
		}
		return false;
	}
	
	private static String stringsIntersection(String one, String two) {
		char[] a1 = one.toCharArray();
		char[] a2 = two.toCharArray();
		
		for (int i = 0; i < a1.length; i++) {
			for (int j = 0; j < a2.length; j++) {
				if (a1[i] == a2[j]) {
					a2[j] = '#';
					break;
				}
			}
		}
		return new String(a2).replace("#", "");
	}
}
