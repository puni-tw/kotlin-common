package puni.data.entity

import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
class Author(
  var name: String = "",
  @ManyToOne(targetEntity = AuthorGroup::class)
  var authorGroup: AuthorGroup? = null
) : AutoIdEntity()
