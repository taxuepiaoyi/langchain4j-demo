CREATE TABLE `user` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `username` varchar(50) NOT NULL,
                        `password` varchar(100) NOT NULL,
                        `email` varchar(50) DEFAULT NULL,
                        `age` int DEFAULT NULL,
                        `id_card` varchar(100) NOT NULL COMMENT '身份证号',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `idx_id_card` (`id_card`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci