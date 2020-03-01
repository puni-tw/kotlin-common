package puni.data.entity

import javax.persistence.Entity

@Entity
class AuthorGroup(
  var name: String = ""
) : AutoIdEntity()
