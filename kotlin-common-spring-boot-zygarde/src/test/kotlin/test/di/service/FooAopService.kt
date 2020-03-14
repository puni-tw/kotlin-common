package test.di.service

import puni.zygarde.di.DiService

interface FooAopService : DiService {
  fun foo(): String
  fun bar(): String
}
