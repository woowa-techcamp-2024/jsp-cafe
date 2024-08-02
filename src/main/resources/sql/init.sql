CREATE TABLE IF NOT EXISTS `users` (
    `userId` varchar(255) NOT NULL,
    `name` varchar(255) NOT NULL,
    `password` varchar(255) NOT NULL,
    `email` varchar(255) NOT NULL,
    `created` varchar(255) NOT NULL,
    `deleted` boolean NOT NULL DEFAULT false,
    PRIMARY KEY (`userId`))
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `articles` (
    `articleId` varchar(255) NOT NULL,
    `writer` varchar(255) NOT NULL,
    `title` varchar(255) NOT NULL,
    `contents` text NOT NULL,
    `created` varchar(255) NOT NULL,
    `deleted` boolean NOT NULL DEFAULT false,
    PRIMARY KEY (`articleId`),
    FOREIGN KEY (`writer`) REFERENCES `users`(`userId`) ON DELETE CASCADE)
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `comments` (
    `commentId` varchar(255) NOT NULL,
    `userId` varchar(255) NOT NULL,
    `articleId` varchar(255) NOT NULL,
    `contents` text NOT NULL,
    `created` varchar(255) NOT NULL,
    `deleted` boolean NOT NULL DEFAULT false,
    PRIMARY KEY (`commentId`),
    FOREIGN KEY (`articleId`) REFERENCES `articles`(`articleId`) ON DELETE CASCADE,
    FOREIGN KEY (`userId`) REFERENCES `users`(`userId`) ON DELETE CASCADE)
    ENGINE=InnoDB DEFAULT CHARSET=utf8;