package puni.data.entity

import javax.persistence.Entity

@Entity
class Author(
  var name: String = "",
  var country: String = ""
) : AutoIdEntity()
