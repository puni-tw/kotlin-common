package puni.data.entity

import javax.persistence.Entity

@Entity
class Bar(val name: String = "") : AutoIdEntity()
