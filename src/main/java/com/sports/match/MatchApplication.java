package com.sports.match;

import com.sports.match.main.service.MainService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatchApplication implements CommandLineRunner {

	private final MainService mainService;

	// 생성자 주입
	public MatchApplication(MainService mainService) {
		this.mainService = mainService;
	}

	public static void main(String[] args) {
		SpringApplication.run(MatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// 서버 시작 후 실행되는 코드
		mainService.resetCourt();
	}
}
