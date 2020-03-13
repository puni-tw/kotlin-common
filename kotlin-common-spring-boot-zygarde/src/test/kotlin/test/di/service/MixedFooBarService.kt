package test.di.service

import puni.zygarde.di.DiService

interface MixedFooBarService : DiService {
  fun foobar(): String
}
