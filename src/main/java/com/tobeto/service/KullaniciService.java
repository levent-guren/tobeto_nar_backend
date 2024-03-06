package com.tobeto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tobeto.entity.Kullanicilar;
import com.tobeto.entity.Roller;
import com.tobeto.repository.KullaniciRepository;
import com.tobeto.repository.RollerRepository;

import jakarta.transaction.Transactional;

@Service
public class KullaniciService {
	@Autowired
	private KullaniciRepository kullanicilarRepository;
	@Autowired
	private RollerRepository rollerRepository;
	// @Autowired
	// private Kullanici

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	public Optional<Kullanicilar> getKullanici(String adi) {
		Optional<Kullanicilar> kullanicilar = kullanicilarRepository.findByKullaniciAdi(adi);
		if (kullanicilar.isPresent()) {
			kullanicilar.get().getRollers();
		}
		return kullanicilar;
	}

	public boolean sifreDegistir(String eskiSifre, String yeniSifre, String adi) {
		Optional<Kullanicilar> kullanicilar = kullanicilarRepository.findByKullaniciAdi(adi);
		if (kullanicilar.isPresent()) {
			// kullanıcı, adına göre veritabanında bulundu.
			// şifresini kontrol edelim.
			Kullanicilar kullanici = kullanicilar.get();
			if (passwordEncoder.matches(eskiSifre, kullanici.getSifre())) {
				// şifresi doğru. Şifresini yeni şifre ile güncelleyelim.
				kullanici.setSifre(passwordEncoder.encode(yeniSifre));
				kullanicilarRepository.save(kullanici);
				return true;
			}
		}
		return false;
	}

	@Transactional
	public Kullanicilar kayitOl(String email, String password) {
		if (kullanicilarRepository.findByKullaniciAdi(email).isPresent()) {
			// yeni kayıt işleminde var olan bir kayıdın email'i verilmiş.
			// Hata döndürelim.
			throw new RuntimeException("Bu isimde bir kullanıcı var");
		}
		final Kullanicilar kullanicilar = new Kullanicilar();
		kullanicilar.setKullaniciAdi(email);
		List<Roller> roller = rollerRepository.findAll();
		roller = roller.stream().filter(r -> !r.getRol().equals("admin")).toList();
		// ManytoMany ilişki olduğu için java sınıflarında da iki yönlü ilişki
		// oluşturmak gerekiyor.
		kullanicilar.setRollers(roller);
		roller.forEach(r -> r.getKullanicilars().add(kullanicilar));
		kullanicilar.setSifre(passwordEncoder.encode(password));
		return kullanicilarRepository.save(kullanicilar);
	}
}
