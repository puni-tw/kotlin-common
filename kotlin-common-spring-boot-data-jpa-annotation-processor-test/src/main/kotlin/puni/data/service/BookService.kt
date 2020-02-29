package puni.data.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import puni.data.dao.BookDao
import puni.data.entity.search.author
import puni.data.entity.search.name
import puni.data.search.search

@Service
class BookService(
  @Autowired val bookDao: BookDao
) {

  fun countByName(name: String): Int {
    return bookDao.search { book ->
      book.name().eq(name)
    }.size
  }

  fun countByNameAndAuthorName(name: String, authorName: String): Int {
    return bookDao.search { book ->
      book.name().eq(name)
        .author().name().eq(authorName)
    }.size
  }
}
