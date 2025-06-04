package site.smartenglish.repository

import site.smartenglish.remote.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LearnedRepository @Inject constructor(
    private val api: ApiService
){

}