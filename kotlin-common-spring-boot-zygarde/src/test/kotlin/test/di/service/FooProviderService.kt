package test.di.service

import puni.zygarde.di.DiService

interface FooProviderService : DiService {

  fun foo(): String
}
