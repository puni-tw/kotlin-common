package test.di.service

import org.springframework.stereotype.Service
import puni.zygarde.di.DiService

@Service
class BarProviderService : DiService {

  fun bar(): String {
    return "bar"
  }
}
