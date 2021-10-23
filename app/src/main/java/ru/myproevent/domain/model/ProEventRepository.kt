package ru.myproevent.domain.model

import android.util.Log
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class ProEventRepository @Inject constructor(private val api: IProEventDataSource) :
    IProEventRepository {
    override fun getToken(loginBody: LoginBody): String? {

        val jsonObj = JSONObject()

        try {
            jsonObj.put("email", "grishanin.slava@yandex.ru")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        try {
            jsonObj.put("password", "password")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        Log.d("[MYLOG]", "jsonObj: $jsonObj")

//        val json = "{\"email\":\"grishanin.slava@yandex.ru\",\"password\":\"password\"}"
//        val `in`: RequestBody =
//            TypedByteArray("application/json", json.toByteArray(charset("UTF-8")))
//        val response: FooResponse = foo.postRawJson(`in`)

        val response = api.getToken(jsonObj)?.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                Log.d("[MYLOG]", "getToken url: ${response.raw().request.url}")
                Log.d("[MYLOG]", "getToken status: ${response?.code()}")
                Log.d("[MYLOG]", "getToken request: ${response.raw().request}")
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                Log.d("[MYLOG]", "getToken onFailure")
            }
        })
        return ""
    }
}