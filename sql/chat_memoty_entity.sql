CREATE TABLE `chat_memory_entity` (
                                      `id` bigint NOT NULL AUTO_INCREMENT,
                                      `memory_id` varchar(100) NOT NULL,
                                      `messages_json` longtext,
                                      `updated_at` datetime DEFAULT NULL,
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `memory_id` (`memory_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1004 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci