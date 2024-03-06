package com.tobeto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tobeto.dto.login.LoginRequestDTO;
import com.tobeto.dto.login.LoginResponseDTO;
import com.tobeto.entity.Kullanicilar;
import com.tobeto.service.KullaniciService;
import com.tobeto.service.TokenService;

@RestController
@RequestMapping("/api/v1")
public class SignupController {
	@Autowired
	private KullaniciService kullaniciService;
	@Autowired
	private TokenService tokenService;

	@PostMapping("/signup")
	public ResponseEntity<LoginResponseDTO> signup(@RequestBody LoginRequestDTO dto) {
		Kullanicilar oKullanicilar = kullaniciService.kayitOl(dto.getEmail(), dto.getPassword());
		String token = tokenService.createToken(oKullanicilar);
		return ResponseEntity.ok(new LoginResponseDTO(token));
	}

}
