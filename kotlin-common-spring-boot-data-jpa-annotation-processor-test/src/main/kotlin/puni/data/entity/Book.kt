package puni.data.entity

import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
class Book(
  var bookName: String = "",
  @ManyToOne(targetEntity = Author::class)
  var author: Author = Author()
) : AutoIdEntity()
