package puni.zygarde.api.value

import puni.data.entity.AutoIdEntity
import puni.data.entity.getId

class AutoIdValueProvider : ValueProvider<AutoIdEntity, Long> {
  override fun getValue(v: AutoIdEntity): Long = v.getId()
}
