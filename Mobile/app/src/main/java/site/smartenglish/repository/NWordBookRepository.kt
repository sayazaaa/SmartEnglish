package site.smartenglish.repository

import site.smartenglish.remote.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NWordBookRepository @Inject constructor(
    private val api: ApiService
){

}