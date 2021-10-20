package ru.myproevent.domain.model

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class ProEventRepository @Inject constructor(private val api: IProEventDataSource) :
    IProEventRepository {
    override fun getToken(loginBody: LoginBody): String? {
        val response = api.getToken(loginBody)?.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                Log.d("[MYLOG]", "getToken url: ${response.raw().request().url()}")
                Log.d("[MYLOG]", "getToken status: ${response?.code()}")
                Log.d("[MYLOG]", "getToken request: ${response.raw().request()}")
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                Log.d("[MYLOG]", "getToken onFailure")
            }
        })
        return ""
    }
}