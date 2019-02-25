USE bacefook;
ALTER TABLE additional_users_info ADD COLUMN  cover_photo_id INT(11) NULL DEFAULT NULL AFTER profile_photo_id;
ALTER TABLE additional_users_info ADD CONSTRAINT fk_info FOREIGN KEY (id) REFERENCES users(id);
ALTER TABLE genders ADD UNIQUE (gender_name);
select * from genders;
SELECT * FROM additional_users_info;