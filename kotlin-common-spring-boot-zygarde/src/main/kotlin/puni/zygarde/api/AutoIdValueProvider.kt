package puni.zygarde.api

import puni.data.entity.AutoIdEntity
import puni.data.entity.getId

class AutoIdValueProvider : DtoValueProvider<AutoIdEntity, Long> {
  override fun getValue(entity: AutoIdEntity): Long {
    return entity.getId()
  }
}
