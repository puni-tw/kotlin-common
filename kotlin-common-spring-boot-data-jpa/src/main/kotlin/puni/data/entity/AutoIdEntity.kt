package puni.data.entity

import java.util.Objects
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class AutoIdEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null

  override fun equals(other: Any?): Boolean {
    return if (other == null) {
      false
    } else if (other is AutoIdEntity) {
      if (other.id == null || this.id == null) {
        return false
      }
      if (other.javaClass == this.javaClass) {
        Objects.equals(id, other.id)
      } else {
        false
      }
    } else {
      false
    }
  }

  override fun hashCode() = Objects.hashCode(id)
}
