package puni.data.entity

import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
class Book(
  var name: String = "",
  var price: Int = 0,
  @ManyToOne(targetEntity = Author::class)
  var author: Author = Author()
) : AutoIdEntity()
