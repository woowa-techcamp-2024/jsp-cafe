CREATE TABLE IF NOT EXISTS `users` (
    `userId` varchar(255) NOT NULL,
    `name` varchar(255) NOT NULL,
    `password` varchar(255) NOT NULL,
    `email` varchar(255) NOT NULL,
    PRIMARY KEY (`userId`))
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `articles` (
    `articleId` varchar(255) NOT NULL,
    `writer` varchar(255) NOT NULL,
    `title` varchar(255) NOT NULL,
    `contents` text NOT NULL,
    `created` varchar(255) NOT NULL,
    PRIMARY KEY (`articleId`))
    ENGINE=InnoDB DEFAULT CHARSET=utf8;
