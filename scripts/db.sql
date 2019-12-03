create schema `entry_db`;

CREATE TABLE `entry_db`.`entry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` blob,
  `add_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=138 DEFAULT CHARSET=latin1;
