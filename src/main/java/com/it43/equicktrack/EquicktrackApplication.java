package com.it43.equicktrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * I always overthink why doing this project feel so lonely and unhappy
 * is it because I can only rely on myself? My thoughts are filling my head
 * is it because knowing I'll code this project alone? My anxiety never fade away
 * but never shared to anyone else, I never assumed someone would care
 * I was there when they're at lowest, but when I'm at my lowest and needs help
 * I guess all I need is to be heard willingly and not to be misjudged
 * Author: Emmanuel Male
 *
 */

// TODO: Uncomment @EnableScheduling when most of the functions are finished
@EnableScheduling
@SpringBootApplication
public class EquicktrackApplication {
	public static void main(String[] args) {
		SpringApplication.run(EquicktrackApplication.class, args);
	}
}
