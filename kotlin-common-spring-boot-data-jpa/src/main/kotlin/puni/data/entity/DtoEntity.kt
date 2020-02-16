package puni.data.entity

interface DtoEntity<DTO> {

  abstract fun toDto(): DTO
}
