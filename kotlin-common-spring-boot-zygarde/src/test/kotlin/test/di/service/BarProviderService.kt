package test.di.service

import org.springframework.stereotype.Service

@Service
class BarProviderService {

  fun bar(): String {
    return "bar"
  }
}
