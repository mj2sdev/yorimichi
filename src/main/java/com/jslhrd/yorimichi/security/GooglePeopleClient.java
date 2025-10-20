package com.jslhrd.yorimichi.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GooglePeopleClient {

	private final RestClient.Builder rest;

	public Person fetchMe(String accessToken) {
		try {
			return rest.build()
					.get()
					.uri("https://people.googleapis.com/v1/people/me?personFields=genders,birthdays")
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
					.retrieve()
					.onStatus(s -> s.is4xxClientError() || s.is5xxServerError(),
							(req, res) -> {
								String body = (res.getBody() != null)
										? new String(res.getBody().readAllBytes())
										: "";
								// 예: insufficientAuthenticationScopes, accessNotConfigured 등 확인
								// 꼭 로그로 남겨 원인 분류
								System.err.println("People API error " + res.getStatusCode() + " body=" + body);
							})
					.body(Person.class);
		} catch (Exception e) {
			return null;
		}
	}

	public String extractGender(Person person) {

		if (person == null || person.getGenders() == null || person.getGenders().isEmpty()) {
			return null;
		}

		var primary = person.getGenders().stream()
				.filter(g -> g.getMetadata() != null && Boolean.TRUE.equals(g.getMetadata().getPrimary()))
				.findFirst().orElse(person.getGenders().get(0));
		var v = primary.getValue();
		if ("male".equalsIgnoreCase(v)) return "M";
		if ("female".equalsIgnoreCase(v)) return "F";
		return null; // unspecified 등
	}

	public Integer extractBirthYear(Person person) {

		if (person == null || person.getBirthdays() == null || person.getBirthdays().isEmpty()) {
			return null;
		}

		Birthday primary = person.getBirthdays().stream()
				.filter(b -> b.getMetadata() != null && Boolean.TRUE.equals(b.getMetadata().getPrimary()))
				.findFirst().orElse(person.getBirthdays().get(0));
		return (primary.getDate() != null) ? primary.getDate().getYear() : null; // 연도 없을 수 있음
	}

	/* 필요한 필드만 최소 매핑 */
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Person {
		private List<Gender> genders;
		private List<Birthday> birthdays;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Gender {
		private String value;   // "male" | "female" | ...
		private Meta metadata;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Birthday {
		private Date date;      // year/month/day (year가 없을 수도)
		private Meta metadata;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Date {
		private Integer year, month, day;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Meta {
		private Boolean primary;
	}
}