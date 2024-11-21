package com.encapsulados.model.slick

case class Comment(id: Long, text: String, author_id: Long, parentCommentId: Long, post_id: Long)
