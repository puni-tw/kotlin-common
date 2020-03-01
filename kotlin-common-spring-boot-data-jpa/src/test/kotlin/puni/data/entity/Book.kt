package puni.data.entity

import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
class Book(
  val name: String = "",
  val price: Int = 100,
  @ManyToOne(targetEntity = Author::class)
  val author: Author = Author()
) : AutoIdEntity()
