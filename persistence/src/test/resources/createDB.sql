-- -----------------------------------------------------
-- Table `mydb`.`user`
-- -----------------------------------------------------

CREATE TABLE `users`
(
    `id`   INT         NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`)
);


-- -----------------------------------------------------
-- Table `rest-api`.`gift_certificate`
-- -----------------------------------------------------

CREATE TABLE `gift_certificates`
(
    `id`                INT          NOT NULL AUTO_INCREMENT,
    `name`              VARCHAR(45)  NOT NULL,
    `description`       VARCHAR(255) NULL DEFAULT NULL,
    `price`             INT          NOT NULL,
    `duration`          INT          NOT NULL,
    `create_date`       DATETIME     NULL DEFAULT CURRENT_TIMESTAMP,
    `last_updated_date` DATETIME     NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
);


-- -----------------------------------------------------
-- Table `mydb`.`order`
-- -----------------------------------------------------

CREATE TABLE `orders`
(
    `id`                  INT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`             INT      NOT NULL,
    `gift_certificate_id` INT      NOT NULL,
    `order_date`          DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
    `cost`                INT,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`)
        REFERENCES `users` (`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,
    FOREIGN KEY (`gift_certificate_id`)
        REFERENCES `gift_certificates` (`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);


-- -----------------------------------------------------
-- Table `rest-api`.`tag`
-- -----------------------------------------------------

CREATE TABLE `tags`
(
    `id`   INT         NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NULL DEFAULT NULL,
    PRIMARY KEY (`id`)
);


-- -----------------------------------------------------
-- Table `rest-api`.`gift_certificate_has_tag`
-- -----------------------------------------------------

CREATE TABLE `gift_certificate_has_tag`
(
    `gift_certificate_id` INT NOT NULL,
    `tag_id`              INT NOT NULL,
    PRIMARY KEY (`gift_certificate_id`, `tag_id`),

    FOREIGN KEY (`gift_certificate_id`)
        REFERENCES `gift_certificates` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (`tag_id`)
        REFERENCES `tags` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);



