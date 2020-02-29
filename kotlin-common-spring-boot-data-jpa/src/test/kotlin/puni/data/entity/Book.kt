package puni.data.entity

import javax.persistence.Entity

@Entity
class Book(val name: String = "") : AutoIdEntity()
