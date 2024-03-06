ALTER TABLE `nar`.`yazilim_ilan` 
ADD COLUMN `kullanici_id` BINARY(16) NOT NULL AFTER `ucret`,
ADD INDEX `yazilim_ilan_kul_id_fk_idx` (`kullanici_id` ASC) VISIBLE;

ALTER TABLE `nar`.`yazilim_ilan` 
ADD CONSTRAINT `yazilim_ilan_kul_id_fk`
  FOREIGN KEY (`kullanici_id`)
  REFERENCES `nar`.`kullanicilar` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
